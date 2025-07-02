package com.popogonry.infinityTowerPlugin.Monster.Exception;

public class MonsterNotFoundException extends RuntimeException {
    public MonsterNotFoundException(String monsterName) {
        super(monsterName + " monster not found");
    }
}
