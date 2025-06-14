package com.popogonry.infinityTowerPlugin.InfinityTower.DataConfig;

import com.popogonry.infinityTowerPlugin.Config;
import com.popogonry.infinityTowerPlugin.InfinityTower.InfinityTower;

import java.util.UUID;

public class InfinityTowerDataConfig extends Config {
    public InfinityTowerDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeInfinityTowerData(InfinityTower infinityTower) {
        getConfig().set(infinityTower.getId().toString(), infinityTower);
        super.store();
    }

    public InfinityTower loadInfinityTowerData(UUID infinityTowerId) {
        return (InfinityTower) getConfig().get(infinityTowerId.toString());
    }

    public boolean hasInfinityTowerData(UUID infinityTowerId) {
        return getConfig().contains(infinityTowerId.toString());
    }

    public void removeInfinityTowerData(UUID infinityTowerId) {
        getConfig().set(infinityTowerId.toString(), null);
        super.store();
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
