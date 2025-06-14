package com.popogonry.infinityTowerPlugin.Area;

import com.popogonry.infinityTowerPlugin.CooldownManager;
import com.popogonry.infinityTowerPlugin.Reference;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class AreaEvent implements Listener {
    AreaService areaService = new AreaService();

    @EventHandler
    public void onAreaSettingEvent(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(areaService.getAreaSettingTool().getItemMeta().getDisplayName())
                && AreaRepository.playerAreaSettingModeHashMap.getOrDefault(event.getPlayer(), false)) {

            if(event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.AIR) {
                Player player = event.getPlayer();

                event.setCancelled(true);

                CooldownManager cooldownManager = new CooldownManager();
                if (cooldownManager.isOnCooldown(player, 50L)) {
                    return;
                }

                CooldownManager.cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

                Block block = event.getClickedBlock();

                Area area = AreaRepository.playerAreaHashMap.getOrDefault(player, new Area());

                area.setWorldName(player.getWorld().getName());

                double[] location = new double[3];
                location[0] = block.getX();
                location[1] = block.getY();
                location[2] = block.getZ();

                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    area.setLocation1(location);
                    player.sendMessage(Reference.prefix + "첫번째 좌표: " + location[0] + ", " + location[1] + ", " + location[2]);

                } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    area.setLocation2(location);
                    player.sendMessage(Reference.prefix + "두번째 좌표: " + location[0] + ", " + location[1] + ", " + location[2]);
                }

//                player.sendMessage(area.toString());
            }
        }
    }
}
