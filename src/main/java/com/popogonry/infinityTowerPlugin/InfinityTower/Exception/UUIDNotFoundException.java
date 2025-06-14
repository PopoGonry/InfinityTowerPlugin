package com.popogonry.infinityTowerPlugin.InfinityTower.Exception;

import com.popogonry.infinityTowerPlugin.Reference;

import java.util.UUID;

public class UUIDNotFoundException extends RuntimeException {
    public UUIDNotFoundException(UUID uuid) {
        super(Reference.prefix_exception + uuid.toString() + " not found");
    }
}
