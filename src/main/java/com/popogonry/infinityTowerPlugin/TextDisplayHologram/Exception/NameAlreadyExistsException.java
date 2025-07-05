package com.popogonry.infinityTowerPlugin.TextDisplayHologram.Exception;

import com.popogonry.infinityTowerPlugin.Reference;

public class NameAlreadyExistsException extends RuntimeException {
    public NameAlreadyExistsException(String name) {
        super(name + " 이라는 이름이 이미 존재합니다.");
    }
}
