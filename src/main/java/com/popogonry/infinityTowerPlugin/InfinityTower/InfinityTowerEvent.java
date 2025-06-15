package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.CooldownManager;
import com.popogonry.infinityTowerPlugin.PluginRepository;
import com.popogonry.infinityTowerPlugin.Reference;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InfinityTowerEvent implements Listener {

    InfinityTowerService infinityTowerService = new InfinityTowerService();

    @EventHandler
    public void onPlayerUseCoupon(PlayerInteractEvent event) {
        if(event.getPlayer().getItemInHand().getType() == Material.AIR) return;

        if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(infinityTowerService.getInfinityTowerTicket().getItemMeta().getDisplayName())) {
            if(event.getAction().isRightClick()) {

                CooldownManager cooldownManager = new CooldownManager();
                if (cooldownManager.isOnCooldown(event.getPlayer(), 50L)) {
                    return;
                }

                CooldownManager.cooldowns.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());

                try {
                    infinityTowerService.enterInfinityTower(infinityTowerService.getEnterableTowerUUID(), event.getPlayer());
                    event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);

                } catch (Exception e) {
                    event.getPlayer().sendMessage(Reference.prefix_error + e.getMessage());
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

}
