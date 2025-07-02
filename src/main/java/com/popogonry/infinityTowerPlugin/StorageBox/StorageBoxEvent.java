package com.popogonry.infinityTowerPlugin.StorageBox;

import com.popogonry.infinityTowerPlugin.PluginRepository;
import com.popogonry.infinityTowerPlugin.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class StorageBoxEvent implements Listener {

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        StorageBoxRepository storageBoxRepository = new StorageBoxRepository();
        if(!storageBoxRepository.hasStorageBox(event.getPlayer().getUniqueId())) {
            StorageBoxRepository.userStorageBoxHashMap.put(event.getPlayer().getUniqueId(), new StorageBox(event.getPlayer().getUniqueId()));
        }
        else {
            storageBoxRepository.loadStorageBox(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent event) {
        StorageBoxRepository storageBoxRepository = new StorageBoxRepository();
        storageBoxRepository.storeStorageBox(event.getPlayer().getUniqueId());
    }


    @EventHandler
    public static void onClickStorageBoxGUI(InventoryClickEvent event) {
        if(event.getView().getTitle().equalsIgnoreCase(PluginRepository.pluginConfig.getRewardGUIDisplayName())
                && event.getCurrentItem() != null
                && event.getCurrentItem().getType() != Material.AIR) {

            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            Inventory inventory = event.getInventory();

            int slot = event.getRawSlot();

            StorageBoxGUI storageBoxGUI = new StorageBoxGUI();

            String[] strings1 = inventory.getItem(49).getItemMeta().getDisplayName().split("/");
            String[] strings2 = strings1[0].split(" ");
            int page = Integer.parseInt(strings2[1].replaceAll(" ", ""));

            // Item List
            if(0 <= slot && slot <= 44) {
                List<ItemStack> storageInventory = StorageBoxRepository.userStorageBoxHashMap.get(player.getUniqueId()).getInventory();
                if(event.getClick().isLeftClick()) {
                    for(int i = 0; i < 36; i++) {
                        ItemStack item2 = player.getInventory().getItem(i);
                        if (item2 == null || item2.getType() == Material.AIR) {
                            player.getInventory().addItem(storageInventory.get(slot + (45*(page-1))));
                            storageInventory.remove(slot + (45*(page-1)));
                            player.sendMessage(Reference.prefix_normal + "지급 되었습니다.");
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                            storageBoxGUI.openStorageBoxGUI(player, page);
                            return;
                        }
                    }
                    player.sendMessage(Reference.prefix_error + "인벤토리에 공간이 부족합니다.");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }
            }

            else if(48 <= slot && slot <= 50) {
                ItemStack itemStack = inventory.getItem(slot);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if(itemMeta.getDisplayName().contains("To")) {
                    String[] strings = itemMeta.getDisplayName().split(" ");
                    storageBoxGUI.openStorageBoxGUI(player, Integer.parseInt(strings[1]));
                }
            }

            // Player Inventory
            else if(54 <= slot && slot <= 89) {

            }

        }
    }

    private static boolean canGiveItems(Player player, ItemStack item) {

        // addItem은 못 들어간 걸 leftover로 반환
        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(item);

        return leftover.isEmpty();
    }


}
