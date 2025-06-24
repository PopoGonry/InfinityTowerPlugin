package com.popogonry.infinityTowerPlugin.ScoreHologram;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.security.auth.login.Configuration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreHologram implements ConfigurationSerializable {
    private String name;
    private double[] location;

    public ScoreHologram(String name, double[] location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        // 배열을 리스트로 변환해서 저장
        map.put("location", Arrays.asList(location[0], location[1], location[2]));
        return map;
    }

    public static ScoreHologram deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        List<?> locList = (List<?>) map.get("location");

        double[] location = new double[3];
        for (int i = 0; i < 3 && i < locList.size(); i++) {
            location[i] = ((Number) locList.get(i)).doubleValue();
        }

        return new ScoreHologram(name, location);
    }
}
