package com.popogonry.infinityTowerPlugin.StorageBox;

import com.popogonry.infinityTowerPlugin.Area.Area;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StorageBox implements ConfigurationSerializable {

    private Player owner;
    private final List<ItemStack> inventory;

    public StorageBox(Player owner) {
        this.owner = owner;
        inventory = new ArrayList<>();
    }

    public StorageBox(Player owner, List<ItemStack> inventory) {
        this.owner = owner;
        this.inventory = inventory;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("inventory", inventory);
        return map;
    }

    public static StorageBox deserialize(Map<String, Object> map) {
        Player owner = Bukkit.getPlayer((UUID) map.get("owner"));
        List<ItemStack> inventory = (List<ItemStack>) map.get("inventory");

        return new StorageBox(owner, inventory);
    }

    public int getInventorySize() {
        return inventory.size();
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public List<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return "StorageBox{" +
                "owner=" + owner +
                ", inventory=" + inventory +
                '}';
    }
}
