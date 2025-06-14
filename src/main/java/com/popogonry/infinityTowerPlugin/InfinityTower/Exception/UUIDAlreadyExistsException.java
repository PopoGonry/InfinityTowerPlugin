package com.popogonry.infinityTowerPlugin.InfinityTower.Exception;

import com.popogonry.infinityTowerPlugin.Reference;

import java.util.UUID;

public class UUIDAlreadyExistsException extends RuntimeException {
    public UUIDAlreadyExistsException(UUID uuid) {
        super(Reference.prefix_exception + uuid.toString() + " already exists");
    }
}
