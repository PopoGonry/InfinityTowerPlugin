package com.popogonry.infinityTowerPlugin.Monster;

import com.popogonry.infinityTowerPlugin.Area.Area;
import com.popogonry.infinityTowerPlugin.InfinityTower.InfinityTowerRepository;
import com.popogonry.infinityTowerPlugin.Monster.Exception.MonsterNotFoundException;
import com.popogonry.infinityTowerPlugin.PluginRepository;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.*;

public class MonsterService {

    private final Random rand = new Random();


    public void createDefaultMonster() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        Monster monster = new Monster(UUID.randomUUID(), "ZOMBIE", false, 100, list);
        MonsterRepository.monsterHashMap.put(monster.getId(), monster);
        MonsterRepository.monsterUUIDSet.add(monster.getId());
    }

    public EntityType getMonsterByName(String mobName) {

        EntityType type;
        try {
            type = EntityType.valueOf(mobName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MonsterNotFoundException(mobName);
        }

        if (!type.isSpawnable() || !type.isAlive()) {
            throw new MonsterNotFoundException(mobName);
        }

        return type;
    }


    public Location getMonsterSpawnLocationInArea(Area area) {
        World world = Bukkit.getWorld(area.getWorldName());
        if (world == null || !area.isComplete()) return null;

        double minX = Math.min(area.getLocation1()[0], area.getLocation2()[0]);
        double maxX = Math.max(area.getLocation1()[0], area.getLocation2()[0]);

        double minY = Math.min(area.getLocation1()[1], area.getLocation2()[1]);
        double maxY = Math.max(area.getLocation1()[1], area.getLocation2()[1]);

        double minZ = Math.min(area.getLocation1()[2], area.getLocation2()[2]);
        double maxZ = Math.max(area.getLocation1()[2], area.getLocation2()[2]);

        Random random = new Random();
        int attempts = 0;
        int maxAttempts = 1000;

        while (attempts++ < maxAttempts) {
            int x = (int) (minX + random.nextInt((int) (maxX - minX - 1)));
            int z = (int) (minZ + random.nextInt((int) (maxZ - minZ - 1)));
            int initialY = (int) (minY + random.nextInt((int) (maxY - minY - 3)));

            // Y 아래로 내려가며 가장 가까운 solid 발판 찾기
            int baseY = -1;
            for (int y = initialY; y >= minY + 1; y--) {
                Block centerBlock = world.getBlockAt(x + 1, y - 1, z + 1); // 중심 위치 바닥
                if (centerBlock.getType().isSolid()) {
                    baseY = y;
                    break;
                }
            }

            if (baseY == -1) continue;

            // 3x3 발판 체크
            boolean hasSolidFloor = true;
            for (int dx = 0; dx < 3; dx++) {
                for (int dz = 0; dz < 3; dz++) {
                    Block block = world.getBlockAt(x + dx, baseY - 1, z + dz);
                    if (!block.getType().isSolid()) {
                        hasSolidFloor = false;
                        break;
                    }
                }
                if (!hasSolidFloor) break;
            }
            if (!hasSolidFloor) continue;

            // 위 3x3 공간 비어 있는지 확인
            boolean isEmpty = true;
            for (int dx = 0; dx < 3; dx++) {
                for (int dz = 0; dz < 3; dz++) {
                    for (int dy = 0; dy < 3; dy++) {
                        Block block = world.getBlockAt(x + dx, baseY + dy, z + dz);
                        if (!block.isEmpty()) {
                            isEmpty = false;
                            break;
                        }
                    }
                    if (!isEmpty) break;
                }
                if (!isEmpty) break;
            }

            if (isEmpty) {
                return new Location(world, x + 1.5, baseY, z + 1.5); // 중앙 보정
            }
        }

        return null; // 실패 시
    }

    public List<List<Monster>> getSpawnMonsters(int round) {
        int roundScore = getRoundScore(round);

        List<List<Monster>> spawnMonsters = new ArrayList<>();

        boolean isScoreRemained = true;
        while (isScoreRemained && roundScore > 0) {
            List<Monster> monsters = new ArrayList<>();
            for (int i = 0; i < PluginRepository.pluginConfig.getOneTimeMobSpawnNumber(); i++) {
                Monster monster = calculateRoundMonster(round, roundScore);
                if(monster == null) {
                    isScoreRemained = false;
                    break;
                }
                monsters.add(monster);
                roundScore -= monster.getScore();
            }
            if(!monsters.isEmpty()) spawnMonsters.add(monsters);
        }

        return spawnMonsters;
    }

    private Monster calculateRoundMonster(int round, int score) {
        Set<Monster> spawnableMonstersByRound = getSpawnableMonstersByRound(round);

        Set<Monster> spawnableMonstersByScore = new HashSet<>();
        for (Monster monster : spawnableMonstersByRound) {
            if(monster.getScore() <= score && monster.getScore() > 0) spawnableMonstersByScore.add(monster);
        }

        if(spawnableMonstersByScore.isEmpty()) return null;

        int monsterTotalScore = 0;
        for (Monster monster : spawnableMonstersByScore) {
            monsterTotalScore += monster.getScore();
        }

        HashMap<Monster, Integer> monsterMinPercent = new HashMap<>();
        HashMap<Monster, Integer> monsterMaxPercent = new HashMap<>();

        int percentInt = 0;
        int total = 10000;
        int count = 0;
        for (Monster monster : spawnableMonstersByScore) {
            monsterMinPercent.put(monster, percentInt + 1);
            int max = ++count == spawnableMonstersByScore.size() ? total : monster.getScore() * total / monsterTotalScore + percentInt;
            percentInt = max;
            monsterMaxPercent.put(monster, max);
        }

        for (int attempt = 0; attempt < 100; attempt++) {
            int randomInt = rand.nextInt(10000) + 1;
            for (Monster monster : spawnableMonstersByScore) {
                // 확률 값에 포함 되는 몬스터면,
                if(monsterMinPercent.get(monster) <= randomInt && randomInt <= monsterMaxPercent.get(monster)) {
                    return monster;
                }
            }
        }
        return null;
    }


    public int getRoundScore(int round) {
        // 이미 계산된 값이 있으면 반환
        Integer cached = InfinityTowerRepository.infinityTowerRoundScoreHashMap.get(round);
        if (cached != null) return cached;

        int baseScore = PluginRepository.pluginConfig.getRoundScore();
        int totalScore = 0;

        for (int i = 1; i <= round; i++) {
            Integer score = InfinityTowerRepository.infinityTowerRoundScoreHashMap.get(i);
            if (score != null) {
                totalScore = score;
            } else {
                totalScore += baseScore * i;
                InfinityTowerRepository.infinityTowerRoundScoreHashMap.put(i, totalScore);
            }
        }

        return InfinityTowerRepository.infinityTowerRoundScoreHashMap.get(round);
    }

    public Set<Monster> getSpawnableMonstersByRound(int round) {
        Set<Monster> monsters = new HashSet<>();
        for (UUID uuid : MonsterRepository.monsterHashMap.keySet()) {
            Monster monster = MonsterRepository.monsterHashMap.get(uuid);
            if(monster == null) continue;
            if(monster.getRoundList() == null) continue;
            if (monster.getRoundList().contains(round)) monsters.add(monster);
        }
        return monsters;
    }

    public static void printMonsterList(CommandSender sender) {
        if (MonsterRepository.monsterHashMap.isEmpty()) {
            sender.sendMessage("§c⚠ 등록된 몬스터가 없습니다.");
            return;
        }

        sender.sendMessage("§7========== §6[몬스터 목록] §7==========");

        for (UUID uuid : MonsterRepository.monsterHashMap.keySet()) {
            Monster monster = MonsterRepository.monsterHashMap.get(uuid);
            sender.sendMessage("§6▪ 이름: §f" + monster.getName());
            sender.sendMessage("§6▪ UUID: §7" + uuid);
            sender.sendMessage("§6▪ 점수: §f" + monster.getScore());
            sender.sendMessage("§6▪ 타입: " + (monster.isMysticMob() ? "§5MysticMob" : "§f일반 몬스터"));
            sender.sendMessage("§6▪ 등장 라운드: §f" + compressForDisplay(monster.getRoundList()));
            sender.sendMessage("§7----------------------------------");
        }

        sender.sendMessage("§7총 §e" + MonsterRepository.monsterHashMap.size() + "§7마리 등록됨.");
    }

    private static String compressForDisplay(List<Integer> roundList) {
        if (roundList == null || roundList.isEmpty()) return "없음";

        List<String> result = new ArrayList<>();
        Collections.sort(roundList);

        int start = roundList.get(0);
        int prev = start;

        for (int i = 1; i < roundList.size(); i++) {
            int curr = roundList.get(i);
            if (curr != prev + 1) {
                result.add(start == prev ? String.valueOf(start) : start + "..." + prev);
                start = curr;
            }
            prev = curr;
        }

        result.add(start == prev ? String.valueOf(start) : start + "..." + prev);
        return String.join(", ", result);
    }
}
