package com.popogonry.infinityTowerPlugin.Monster;

import com.popogonry.infinityTowerPlugin.InfinityTower.InfinityTowerRepository;
import com.popogonry.infinityTowerPlugin.PluginRepository;

import java.util.*;

public class MonsterService {

    public List<Monster> calculateRoundMonster(int round) {

        Set<Monster> spawnableMonstersByRound = getSpawnableMonstersByRound(round);


        int monsterTotalScore = 0;
        for (Monster monster : spawnableMonstersByRound) {
            monsterTotalScore += monster.getScore();
        }

        int roundScore = getRoundScore(round);

        HashMap<Monster, Integer> monsterMinPercent = new HashMap<>();
        HashMap<Monster, Integer> monsterMaxPercent = new HashMap<>();

        int percentInt = 0;
        for (Monster monster : spawnableMonstersByRound) {
            monsterMinPercent.put(monster, percentInt + 1);
            int max = monster.getScore() * 10000 / monsterTotalScore + percentInt;
            percentInt = max;
            monsterMaxPercent.put(monster, max);
        }

        for (Monster monster : spawnableMonstersByRound) {
            System.out.println(monsterMinPercent.get(monster) + " " + monsterMaxPercent.get(monster));
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
