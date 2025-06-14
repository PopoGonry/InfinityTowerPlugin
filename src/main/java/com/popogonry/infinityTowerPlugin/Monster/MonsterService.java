package com.popogonry.infinityTowerPlugin.Monster;

import com.popogonry.infinityTowerPlugin.InfinityTower.InfinityTowerRepository;
import com.popogonry.infinityTowerPlugin.PluginRepository;

import java.util.*;

public class MonsterService {

    private final Random rand = new Random();
    public List<List<Monster>> getSpawnMonsters(int round) {
        int roundScore = getRoundScore(round);

        List<List<Monster>> spawnMonsters = new ArrayList<>();


        int score = 0;


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
                score++;
                roundScore -= monster.getScore();
            }
            if(!monsters.isEmpty()) spawnMonsters.add(monsters);
        }
        System.out.println("총 몬스터 수: " + score);

        return spawnMonsters;
    }

    public Monster calculateRoundMonster(int round, int score) {
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
        if(round == 1) {
            return PluginRepository.pluginConfig.getRoundScore();
        }

        if(!InfinityTowerRepository.infinityTowerRoundScoreHashMap.containsKey(round)) {
            int score = PluginRepository.pluginConfig.getRoundScore() * round + getRoundScore(round - 1);
            InfinityTowerRepository.infinityTowerRoundScoreHashMap.put(round, score);
        }

        return InfinityTowerRepository.infinityTowerRoundScoreHashMap.get(round);
    }

    public Set<Monster> getSpawnableMonstersByRound(int round) {
        Set<Monster> monsters = new HashSet<>();
        for (UUID uuid : MonsterRepository.monsterHashMap.keySet()) {
            Monster monster = MonsterRepository.monsterHashMap.get(uuid);
            if (monster.getRoundList().contains(round)) monsters.add(monster);
        }
        return monsters;
    }
}
