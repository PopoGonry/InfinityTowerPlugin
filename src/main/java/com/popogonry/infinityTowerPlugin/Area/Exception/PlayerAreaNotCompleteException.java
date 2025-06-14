package com.popogonry.infinityTowerPlugin.Area.Exception;

import com.popogonry.infinityTowerPlugin.Reference;
import org.bukkit.entity.Player;

public class PlayerAreaNotCompleteException extends RuntimeException {
    public PlayerAreaNotCompleteException(Player player) {
        super(Reference.prefix_exception + player.getName() + " Area not complete.");
    }
}
