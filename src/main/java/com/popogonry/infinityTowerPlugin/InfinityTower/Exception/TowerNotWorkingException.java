package com.popogonry.infinityTowerPlugin.InfinityTower.Exception;

public class TowerNotWorkingException extends RuntimeException {
    public TowerNotWorkingException(String message) {
        super(message + " tower is not working");
    }
}
