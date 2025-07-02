package com.popogonry.infinityTowerPlugin.StorageBox;

import com.popogonry.infinityTowerPlugin.Config;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class StorageBoxDataConfig extends Config {

    public StorageBoxDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeStorageBoxData(StorageBox storageBox) {
        getConfig().set(String.valueOf(storageBox.getOwnerUUID()), storageBox);
        super.store();
    }

    public StorageBox loadStorageBoxData(UUID uuid) {
        return (StorageBox) getConfig().get(String.valueOf(uuid));
    }

    public boolean hasStorageBoxData(UUID uuid) {
        return getConfig().contains(String.valueOf(uuid));
    }

    public void removeStorageBoxData(UUID uuid) {
        getConfig().set(String.valueOf(uuid), null);
        super.store();
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
