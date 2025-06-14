package com.popogonry.infinityTowerPlugin.Monster.DataConfig;

import com.popogonry.infinityTowerPlugin.Config;
import com.popogonry.infinityTowerPlugin.Monster.Monster;

import java.util.UUID;

public class MonsterDataConfig extends Config {
    public MonsterDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeMonsterData(Monster monster) {
        getConfig().set(monster.getId().toString(), monster);
        super.store();
    }

    public Monster loadMonsterData(UUID monsterId) {
        return (Monster) getConfig().get(monsterId.toString());
    }

    public boolean hasMonsterData(UUID monsterId) {
        return getConfig().contains(monsterId.toString());
    }

    public void removeMonsterData(UUID monsterId) {
        getConfig().set(monsterId.toString(), null);
        super.store();
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
