package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.InfinityTower.DataConfig.InfinityTowerDataConfig;
import com.popogonry.infinityTowerPlugin.InfinityTower.DataConfig.InfinityTowerSetDataConfig;
import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import org.bukkit.entity.Player;

import java.util.*;

public class InfinityTowerRepository {
    private static final String INFINITY_TOWER_FILE_NAME = "infinityTower.yml";
    private static final String INFINITY_TOWER_SET_FILE_NAME = "infinityTowerSet.yml";

    private final String configBasePath;
    private final InfinityTowerDataConfig dataConfig;
    private final InfinityTowerSetDataConfig setDataConfig;


    public static final HashMap<UUID, InfinityTower> infinityTowerHashMap = new HashMap<UUID, InfinityTower>();
    public static final Set<UUID> infinityTowerUUIDSet = new HashSet<>();

    public static final HashMap<Integer, Integer> infinityTowerRoundScoreHashMap = new HashMap<>();

    public static final HashMap<Player, InfinityTowerProcess> infinityTowerPlayerHashMap = new HashMap<>();

    public InfinityTowerRepository() {
        this.configBasePath = InfinityTowerPlugin.getServerInstance().getDataFolder().getAbsolutePath();
        this.dataConfig = new InfinityTowerDataConfig(configBasePath, INFINITY_TOWER_FILE_NAME);
        this.setDataConfig = new InfinityTowerSetDataConfig(configBasePath, INFINITY_TOWER_SET_FILE_NAME);
    }

    public void reloadConfig() {
        dataConfig.reload();
        setDataConfig.reload();
    }

    public void saveConfig() {
        dataConfig.store();
        setDataConfig.store();
    }

    public boolean hasInfinityTower(UUID infinityTowerId) {
        return dataConfig.hasInfinityTowerData(infinityTowerId);
    }
    public void storeInfinityTower(UUID infinityTowerId) {
        dataConfig.storeInfinityTowerData(infinityTowerHashMap.get(infinityTowerId));
        infinityTowerHashMap.remove(infinityTowerId);
    }
    public void saveInfinityTower(UUID infinityTowerId) {
        dataConfig.storeInfinityTowerData(infinityTowerHashMap.get(infinityTowerId));
    }
    public void loadInfinityTower(UUID infinityTowerId) {
        infinityTowerHashMap.put(infinityTowerId, dataConfig.loadInfinityTowerData(infinityTowerId));
    }
    public void removeInfinityTower(UUID infinityTowerId) {
        dataConfig.removeInfinityTowerData(infinityTowerId);
    }

    public void storeInfinityTowerSet() {
        setDataConfig.storeInfinityTowerSet(infinityTowerUUIDSet);
        infinityTowerUUIDSet.clear();
    }
    public void saveInfinityTowerSet() {
        setDataConfig.storeInfinityTowerSet(infinityTowerUUIDSet);
    }

    public void loadInfinityTowerSet() {
        Set<UUID> set = setDataConfig.loadInfinityTowerSet();
        infinityTowerUUIDSet.clear();
        infinityTowerUUIDSet.addAll(set);
    }
    public void removeInfinityTowerSet() {
        setDataConfig.removeInfinityTowerSet();
    }

    public void storeAllInfinityTower() {
        Set<UUID> uuids = new HashSet<>(infinityTowerHashMap.keySet());
        for (UUID uuid : uuids) {
            storeInfinityTower(uuid);
        }
        storeInfinityTowerSet();
    }

    public void saveAllInfinityTower() {
        for (UUID uuid : infinityTowerHashMap.keySet()) {
            saveInfinityTower(uuid);
        }
        saveInfinityTowerSet();
    }

    public void loadAllInfinityTower() {
        loadInfinityTowerSet();
        for (UUID uuid : infinityTowerUUIDSet) {
            loadInfinityTower(uuid);
        }
    }




}
