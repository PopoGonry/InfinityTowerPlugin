package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.PlayerInTowerException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.TowerNotWorkingException;
import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.Monster.Exception.MonsterNotFoundException;
import com.popogonry.infinityTowerPlugin.Monster.Monster;
import com.popogonry.infinityTowerPlugin.Monster.MonsterService;
import com.popogonry.infinityTowerPlugin.PluginRepository;
import com.popogonry.infinityTowerPlugin.Ranking.RankingService;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecord;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecordRepository;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecordService;
import com.popogonry.infinityTowerPlugin.Reference;
import com.popogonry.infinityTowerPlugin.Reward.RewardRepository;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBox;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBoxRepository;
import com.popogonry.infinityTowerPlugin.TextDisplayHologram.TextDisplayHologramService;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scheduler.BukkitTask;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InfinityTowerProcess {

    private final Player player;
    private final StorageBox storageBox;

    private final InfinityTower infinityTower;
    private int round;
    private Location playerBeforeLocation;
    private long roundEndTick;
    private BukkitTask statusBarTask;

    private List<Entity> spawnedMinecraftMobs;
    private List<ActiveMob> spawnedMysticMobs;

    private MonsterService monsterService = new MonsterService();

    public InfinityTowerProcess(Player player, InfinityTower infinityTower) {
        this.player = player;
        this.storageBox = StorageBoxRepository.userStorageBoxHashMap.getOrDefault(player.getUniqueId(), new StorageBox(player.getUniqueId()));;
        this.infinityTower = infinityTower;
        this.round = 0;
    }

    public boolean isWorking() {
        return InfinityTowerRepository.infinityTowerPlayerHashMap.containsKey(player);
    }


    public void start() {
        if(isWorking()) throw new PlayerInTowerException("This player is already working");

        InfinityTowerRepository.infinityTowerPlayerHashMap.put(player, this);

        round = 1;

        playerBeforeLocation = player.getLocation().clone();

        player.teleport(new Location(Bukkit.getWorld(infinityTower.getArea().getWorldName()), infinityTower.getSpawnLocation()[0], infinityTower.getSpawnLocation()[1], infinityTower.getSpawnLocation()[2]));


        player.sendTitle("§6무한의 탑에 입장하셨습니다!", "§8도전자: " + player.getName() , 10, 40, 10);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(Reference.prefix_normal + "10초 후 몬스터가 출몰합니다!");
                startTitleCountdown(10);
            }
        }.runTaskLater(InfinityTowerPlugin.getServerInstance(), 60L); // 40 ticks = 2 seconds


        Bukkit.getScheduler().runTaskLater(InfinityTowerPlugin.getServerInstance(), this::infinityTowerCycle, 260L);
    }

    public void stop() {
        if(!isWorking()) throw new TowerNotWorkingException("This player is not working");

        removeAllRemainingMobs();

        RoundRecordService roundRecordService = new RoundRecordService();
        RankingService rankingService = new RankingService();


        player.sendMessage(Reference.prefix_normal + round + "층에서 도전이 끝났습니다!");


        RoundRecord record = new RoundRecord(player.getUniqueId(), player.getName(), round - 1);

        if (roundRecordService.record(record, player)) {
            player.sendMessage(Reference.prefix_normal + "신기록 달성!");

            RoundRecordRepository roundRecordRepository = new RoundRecordRepository();
            RoundRecord roundRecord = roundRecordRepository.loadRecord(player);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");

            player.sendMessage(Reference.prefix_normal + "신기록: " + roundRecord.getRound() + "층 / 도전시간: " + roundRecord.getClearDateTime().format(formatter));
        }

        if(rankingService.addRanking(record)) {
            player.sendMessage(Reference.prefix_normal + "랭킹 순위권에 들어왔습니다!");
            TextDisplayHologramService textDisplayHologramService = new TextDisplayHologramService();
            textDisplayHologramService.updateHologram();
        }

        TextComponent prefix = new TextComponent("§f§l< §6§l무한의 탑 §f§l>§7§r " + (round - 1) + "층까지의 보상이 보상함에 지급되었습니다! ");

        TextComponent clickable = new TextComponent(ChatColor.GOLD + "[보상함]");
        clickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/무한의탑 보상"));
        clickable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("클릭 시 보상함 열기").create()));


        // 전체 메시지 조합
        prefix.addExtra(clickable);

        player.spigot().sendMessage(prefix);

        InfinityTowerRepository.infinityTowerPlayerHashMap.remove(player);
        round = 0;

        if(playerBeforeLocation != null) {
            player.teleport(playerBeforeLocation);
        }

    }


    private int getRemainingSeconds() {
        long currentTick = Bukkit.getCurrentTick();
        long remainingTicks = roundEndTick - currentTick;
        return (int) Math.max(remainingTicks / 20, 0); // 초 단위로 변환
    }

    private void giveRewards(int round) {
        if(RewardRepository.rewardsHashMap.containsKey(round)) {
            storageBox.getInventory().add(RewardRepository.rewardsHashMap.get(round));
        }
    }

    private void nextRound() {
        round++;
        Bukkit.getScheduler().runTaskLater(InfinityTowerPlugin.getServerInstance(), this::infinityTowerCycle, 200L);
    }

    private void clearCurrentRound() {
        if (!isWorking()) return;
        removeAllRemainingMobs();
        player.sendTitle("§a라운드 클리어!", "§f다음 라운드 준비 중...", 10, 40, 10);
        roundEndTick = Bukkit.getCurrentTick() + (200L);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        giveRewards(round);
        nextRound();
    }

    public void failRound(String reason) {
        if (!isWorking()) return;
        player.sendTitle("§c게임 오버", "§f" + reason, 10, 40, 10);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0f, 1.0f);

        if (statusBarTask != null) {
            statusBarTask.cancel();
            statusBarTask = null;
        }

        stop();
    }

    private void infinityTowerCycle() {
        if(round == 0) return;
        List<List<Monster>> spawnMonsters = monsterService.getSpawnMonsters(round);
        spawnedMinecraftMobs = new ArrayList<>();
        spawnedMysticMobs = new ArrayList<>();
        int thisRound = round;

        long delayPerWave = 20L * PluginRepository.pluginConfig.getMobSpawnDelaySeconds();
        int waveIndex = 0;

        int monsterNumber = 0;
        for (List<Monster> spawnMonster : spawnMonsters) {
            monsterNumber += spawnMonster.size();
        }

        player.sendTitle("§e라운드 " + thisRound, "§f총 몬스터수: " + monsterNumber, 10, 40, 10);
        roundEndTick = Bukkit.getCurrentTick() + (PluginRepository.pluginConfig.getRoundClearSeconds() * 20L);

        for (List<Monster> wave : spawnMonsters) {
            long delay = waveIndex * delayPerWave;
            Bukkit.getScheduler().runTaskLater(InfinityTowerPlugin.getServerInstance(), () -> {
                if(thisRound != round || !isWorking()) return;
                for (Monster monster : wave) {
                    Location spawnLoc = monsterService.getMonsterSpawnLocationInArea(infinityTower.getArea());
                    if (spawnLoc == null) continue;

                    if (monster.isMysticMob()) {
                        MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(monster.getName()).orElse(null);
                        if (mob == null) continue;
                        ActiveMob activeMob = mob.spawn(BukkitAdapter.adapt(spawnLoc), 1);
                        spawnedMysticMobs.add(activeMob);
                    } else {
                        try {
                            EntityType type = monsterService.getMonsterByName(monster.getName());
                            Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, type);
                            spawnedMinecraftMobs.add(entity);
                        } catch (MonsterNotFoundException ignored) {}
                    }
                }
            }, delay);
            waveIndex++;
        }

        // 일정 시간 생존 클리어
        Bukkit.getScheduler().runTaskLater(InfinityTowerPlugin.getServerInstance(), () -> {
            if (!isWorking()) return;
            if (isPlayerStillAlive() && thisRound == round) {
                clearCurrentRound();
            }
        }, PluginRepository.pluginConfig.getRoundClearSeconds() * 20L);

        // 몬스터 전멸 감시
        if (statusBarTask != null) {
            statusBarTask.cancel(); // 이전 라운드의 타이머 제거
        }
        statusBarTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isWorking() || !isPlayerStillAlive()) {
                    this.cancel();
                    return;
                }

                if (isAllMobsDead() && thisRound == round) {
                    clearCurrentRound();
                }

                int aliveCount = getAliveMobCount();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§f남은 몬스터: " + aliveCount + " / 남은시간: " + formatTime(getRemainingSeconds())));
            }
        }.runTaskTimer(InfinityTowerPlugin.getServerInstance(), 20L, 20L);
    }

    private int getAliveMobCount() {
        int aliveMinecraftMobs = 0;
        int aliveMysticMobs = 0;

        if (spawnedMinecraftMobs != null) {
            aliveMinecraftMobs = (int) spawnedMinecraftMobs.stream()
                    .filter(entity -> entity != null && !entity.isDead())
                    .count();
        }

        if (spawnedMysticMobs != null) {
            aliveMysticMobs = (int) spawnedMysticMobs.stream()
                    .filter(mob -> mob.getEntity() != null && !mob.getEntity().isDead())
                    .count();
        }

        return aliveMinecraftMobs + aliveMysticMobs;
    }


    private void removeAllRemainingMobs() {
        if (spawnedMinecraftMobs != null) {
            for (Entity entity : spawnedMinecraftMobs) {
                if (!entity.isDead()) entity.remove();
            }
            spawnedMinecraftMobs.clear();
        }

        if (spawnedMysticMobs != null) {
            for (ActiveMob mob : spawnedMysticMobs) {
                Entity le = mob.getEntity().getBukkitEntity();
                if (le != null && !le.isDead()) le.remove();
            }
            spawnedMysticMobs.clear();
        }
    }

    private boolean isAllMobsDead() {
        return (spawnedMinecraftMobs == null || spawnedMinecraftMobs.stream().allMatch(Entity::isDead))
                && (spawnedMysticMobs == null || spawnedMysticMobs.stream().allMatch(mob -> mob.getEntity() == null || mob.getEntity().isDead()));
    }

    public void startTitleCountdown(int seconds) {
        new BukkitRunnable() {
            int count = seconds;

            @Override
            public void run() {
                if (count <= 0) {
                    cancel();
                    return;
                }

                // 타이틀 표시
                player.sendTitle("§e" + count, "", 0, 20, 0);

                // 틱당 효과음
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);

                count--;
            }
        }.runTaskTimer(InfinityTowerPlugin.getServerInstance(), 0L, 20L);
    }


    private boolean isPlayerStillAlive() {
        return player.isOnline() && !player.isDead() && isWorking();
    }

    public InfinityTower getInfinityTower() {
        return infinityTower;
    }

    public Location getPlayerBeforeLocation() {
        return playerBeforeLocation;
    }

    public static String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append("시간 ");
        if (minutes > 0) sb.append(minutes).append("분 ");
        if (seconds > 0 || sb.length() == 0) sb.append(seconds).append("초");

        return sb.toString().trim();
    }
}

