package com.popogonry.infinityTowerPlugin.StorageBox;

import com.popogonry.infinityTowerPlugin.GUI;
import com.popogonry.infinityTowerPlugin.Reference;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StorageBoxGUI {
    public boolean openStorageBoxGUI(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(player, 54, Reference.prefix_normal + "Rewards");

        StorageBox storageBox = StorageBoxRepository.userStorageBoxHashMap.get(player.getUniqueId());
        List<ItemStack> itemList = new ArrayList<>(storageBox.getInventory());


        for (int i = 0 + (45*(page-1)); i < 45 + (45*(page-1)) && i < itemList.size(); i++) {
            inventory.addItem(itemList.get(i));
        }


        // 48 49 50
        int maxPage = itemList.size() / 45;
        maxPage += itemList.size() % 45 == 0 ? 0 : 1;

        inventory.setItem(49, GUI.getCustomItemStack(Material.EMERALD, Reference.prefix + "Page " + page + " / " + maxPage, Collections.singletonList(ChatColor.GOLD + "Amount of Items: " + itemList.size())));

        if(page > 1) {
            inventory.setItem(48, GUI.getCustomItemStack(Material.PAPER, Reference.prefix + "To " + (page - 1)));
        }

        if(page < maxPage) {
            inventory.setItem(50, GUI.getCustomItemStack(Material.PAPER, Reference.prefix + "To " + (page + 1)));
        }

        player.openInventory(inventory);

        return true;
    }
}
