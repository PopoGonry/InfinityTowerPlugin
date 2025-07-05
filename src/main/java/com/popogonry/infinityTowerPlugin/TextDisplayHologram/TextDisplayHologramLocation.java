package com.popogonry.infinityTowerPlugin.TextDisplayHologram;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextDisplayHologramLocation implements ConfigurationSerializable {
    private String worldName;
    private double[] location;

    public TextDisplayHologramLocation(String worldName, double[] location) {
        this.worldName = worldName;
        this.location = location;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("world", worldName);
        map.put("location", Arrays.asList(location[0], location[1], location[2])); // List<Double>
        return map;
    }

    public static TextDisplayHologramLocation deserialize(Map<String, Object> map) {
        String world = (String) map.get("world");
        List<?> locList = (List<?>) map.get("location");
        double[] loc = new double[3];

        for (int i = 0; i < Math.min(3, locList.size()); i++) {
            Object val = locList.get(i);
            if (val instanceof Number num) {
                loc[i] = num.doubleValue();
            }
        }

        return new TextDisplayHologramLocation(world, loc);
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "TextDisplayHologramLocation{" +
                "worldName='" + worldName + '\'' +
                ", location=" + Arrays.toString(location) +
                '}';
    }
}
