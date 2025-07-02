package com.popogonry.infinityTowerPlugin.ScoreHologram.DataConfig;

import com.popogonry.infinityTowerPlugin.Config;
import com.popogonry.infinityTowerPlugin.ScoreHologram.ScoreHologram;

import java.util.*;

public class ScoreHologramSetDataConfig extends Config {
    private final String saveName = "scoreHologramSet";

    public ScoreHologramSetDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeScoreHologramSet(Set<String> scoreHologramNameSet) {
        getConfig().set(saveName, new ArrayList<>(scoreHologramNameSet));
        super.store();
    }

    public Set<String> loadScoreHologramSet() {
        return new HashSet<>(getConfig().getStringList(saveName));
    }

    public boolean hasScoreHologramSet() {
        return getConfig().contains(saveName);
    }

    public void removeScoreHologramSet() {
        getConfig().set(saveName, null);
        super.store();
    }

    @Override
    public void loadDefaults() {
    }

    @Override
    public void applySettings() {
    }
}
