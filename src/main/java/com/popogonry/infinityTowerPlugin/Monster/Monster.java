package com.popogonry.infinityTowerPlugin.Monster;

import com.popogonry.infinityTowerPlugin.Area.Area;
import com.popogonry.infinityTowerPlugin.InfinityTower.InfinityTower;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Monster implements ConfigurationSerializable {
    private UUID id;
    private String name;
    private boolean isMysticMob;
    private int score;
    private List<Integer> roundList = new ArrayList<>();

    public Monster(UUID id, String name, boolean isMysticMob, int score, List<Integer> roundList) {
        this.id = id;
        this.name = name;
        this.isMysticMob = isMysticMob;
        this.score = score;
        this.roundList = roundList;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id.toString());
        map.put("name", name);
        map.put("isMysticMob", isMysticMob);
        map.put("score", score);
        map.put("roundList", roundList);
        return map;
    }

    public static Monster deserialize(Map<String, Object> map) {
        UUID id = UUID.fromString((String) map.get("id"));
        String name = (String) map.get("name");
        boolean isMysticMob = (boolean) map.get("isMysticMob");
        int score = (int) map.get("score");

        List<Integer> roundList = (List<Integer>) map.get("roundList");

        return new Monster(id, name, isMysticMob, score, roundList);
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

    public boolean isMysticMob() {
        return isMysticMob;
    }

    public void setMysticMob(boolean mysticMob) {
        isMysticMob = mysticMob;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Integer> getRoundList() {
        return roundList;
    }

    public void setRoundList(List<Integer> roundList) {
        this.roundList = roundList;
    }


}
