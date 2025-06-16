package com.popogonry.infinityTowerPlugin.StorageBox;

import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.Monster.DataConfig.MonsterDataConfig;
import com.popogonry.infinityTowerPlugin.Monster.DataConfig.MonsterSetDataConfig;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class StorageBoxRepository {

    private static final String STORAGEBOX_FILE_NAME = "userStorageBox.yml";

    private final String configBasePath;
    private final StorageBoxDataConfig dataConfig;

    private final static HashMap<Player, StorageBox> useruserStorageBoxHashMap = new HashMap<>();

    public StorageBoxRepository() {
        configBasePath = InfinityTowerPlugin.getServerInstance().getDataFolder().getAbsolutePath();
        dataConfig = new StorageBoxDataConfig(configBasePath, STORAGEBOX_FILE_NAME);
    }

    public void reloadConfig() {
        dataConfig.reload();
    }

    public void saveConfig() {
        dataConfig.store();
    }

    public boolean hasStorageBox(Player player) {
        return dataConfig.hasStorageBoxData(player);
    }

    public void storeStorageBox(Player player) {
        dataConfig.storeStorageBoxData(useruserStorageBoxHashMap.get(player));
        useruserStorageBoxHashMap.remove(player);
    }

    public void saveStorageBox(Player player) {
        dataConfig.storeStorageBoxData(useruserStorageBoxHashMap.get(player));
    }

    public void loadStorageBox(Player player) {
        useruserStorageBoxHashMap.put(player, dataConfig.loadStorageBoxData(player));
    }

    public void removeStorageBox(Player player) {
        dataConfig.removeStorageBoxData(player);
    }

}
