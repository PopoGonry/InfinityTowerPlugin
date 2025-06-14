package com.popogonry.infinityTowerPlugin.InfinityTower.Exception;

import com.popogonry.infinityTowerPlugin.Reference;

public class NameNotFoundException extends RuntimeException {
    public NameNotFoundException(String name) {
        super(Reference.prefix_exception + name + " not found");
    }
}
