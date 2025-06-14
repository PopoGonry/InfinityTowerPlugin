package com.popogonry.infinityTowerPlugin.Reward.DataConfig;

import com.popogonry.infinityTowerPlugin.Config;

import java.util.*;

public class RewardDataSetConfig extends Config {

    private final String saveName = "rewardSet";

    public RewardDataSetConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeRewardSet(Set<Integer> rewardSet) {
        List<Integer> list = new ArrayList<>(rewardSet);
        getConfig().set(saveName, list);
        super.store();
    }

    public Set<Integer> loadRewardSet() {
        return new HashSet<>(getConfig().getIntegerList(saveName));
    }

    public boolean hasRewardSet() {
        return getConfig().contains(saveName);
    }

    public void removeRewardSet() {
        getConfig().set(saveName, null);
    }
    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
