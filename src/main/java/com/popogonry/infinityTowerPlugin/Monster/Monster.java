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
        map.put("roundList", compress(roundList));
        return map;
    }

    public static Monster deserialize(Map<String, Object> map) {
        UUID id = UUID.fromString((String) map.get("id"));
        String name = (String) map.get("name");
        boolean isMysticMob = (boolean) map.get("isMysticMob");
        int score = (int) map.get("score");

        List<Integer> roundList = decompress(map.get("roundList")); // ✔ 안전하게 처리

        return new Monster(id, name, isMysticMob, score, roundList);
    }

    private static List<String> compress(List<Integer> numbers) {
        Collections.sort(numbers);
        List<String> result = new ArrayList<>();

        int start = numbers.get(0);
        int prev = start;

        for (int i = 1; i < numbers.size(); i++) {
            int current = numbers.get(i);
            if (current != prev + 1) {
                result.add(start == prev ? String.valueOf(start) : start + "..." + prev);
                start = current;
            }
            prev = current;
        }

        result.add(start == prev ? String.valueOf(start) : start + "..." + prev);
        return result;
    }

    public static List<Integer> decompress(Object listObj) {
        List<Integer> result = new ArrayList<>();

        for (Object obj : (List<?>) listObj) {
            String s = String.valueOf(obj); // ← 여기서 Integer, String 구분 없이 처리
            if (s.contains("...")) {
                String[] parts = s.split("\\.\\.\\.");
                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);
                for (int i = start; i <= end; i++) {
                    result.add(i);
                }
            } else {
                result.add(Integer.parseInt(s));
            }
        }

        return result;
    }


    @Override
    public String toString() {
        return "Monster{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isMysticMob=" + isMysticMob +
                ", score=" + score +
                ", roundList=" + roundList +
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
