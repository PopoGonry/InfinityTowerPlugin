
package com.popogonry.infinityTowerPlugin;

import java.util.List;

public class PluginConfig {
    private final int roundScore;
    private final int roundClearSeconds;
    private final List<String> allowedCommands;
    private final int oneTimeMobSpawnNumber;
    private final int mobSpawnDelaySeconds;

    public PluginConfig(int roundScore, int roundClearSeconds, List<String> allowedCommands, int oneTimeMobSpawnNumber, int mobSpawnDelaySeconds) {
        this.roundScore = roundScore;
        this.roundClearSeconds = roundClearSeconds;
        this.allowedCommands = allowedCommands;
        this.oneTimeMobSpawnNumber = oneTimeMobSpawnNumber;
        this.mobSpawnDelaySeconds = mobSpawnDelaySeconds;
    }

    @Override
    public String toString() {
        return "PluginConfig{" +
                "roundScore=" + roundScore +
                ", roundClearSeconds=" + roundClearSeconds +
                ", allowedCommands=" + allowedCommands +
                ", oneTimeMobSpawnNumber=" + oneTimeMobSpawnNumber +
                ", mobSpawnDelaySeconds=" + mobSpawnDelaySeconds +
                '}';
    }

    public int getRoundScore() {
        return roundScore;
    }

    public int getRoundClearSeconds() {
        return roundClearSeconds;
    }

    public List<String> getAllowedCommands() {
        return allowedCommands;
    }

    public int getOneTimeMobSpawnNumber() {
        return oneTimeMobSpawnNumber;
    }

    public int getMobSpawnDelaySeconds() {
        return mobSpawnDelaySeconds;
    }
}
