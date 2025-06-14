package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.PluginConfig;
import com.popogonry.infinityTowerPlugin.PluginRepository;
import org.bukkit.entity.Player;

public class InfinityTowerProcess {

    private final Player player;
    private final InfinityTower infinityTower;
    private int round;
    private int score;

    public InfinityTowerProcess(Player player, InfinityTower infinityTower) {
        this.player = player;
        this.infinityTower = infinityTower;
        this.round = 0;
    }

    public void infinityTowerCycle(int round) {

    }


}
