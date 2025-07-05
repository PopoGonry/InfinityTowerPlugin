package com.popogonry.infinityTowerPlugin.TextDisplayHologram;

import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class TextDisplayHologramUtil {

    /**
     * 여러 줄 텍스트를 하나의 TextDisplay로 생성합니다.
     * @param location 텍스트가 표시될 위치
     * @param lines 출력할 문자열 줄 목록
     * @return 생성된 TextDisplay 객체
     */
    public static TextDisplay spawnMultilineHologram(String name, Location location, List<String> lines) {


        World world = location.getWorld();
        if (world == null) return null;

        String text = String.join("\n", lines);

        TextDisplay display = world.spawn(location, TextDisplay.class, t -> {
            t.setText(text);
            t.setBillboard(Display.Billboard.CENTER); // 항상 카메라 정면
            t.setSeeThrough(true);
            t.setShadowed(false);
            t.setLineWidth(200); // 줄바꿈 기준 너비 (픽셀)
            t.setTransformation(new Transformation(
                    new Vector3f(0, 0, 0),          // translation
                    new Quaternionf(),              // leftRotation (identity)
                    new Vector3f(1, 1, 1),          // scale
                    new Quaternionf()               // rightRotation (identity)
            ));
        });


        return display;
    }
}
