package com.popogonry.infinityTowerPlugin.Monster.DataConfig;

import com.popogonry.infinityTowerPlugin.Config;

import java.util.*;

public class MonsterSetDataConfig extends Config {
    public MonsterSetDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    private final String saveName = "monsterSet";

    public void storeMonsterSet(Set<UUID> monsterSet) {
        List<String> list = new ArrayList<>();
        for (UUID uuid : monsterSet) {
            list.add(uuid.toString());
        }
        getConfig().set(saveName, list);
        super.store();
    }

    public Set<UUID> loadMonsterSet() {
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

    public boolean hasMonsterSet() {
        return getConfig().contains(saveName);
    }

    public void removeMonsterSet() {
        getConfig().set(saveName, null);
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
