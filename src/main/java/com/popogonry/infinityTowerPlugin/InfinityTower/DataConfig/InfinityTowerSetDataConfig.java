package com.popogonry.infinityTowerPlugin.InfinityTower.DataConfig;

import com.popogonry.infinityTowerPlugin.Config;

import java.util.*;

public class InfinityTowerSetDataConfig extends Config {

    private final String saveName = "infinityTowerSet";

    public InfinityTowerSetDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeInfinityTowerSet(Set<UUID> infinityTowerSet) {
        List<String> list = new ArrayList<>();
        for (UUID uuid : infinityTowerSet) {
            list.add(uuid.toString());
        }
        getConfig().set(saveName, list);
        super.store();
    }

    public Set<UUID> loadInfinityTowerSet() {
        List<String> list = getConfig().getStringList(saveName);
        Set<UUID> set = new HashSet<>();
        for (String uuidString : list) {
            try {
                set.add(UUID.fromString(uuidString));
            }
            catch (Exception e) {}
        }
        return set;
    }

    public boolean hasInfinityTowerSet() {
        return getConfig().contains(saveName);
    }

    public void removeInfinityTowerSet() {
        getConfig().set(saveName, null);
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
