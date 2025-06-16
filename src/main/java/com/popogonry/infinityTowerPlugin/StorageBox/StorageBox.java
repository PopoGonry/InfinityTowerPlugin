package com.popogonry.infinityTowerPlugin.StorageBox;

import com.popogonry.infinityTowerPlugin.Area.Area;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StorageBox implements ConfigurationSerializable {

    private UUID ownerUUID;
    private final List<ItemStack> inventory;

    public StorageBox(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
        this.inventory = new ArrayList<>();
    }

    public StorageBox(UUID ownerUUID, List<ItemStack> inventory) {
        this.ownerUUID = ownerUUID;
        this.inventory = inventory;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("ownerUUID", ownerUUID.toString());
        map.put("inventory", inventory);
        return map;
    }

    public static StorageBox deserialize(Map<String, Object> map) {
        UUID ownerUUID = UUID.fromString((String) map.get("ownerUUID"));
        List<ItemStack> inventory = (List<ItemStack>) map.get("inventory");
        return new StorageBox(ownerUUID, inventory);
    }

    public int getInventorySize() {
        return inventory.size();
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public List<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return "StorageBox{" +
                "ownerUUID=" + ownerUUID +
                ", inventory=" + inventory +
                '}';
    }
}

