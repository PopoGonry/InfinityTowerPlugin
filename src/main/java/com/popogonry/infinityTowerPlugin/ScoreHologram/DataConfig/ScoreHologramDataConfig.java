package com.popogonry.infinityTowerPlugin.ScoreHologram.DataConfig;

import com.popogonry.infinityTowerPlugin.Config;
import com.popogonry.infinityTowerPlugin.InfinityTower.InfinityTower;
import com.popogonry.infinityTowerPlugin.ScoreHologram.ScoreHologram;

import java.util.UUID;

public class ScoreHologramDataConfig extends Config {
    public ScoreHologramDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeScoreHologramData(ScoreHologram scoreHologram) {
        getConfig().set(scoreHologram.getName(), scoreHologram);
        super.store();
    }

    public ScoreHologram loadScoreHologramData(String scoreHologramName) {
        return (ScoreHologram) getConfig().get(scoreHologramName);
    }

    public boolean hasScoreHologramData(String scoreHologramName) {
        return getConfig().contains(scoreHologramName);
    }

    public void removeScoreHologramData(String scoreHologramName) {
        getConfig().set(scoreHologramName, null);
        super.store();
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
