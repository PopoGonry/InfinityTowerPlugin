package com.popogonry.infinityTowerPlugin.TextDisplayHologram;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class TextDisplayHologramUtil {

    /**
     * 여러 줄 텍스트를 하나의 TextDisplay로 생성합니다.
     * @param name     이름 (현재 미사용, 추후 PDC 등에 활용 가능)
     * @param location 텍스트가 표시될 위치
     * @param lines    출력할 Component 줄 목록
     * @return 생성된 TextDisplay 객체
     */
    public static TextDisplay spawnMultilineHologram(String name, Location location, List<Component> lines) {
        World world = location.getWorld();
        if (world == null) return null;

        // 줄바꿈 포함된 하나의 Component 생성
        TextComponent.Builder builder = Component.text();

        for (int i = 0; i < lines.size(); i++) {
            builder.append(lines.get(i));
            if (i != lines.size() - 1) {
                builder.append(Component.newline());
            }
        }

        TextComponent finalText = builder.build();

        // 하나의 TextDisplay에 텍스트 적용
        TextDisplay display = world.spawn(location, TextDisplay.class, t -> {
            t.text(finalText);
            t.setBillboard(TextDisplay.Billboard.CENTER);
            t.setSeeThrough(true);
            t.setShadowed(false);
            t.setLineWidth(500); // 줄바꿈 기준 너비
            t.setTransformation(new Transformation(
                    new Vector3f(0, 0, 0),
                    new Quaternionf(),
                    new Vector3f(1, 1, 1),
                    new Quaternionf()
            ));
        });

        return display;
    }
}
