package com.popogonry.infinityTowerPlugin;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public boolean isOnCooldown(Player player, long cooldownTime) {
        long currentTime = System.currentTimeMillis();
        UUID uuid = player.getUniqueId();

        if (!cooldowns.containsKey(uuid)) {
            cooldowns.put(uuid, currentTime);
            return false; // 쿨타임 없음
        }

        long lastUsed = cooldowns.get(uuid);
        if (currentTime - lastUsed >= cooldownTime) {
            cooldowns.put(uuid, currentTime); // 쿨타임 리셋
            return false; // 쿨타임 끝남
        }

        return true; // 아직 쿨타임
    }

    public long getRemainingTime(Player player, long cooldownTime) {
        UUID uuid = player.getUniqueId();
        if (!cooldowns.containsKey(uuid)) return 0;

        long elapsed = System.currentTimeMillis() - cooldowns.get(uuid);
        long remaining = cooldownTime - elapsed;
        return Math.max(0, remaining);
    }
}
