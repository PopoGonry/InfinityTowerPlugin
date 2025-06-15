package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.PlayerInTowerException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.TowerNotWorkingException;
import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.Monster.Exception.MonsterNotFoundException;
import com.popogonry.infinityTowerPlugin.Monster.Monster;
import com.popogonry.infinityTowerPlugin.Monster.MonsterService;
import com.popogonry.infinityTowerPlugin.PluginRepository;
import com.popogonry.infinityTowerPlugin.Reference;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scheduler.BukkitTask;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InfinityTowerProcess {

    private final Player player;
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

        playerBeforeLocation = player.getLocation();
        player.teleport(new Location(Bukkit.getWorld(infinityTower.getArea().getWorldName()), infinityTower.getSpawnLocation()[0], infinityTower.getSpawnLocation()[1], infinityTower.getSpawnLocation()[2]));

        player.sendTitle("§6무한의 탑에 입장하셨습니다!", "§8도전자: " + player.getName() , 10, 40, 10);

        player.sendMessage(Reference.prefix_normal + "10초 후 몬스터가 출몰합니다!");


        Bukkit.getScheduler().runTaskLater(InfinityTowerPlugin.getServerInstance(), this::infinityTowerCycle, 200L);
    }

    public void stop() {
        if(!isWorking()) throw new TowerNotWorkingException("This player is not working");

        InfinityTowerRepository.infinityTowerPlayerHashMap.remove(player);

        removeAllRemainingMobs();
        giveRewards();
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

    private void giveRewards() {
        player.sendMessage(round - 1 + " 까지의 보상 지급");
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
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§f남은 몬스터: " + aliveCount + " / 남은시간: " + getRemainingSeconds()));
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

    private boolean isPlayerStillAlive() {
        return player.isOnline() && !player.isDead() && isWorking();
    }

    public InfinityTower getInfinityTower() {
        return infinityTower;
    }
}

