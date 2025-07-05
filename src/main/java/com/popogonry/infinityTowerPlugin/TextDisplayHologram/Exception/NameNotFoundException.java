package com.popogonry.infinityTowerPlugin.TextDisplayHologram.Exception;

public class NameNotFoundException extends RuntimeException {
    public NameNotFoundException(String name) {
        super(name + " 이라는 이름은 존재하지 않습니다.");
    }
}
