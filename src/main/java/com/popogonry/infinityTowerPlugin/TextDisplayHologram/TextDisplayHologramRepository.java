package com.popogonry.infinityTowerPlugin.TextDisplayHologram;

import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.Ranking.Ranking;
import org.bukkit.entity.TextDisplay;

import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.List;

public class TextDisplayHologramRepository {

    private final String configBasePath;
    private static final String FILE_NAME = "hologramLocation.yml";

    private final TextDisplayHologramDataConfig dataConfig;

    public static HashMap<String, HashMap<String, TextDisplayHologramLocation>> textDisplayHologramLocationHashMap = new HashMap<>();


    public static HashMap<String, TextDisplay> textDisplayHologramHashMap = new HashMap<>();

    public TextDisplayHologramRepository() {
        this.configBasePath = InfinityTowerPlugin.getServerInstance().getDataFolder().getAbsolutePath();
        this.dataConfig = new TextDisplayHologramDataConfig(configBasePath, FILE_NAME);

    }

    public void reloadConfig() {
        dataConfig.reload();
    }

    public void saveConfig() {
        dataConfig.store();
    }

    public boolean hasTextDisplayHologram() {
        return dataConfig.hasHologramData();
    }

    public void storeTextDisplayHologram() {
        dataConfig.storeHologramData(textDisplayHologramLocationHashMap);
        textDisplayHologramLocationHashMap = new HashMap<>();
    }

    public void saveTextDisplayHologram() {
        dataConfig.storeHologramData(textDisplayHologramLocationHashMap);
    }

    public void loadTextDisplayHologram() {
        textDisplayHologramLocationHashMap = dataConfig.loadHologramData();
    }

    public void removeTextDisplayHologram() {
        dataConfig.removeHologramData();
    }

}
