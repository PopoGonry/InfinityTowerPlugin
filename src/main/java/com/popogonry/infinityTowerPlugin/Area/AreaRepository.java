package com.popogonry.infinityTowerPlugin.Area;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;

public class AreaRepository {
    public static HashMap<Player, Area> playerAreaHashMap = new HashMap<Player, Area>();
    public static HashMap<Player, Boolean> playerAreaSettingModeHashMap = new HashMap<>();


    public void addAreaData(Area area, Player player) {
        playerAreaHashMap.put(player, area);
    }

    public Area getAreaData(Player player) {
        return playerAreaHashMap.get(player);
    }

    public void removeAreaData(Player player) {
        playerAreaHashMap.remove(player);
    }
}
