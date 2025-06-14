package com.popogonry.infinityTowerPlugin.InfinityTower.Exception;

import com.popogonry.infinityTowerPlugin.Reference;

public class NameAlreadyExistsException extends RuntimeException {
    public NameAlreadyExistsException(String name) {
        super(Reference.prefix_exception + name + " already exists");
    }
}
