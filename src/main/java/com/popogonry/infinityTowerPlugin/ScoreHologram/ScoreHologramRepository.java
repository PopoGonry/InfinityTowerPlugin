//package com.popogonry.infinityTowerPlugin.ScoreHologram;
//
//import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
//import com.popogonry.infinityTowerPlugin.ScoreHologram.DataConfig.ScoreHologramDataConfig;
//import com.popogonry.infinityTowerPlugin.ScoreHologram.DataConfig.ScoreHologramSetDataConfig;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Set;
//
//public class ScoreHologramRepository {
//    private final String configBasePath;
//    private static final String SCORE_HOLOGRAM_FILE_NAME = "scoreHologram.yml";
//    private static final String SCORE_HOLOGRAM_SET_FILE_NAME = "scoreHologram.yml";
//
//    private final ScoreHologramDataConfig dataConfig;
//    private final ScoreHologramSetDataConfig setDataConfig;
//
//    public ScoreHologramRepository() {
//        this.configBasePath = InfinityTowerPlugin.getServerInstance().getDataFolder().getAbsolutePath();
//        this.dataConfig = new ScoreHologramDataConfig(configBasePath, SCORE_HOLOGRAM_FILE_NAME);
//        this.setDataConfig = new ScoreHologramSetDataConfig(configBasePath, SCORE_HOLOGRAM_SET_FILE_NAME);
//    }
//
//    public static final HashMap<String, ScoreHologram> scoreHologramHashMap = new HashMap<>();
//    public static final Set<String> scoreHologramSet = new HashSet<>();
//
//    public void reloadConfig() {
//        dataConfig.reload();
//        setDataConfig.reload();
//    }
//
//    public void saveConfig() {
//        dataConfig.store();
//        setDataConfig.store();
//    }
//
//    public boolean hasReward(int round) {
//        return dataConfig.hasRewardData(round);
//    }
//    public void storeReward(int round) {
//        dataConfig.storeScoreHologramData(round, scoreHologramHashMap.get(round));
//        scoreHologramHashMap.remove(round);
//    }
//    public void saveReward(int round) {
//        dataConfig.storeScoreHologramData(round, scoreHologramHashMap.get(round));
//    }
//    public void loadReward(int round) {
//        scoreHologramHashMap.put(round, dataConfig.loadRewardData(round));
//    }
//    public void removeReward(int round) {
//        dataConfig.removeRewardData(round);
//    }
//
//    public void storeRewardSet() {
//        setDataConfig.storeRewardSet(scoreHologramSet);
//        scoreHologramSet.clear();
//    }
//    public void saveRewardSet() {
//        setDataConfig.storeRewardSet(scoreHologramSet);
//    }
//
//    public void loadRewardSet() {
//        Set<Integer> set = setDataConfig.loadRewardSet();
//        scoreHologramSet.clear();
//        scoreHologramSet.addAll(set);
//    }
//    public void removeRewardSet() {
//        setDataConfig.removeRewardSet();
//    }
//
//    public void storeAllReward() {
//        Set<Integer> rounds = new HashSet<>(scoreHologramHashMap.keySet());
//        for (int round : rounds) {
//            storeReward(round);
//        }
//        storeRewardSet();
//    }
//
//    public void saveAllReward() {
//        for (int round : scoreHologramHashMap.keySet()) {
//            saveReward(round);
//        }
//        saveRewardSet();
//    }
//
//    public void loadAllReward() {
//        loadRewardSet();
//        for (int round : scoreHologramSet) {
//            loadReward(round);
//        }
//    }
//}
