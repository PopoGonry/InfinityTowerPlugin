package com.popogonry.infinityTowerPlugin.TextDisplayHologram;

import com.popogonry.infinityTowerPlugin.Ranking.RankingService;
import com.popogonry.infinityTowerPlugin.TextDisplayHologram.Exception.NameAlreadyExistsException;
import com.popogonry.infinityTowerPlugin.TextDisplayHologram.Exception.NameNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextDisplayHologramService {

    public boolean createHologram(String name, Location location, String type) {
        if (TextDisplayHologramRepository.textDisplayHologramHashMap.containsKey(name)) {
            throw new NameAlreadyExistsException(name);
        }

        RankingService rankingService = new RankingService();
        TextDisplay textDisplay = TextDisplayHologramUtil.spawnMultilineHologram(name, location, rankingService.getRankingHologramComponents(type));

        TextDisplayHologramRepository.textDisplayHologramHashMap.put(name, textDisplay);

        TextDisplayHologramRepository.textDisplayHologramLocationHashMap
                .computeIfAbsent(type, k -> new HashMap<>())
                .put(name, new TextDisplayHologramLocation(
                        location.getWorld().getName(),
                        new double[]{location.getX(), location.getY(), location.getZ()}
                ));

        return true;
    }

    public boolean removeHologram(String name) {
        if(!TextDisplayHologramRepository.textDisplayHologramHashMap.containsKey(name)) throw new NameNotFoundException(name);

        TextDisplayHologramRepository.textDisplayHologramHashMap.get(name).remove();
        TextDisplayHologramRepository.textDisplayHologramHashMap.remove(name);
        for (HashMap<String, TextDisplayHologramLocation> value : TextDisplayHologramRepository.textDisplayHologramLocationHashMap.values()) {
            value.remove(name);
        }
        return true;
    }

    public void updateHologram() {
        clearHologram();
        settingHologram();
    }

    public void settingHologram() {
        HashMap<String, HashMap<String, TextDisplayHologramLocation>> locationMap = TextDisplayHologramRepository.textDisplayHologramLocationHashMap;
        RankingService rankingService = new RankingService();

        for (String type : locationMap.keySet()) {
            HashMap<String, TextDisplayHologramLocation> typeMap = locationMap.get(type);

            for (String name : typeMap.keySet()) {
                TextDisplayHologramLocation holoLoc = typeMap.get(name);

                // world 존재 확인
                World world = Bukkit.getWorld(holoLoc.getWorldName());
                if (world == null) {
                    Bukkit.getLogger().warning("[InfinityTower] 월드 '" + holoLoc.getWorldName() + "' 를 찾을 수 없습니다. 홀로그램 '" + name + "' 생성을 건너뜁니다.");
                    continue;
                }

                double[] coords = holoLoc.getLocation();
                if (coords.length < 3) {
                    Bukkit.getLogger().warning("[InfinityTower] 위치 데이터가 손상되었습니다. '" + name + "' 생성을 건너뜁니다.");
                    continue;
                }

                Location location = new Location(world, coords[0], coords[1], coords[2]);
                TextDisplay textDisplay = TextDisplayHologramUtil.spawnMultilineHologram(name, location, rankingService.getRankingHologramComponents(type));

                TextDisplayHologramRepository.textDisplayHologramHashMap.put(name, textDisplay);
            }
        }
    }

    public void clearHologram() {
        for (TextDisplay textDisplay : TextDisplayHologramRepository.textDisplayHologramHashMap.values()) {
            if (textDisplay != null && !textDisplay.isDead()) {
                textDisplay.remove();
            }
        }

        TextDisplayHologramRepository.textDisplayHologramHashMap.clear();
    }

    public static void sendTextDisplayHologramLocationMap(CommandSender sender) {
        sender.sendMessage("§7======= §e📊 랭킹 홀로그램 위치 정보 §7=======");

        for (String type : List.of("daily", "weekly", "monthly")) {
            HashMap<String, TextDisplayHologramLocation> nameMap = TextDisplayHologramRepository.textDisplayHologramLocationHashMap.get(type);

            sender.sendMessage("§6■ 랭킹 타입: §f" + type.toUpperCase());

            if (nameMap == null || nameMap.isEmpty()) {
                sender.sendMessage("  §8(데이터 없음)");
                continue;
            }

            for (Map.Entry<String, TextDisplayHologramLocation> entry : nameMap.entrySet()) {
                String name = entry.getKey();
                TextDisplayHologramLocation loc = entry.getValue();
                double[] coords = loc.getLocation();

                sender.sendMessage("  §e● 이름: §f" + name);
                sender.sendMessage("    §7↳ 월드: §f" + loc.getWorldName());

                String coordText = String.format("X=%.2f, Y=%.2f, Z=%.2f", coords[0], coords[1], coords[2]);

                if (sender instanceof Player player) {
                    TextComponent clickable = new TextComponent("    §7↳ 좌표: §f" + coordText);
                    clickable.setClickEvent(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            String.format("/tp %s %.2f %.2f %.2f", player.getName(), coords[0], coords[1], coords[2])
                    ));
                    player.spigot().sendMessage(clickable);
                } else {
                    sender.sendMessage("    §7↳ 좌표: §f" + coordText);
                }
            }
        }

        sender.sendMessage("§7=====================================");
    }


}
