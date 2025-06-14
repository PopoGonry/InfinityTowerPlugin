package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.Area.Area;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InfinityTower implements ConfigurationSerializable {
    private UUID id;
    private String name;
    private Area area;
    private double[] spawnLocation;
    private boolean isWorking;

    public InfinityTower(UUID id, String name, Area area, double[] spawnLocation, boolean isWorking) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.spawnLocation = spawnLocation;
        this.isWorking = isWorking;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", id.toString());
        map.put("name", name);
        map.put("area", area);
        map.put("spawnLocation", spawnLocation);
        map.put("isWorking", isWorking);

        return map;
    }

    public static InfinityTower deserialize(Map<String, Object> map) {
        List<Number> spawnList = (List<Number>) map.get("spawnLocation");
        double[] spawnLocation = null;
        if(spawnList != null) {
            spawnLocation = new double[spawnList.size()];
            for (int i = 0; i < spawnList.size(); i++) {
                spawnLocation[i] = spawnList.get(i).doubleValue();
            }
        }
        return new InfinityTower(
                UUID.fromString(map.get("id").toString()),
                (String) map.get("name"),
                (Area) map.get("area"),
                spawnLocation,
                (boolean) map.get("isWorking")
        );
    }

    @Override
    public String toString() {
        return "InfinityTower{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", area=" + area +
                ", spawnLocation=" + Arrays.toString(spawnLocation) +
                ", isWorking=" + isWorking +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public double[] getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(double[] spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }
}
