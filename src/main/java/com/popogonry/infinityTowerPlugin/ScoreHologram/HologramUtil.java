package com.popogonry.infinityTowerPlugin.ScoreHologram;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class HologramUtil {

    public static ArmorStand spawnHologram(Location location, String text) {
        World world = location.getWorld();
        if (world == null) return null;

        ArmorStand stand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);

        stand.setCustomName(text); // 텍스트 설정
        stand.setCustomNameVisible(true); // 항상 표시
        stand.setInvisible(true); // 본체 숨김
        stand.setMarker(true); // 히트박스 제거 (1.8 이상)
        stand.setGravity(false); // 떨어지지 않게

        return stand;
    }

    public static List<ArmorStand> spawnHologramLines(Location baseLocation, List<String> lines) {
        List<ArmorStand> result = new ArrayList<>();
        double lineSpacing = 0.3;

        for (int i = 0; i < lines.size(); i++) {
            Location lineLoc = baseLocation.clone().add(0, -i * lineSpacing, 0);
            ArmorStand stand = spawnHologram(lineLoc, lines.get(i));
            result.add(stand);
        }

        return result;
    }
}
