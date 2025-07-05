package com.popogonry.infinityTowerPlugin.TextDisplayHologram;

import com.popogonry.infinityTowerPlugin.Config;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextDisplayHologramDataConfig extends Config {
    private static final String dataKey = "textDisplayHologramLocationHashMap";

    public TextDisplayHologramDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeHologramData(HashMap<String, HashMap<String, TextDisplayHologramLocation>> textDisplayHologramLocationHashMap) {
        getConfig().set(dataKey, textDisplayHologramLocationHashMap);
        super.store();
    }

    public HashMap<String, HashMap<String, TextDisplayHologramLocation>> loadHologramData() {
        HashMap<String, HashMap<String, TextDisplayHologramLocation>> result = new HashMap<>();
        ConfigurationSection outerSection = getConfig().getConfigurationSection(dataKey);
        if (outerSection == null) return result;

        for (String type : outerSection.getKeys(false)) {
            ConfigurationSection typeSection = outerSection.getConfigurationSection(type);
            if (typeSection == null) continue;

            HashMap<String, TextDisplayHologramLocation> innerMap = new HashMap<>();
            for (String name : typeSection.getKeys(false)) {
                TextDisplayHologramLocation location =
                        (TextDisplayHologramLocation) typeSection.get(name);
                if (location != null) {
                    innerMap.put(name, location);
                }
            }

            result.put(type, innerMap);
        }

        return result;
    }

    public boolean hasHologramData() {
        return getConfig().contains(dataKey);
    }

    public void removeHologramData() {
        getConfig().set(dataKey, null);
        super.store();
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
