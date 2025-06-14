package com.popogonry.infinityTowerPlugin.Monster;

import com.popogonry.infinityTowerPlugin.InfinityTower.DataConfig.InfinityTowerDataConfig;
import com.popogonry.infinityTowerPlugin.InfinityTower.DataConfig.InfinityTowerSetDataConfig;
import com.popogonry.infinityTowerPlugin.InfinityTower.InfinityTower;
import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.Monster.DataConfig.MonsterDataConfig;
import com.popogonry.infinityTowerPlugin.Monster.DataConfig.MonsterSetDataConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MonsterRepository {
    private static final String MONSTER_FILE_NAME = "monster.yml";
    private static final String MONSTER_SET_FILE_NAME = "monsterSet.yml";

    private final String configBasePath;
    private final MonsterDataConfig dataConfig;
    private final MonsterSetDataConfig setDataConfig;

    public static final HashMap<UUID, Monster> monsterHashMap = new HashMap<UUID, Monster>();
    public static final Set<UUID> monsterUUIDSet = new HashSet<>();

    public MonsterRepository() {
        this.configBasePath = InfinityTowerPlugin.getServerInstance().getDataFolder().getAbsolutePath();
        this.dataConfig = new MonsterDataConfig(configBasePath, MONSTER_FILE_NAME);
        this.setDataConfig = new MonsterSetDataConfig(configBasePath, MONSTER_SET_FILE_NAME);
    }

    public void reloadConfig() {
        dataConfig.reload();
        setDataConfig.reload();
    }

    public void saveConfig() {
        dataConfig.store();
        setDataConfig.store();
    }

    public boolean hasMonster(UUID monsterId) {
        return dataConfig.hasMonsterData(monsterId);
    }

    public void storeMonster(UUID monsterId) {
        dataConfig.storeMonsterData(monsterHashMap.get(monsterId));
        monsterHashMap.remove(monsterId);
    }

    public void saveMonster(UUID monsterId) {
        dataConfig.storeMonsterData(monsterHashMap.get(monsterId));
    }

    public void loadMonster(UUID monsterId) {
        monsterHashMap.put(monsterId, dataConfig.loadMonsterData(monsterId));
    }

    public void removeMonster(UUID monsterId) {
        dataConfig.removeMonsterData(monsterId);
    }

    public void storeMonsterSet() {
        setDataConfig.storeMonsterSet(monsterUUIDSet);
        monsterUUIDSet.clear();
    }

    public void saveMonsterSet() {
        setDataConfig.storeMonsterSet(monsterUUIDSet);
    }

    public void loadMonsterSet() {
        Set<UUID> set = setDataConfig.loadMonsterSet();
        monsterUUIDSet.clear();
        monsterUUIDSet.addAll(set);
    }

    public void removeMonsterSet() {
        setDataConfig.removeMonsterSet();
    }

    public void storeAllMonster() {
        for (UUID uuid : monsterHashMap.keySet()) {
            storeMonster(uuid);
        }
        storeMonsterSet();
    }

    public void saveAllMonster() {
        for (UUID uuid : monsterHashMap.keySet()) {
            saveMonster(uuid);
        }
        saveMonsterSet();
    }

    public void loadAllMonster() {
        loadMonsterSet();
        for (UUID uuid : monsterUUIDSet) {
            loadMonster(uuid);
        }
    }
}
