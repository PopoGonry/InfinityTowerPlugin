package com.popogonry.infinityTowerPlugin.Area;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AreaService {

    public ItemStack getAreaSettingTool() {
        ItemStack itemStack = new ItemStack(Material.BLAZE_ROD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Area Setting Tool");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void onAreaSettingMode(Player player) {
        AreaRepository.playerAreaSettingModeHashMap.put(player, true);
    }

    public void offAreaSettingMode(Player player) {
        AreaRepository.playerAreaSettingModeHashMap.remove(player);
    }

}
