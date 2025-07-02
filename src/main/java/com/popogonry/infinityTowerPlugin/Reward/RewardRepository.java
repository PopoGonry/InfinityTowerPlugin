package com.popogonry.infinityTowerPlugin.Reward;

import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.Reward.DataConfig.RewardDataConfig;
import com.popogonry.infinityTowerPlugin.Reward.DataConfig.RewardDataSetConfig;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RewardRepository {

    private final String configBasePath;
    private static final String REWARDS_FILE_NAME = "rewards.yml";
    private static final String REWARDS_SET_FILE_NAME = "rewardSet.yml";

    private final RewardDataConfig dataConfig;
    private final RewardDataSetConfig setDataConfig;

    public RewardRepository() {
        this.configBasePath = InfinityTowerPlugin.getServerInstance().getDataFolder().getAbsolutePath();
        this.dataConfig = new RewardDataConfig(configBasePath, REWARDS_FILE_NAME);
        this.setDataConfig = new RewardDataSetConfig(configBasePath, REWARDS_SET_FILE_NAME);
    }

    public static final HashMap<Integer, ItemStack> rewardsHashMap = new HashMap<>();
    public static final Set<Integer> rewardsSet = new HashSet<>();

    public void reloadConfig() {
        dataConfig.reload();
        setDataConfig.reload();
    }

    public void saveConfig() {
        dataConfig.store();
        setDataConfig.store();
    }

    public boolean hasReward(int round) {
        return dataConfig.hasRewardData(round);
    }
    public void storeReward(int round) {
        dataConfig.storeRewardData(round, rewardsHashMap.get(round));
        rewardsHashMap.remove(round);
    }
    public void saveReward(int round) {
        dataConfig.storeRewardData(round, rewardsHashMap.get(round));
    }
    public void loadReward(int round) {
        rewardsHashMap.put(round, dataConfig.loadRewardData(round));
    }
    public void removeReward(int round) {
        dataConfig.removeRewardData(round);
    }

    public void storeRewardSet() {
        setDataConfig.storeRewardSet(rewardsSet);
        rewardsSet.clear();
    }
    public void saveRewardSet() {
        setDataConfig.storeRewardSet(rewardsSet);
    }

    public void loadRewardSet() {
        Set<Integer> set = setDataConfig.loadRewardSet();
        rewardsSet.clear();
        rewardsSet.addAll(set);
    }
    public void removeRewardSet() {
        setDataConfig.removeRewardSet();
    }

    public void storeAllReward() {
        Set<Integer> rounds = new HashSet<>(rewardsHashMap.keySet());
        for (int round : rounds) {
            storeReward(round);
        }
        storeRewardSet();
    }

    public void saveAllReward() {
        for (int round : rewardsHashMap.keySet()) {
            saveReward(round);
        }
        saveRewardSet();
    }

    public void loadAllReward() {
        loadRewardSet();
        for (int round : rewardsSet) {
            loadReward(round);
        }
    }
}
