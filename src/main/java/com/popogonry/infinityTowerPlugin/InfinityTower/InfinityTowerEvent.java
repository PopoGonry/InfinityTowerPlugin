package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.CooldownManager;
import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.PluginRepository;
import com.popogonry.infinityTowerPlugin.Reference;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class InfinityTowerEvent implements Listener {

    InfinityTowerService infinityTowerService = new InfinityTowerService();

    @EventHandler
    public void onPlayerUseTicket(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey("infinity_tower", "infinity_tower_ticket");

        if (meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
            if (event.getAction().isRightClick()) {

                CooldownManager cooldownManager = new CooldownManager();
                if (cooldownManager.isOnCooldown(player, 50L)) return;

                CooldownManager.cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

                try {
                    infinityTowerService.enterInfinityTower(infinityTowerService.getEnterableTowerUUID(), player);
                    item.setAmount(item.getAmount() - 1);
                } catch (Exception e) {
                    player.sendMessage(Reference.prefix_error + e.getMessage());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(InfinityTowerRepository.infinityTowerPlayerHashMap.containsKey(event.getPlayer())) {
            InfinityTowerProcess infinityTowerProcess = InfinityTowerRepository.infinityTowerPlayerHashMap.get(event.getPlayer());
            infinityTowerProcess.failRound("플레이어가 게임을 떠났습니다.");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(InfinityTowerRepository.infinityTowerPlayerHashMap.containsKey(event.getPlayer())) {
            InfinityTowerProcess infinityTowerProcess = InfinityTowerRepository.infinityTowerPlayerHashMap.get(event.getPlayer());
            infinityTowerProcess.failRound("플레이어가 사망했습니다.");
            event.setCancelled(true);
            Location playerBeforeLocation = infinityTowerProcess.getPlayerBeforeLocation();

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(playerBeforeLocation != null) {
                        event.getPlayer().teleport(playerBeforeLocation);
                    }
                }
            }.runTaskLater(InfinityTowerPlugin.getServerInstance(), 1L); // 40 ticks = 2 seconds
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if(event.getPlayer().isOp()) return;

        Player player = event.getPlayer();

        // 플레이어가 무한의 탑에 참가 중인지 확인
        if (!InfinityTowerRepository.infinityTowerPlayerHashMap.containsKey(player)) {
            return;
        }

        // 허용된 명령어 리스트 확인
        String message = event.getMessage().toLowerCase(); // /home set 등 포함됨
        for (String allowedCommand : PluginRepository.pluginConfig.getAllowedCommands()) {
            System.out.println(message + " " + allowedCommand);

            if (message.startsWith("/" + allowedCommand.toLowerCase())) {
                return; // 허용된 명령어이면 허용
            }
        }

        // 허용되지 않은 명령어는 취소
        event.setCancelled(true);
        player.sendMessage("§c무한의 탑에서는 해당 명령어를 사용할 수 없습니다.");
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            for (InfinityTower value : InfinityTowerRepository.infinityTowerHashMap.values()) {
                World world = Bukkit.getWorld(value.getArea().getWorldName());
                if (world != null && world.getName().equalsIgnoreCase(event.getLocation().getWorld().getName())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if(killer != null && InfinityTowerRepository.infinityTowerPlayerHashMap.containsKey(killer)) {
            event.getDrops().clear();
        }
    }
}
