
package com.popogonry.infinityTowerPlugin;

import java.util.List;

public class PluginConfig {
    private final int roundScore;
    private final int roundClearSeconds;
    private final List<String> allowedCommands;

    public PluginConfig(int roundScore, int roundClearSeconds, List<String> allowedCommands) {
        this.roundScore = roundScore;
        this.roundClearSeconds = roundClearSeconds;
        this.allowedCommands = allowedCommands;
    }

    @Override
    public String toString() {
        return "PluginConfig{" +
                "roundScore=" + roundScore +
                ", roundClearSeconds=" + roundClearSeconds +
                ", allowedCommands=" + allowedCommands +
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
}
