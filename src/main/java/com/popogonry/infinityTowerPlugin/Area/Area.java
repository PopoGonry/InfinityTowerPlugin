package com.popogonry.infinityTowerPlugin.Area;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Area implements ConfigurationSerializable {
    private String worldName;
    private double[] location1;
    private double[] location2;

    public Area() {
        this.worldName = "";
        this.location1 = new double[3];
        this.location2 = new double[3];
    }

    public Area(String worldName, double[] location1, double[] location2) {
        this.worldName = worldName;
        this.location1 = location1;
        this.location2 = location2;
    }

    public boolean isComplete() {
        if(worldName.isEmpty() || location1.length != 3 || location2.length != 3) return false;
        return true;
    }

    public boolean isInside(double[] location) {
        if (location == null || location.length != 3) return false;

        for (int i = 0; i < 3; i++) {
            double min = Math.min(location1[i], location2[i]);
            double max = Math.max(location1[i], location2[i]);

            if (location[i] < min || location[i] > max) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("world", worldName);
        map.put("location1", Arrays.asList(location1[0], location1[1], location1[2]));
        map.put("location2", Arrays.asList(location2[0], location2[1], location2[2]));
        return map;
    }

    public static Area deserialize(Map<String, Object> map) {
        String worldName = (String) map.get("world");
        List<?> loc1List = (List<?>) map.get("location1");
        List<?> loc2List = (List<?>) map.get("location2");

        double[] loc1 = new double[3];
        double[] loc2 = new double[3];
        for (int i = 0; i < 3; i++) {
            loc1[i] = ((Number) loc1List.get(i)).doubleValue();
            loc2[i] = ((Number) loc2List.get(i)).doubleValue();
        }

        return new Area(worldName, loc1, loc2);
    }

    @Override
    public String toString() {
        return "Area{" +
                "worldName='" + worldName + '\'' +
                ", location1=" + Arrays.toString(location1) +
                ", location2=" + Arrays.toString(location2) +
                '}';
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public double[] getLocation1() {
        return location1;
    }

    public void setLocation1(double[] location1) {
        this.location1 = location1;
    }

    public double[] getLocation2() {
        return location2;
    }

    public void setLocation2(double[] location2) {
        this.location2 = location2;
    }


}
