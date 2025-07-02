package com.popogonry.infinityTowerPlugin.Reward.DataConfig;

import com.popogonry.infinityTowerPlugin.Config;
import org.bukkit.inventory.ItemStack;

public class RewardDataConfig extends Config {
    public RewardDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeRewardData(int round, ItemStack reward) {
        getConfig().set(String.valueOf(round), reward);
        super.store();
    }

    public ItemStack loadRewardData(int round) {
        return (ItemStack) getConfig().get(String.valueOf(round));
    }

    public boolean hasRewardData(int round) {
        return getConfig().contains(String.valueOf(round));
    }

    public void removeRewardData(int round) {
        getConfig().set(String.valueOf(round), null);
        super.store();
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
