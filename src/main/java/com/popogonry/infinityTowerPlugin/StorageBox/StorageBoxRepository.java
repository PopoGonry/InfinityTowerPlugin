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

    public final static HashMap<UUID, StorageBox> userStorageBoxHashMap = new HashMap<>();

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

    public boolean hasStorageBox(UUID uuid) {
        return dataConfig.hasStorageBoxData(uuid);
    }

    public void storeStorageBox(UUID uuid) {
        dataConfig.storeStorageBoxData(userStorageBoxHashMap.getOrDefault(uuid, new StorageBox(uuid)));
        userStorageBoxHashMap.remove(uuid);
    }

    public void saveStorageBox(UUID uuid) {
        dataConfig.storeStorageBoxData(userStorageBoxHashMap.getOrDefault(uuid, new StorageBox(uuid)));
    }

    public void loadStorageBox(UUID uuid) {
        userStorageBoxHashMap.put(uuid, dataConfig.loadStorageBoxData(uuid));
    }

    public void removeStorageBox(UUID uuid) {
        dataConfig.removeStorageBoxData(uuid);
    }




}
