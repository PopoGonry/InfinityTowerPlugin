package com.popogonry.infinityTowerPlugin.StorageBox;

import com.popogonry.infinityTowerPlugin.Config;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StorageBoxDataConfig extends Config {

    public StorageBoxDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeStorageBoxData(StorageBox storageBox) {
        getConfig().set(String.valueOf(storageBox.getOwner().getUniqueId()), storageBox);
        super.store();
    }

    public StorageBox loadStorageBoxData(Player player) {
        return (StorageBox) getConfig().get(String.valueOf(player.getUniqueId()));
    }

    public boolean hasStorageBoxData(Player player) {
        return getConfig().contains(String.valueOf(player.getUniqueId()));
    }

    public void removeStorageBoxData(Player player) {
        getConfig().set(String.valueOf(player.getUniqueId()), null);
        super.store();
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
