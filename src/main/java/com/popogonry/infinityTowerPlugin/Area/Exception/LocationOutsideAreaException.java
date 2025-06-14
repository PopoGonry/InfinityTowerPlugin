package com.popogonry.infinityTowerPlugin.Area.Exception;

import com.popogonry.infinityTowerPlugin.Reference;

public class LocationOutsideAreaException extends RuntimeException {
    public LocationOutsideAreaException(double[] location) {
        super(Reference.prefix_exception + location.toString() + " outside the area.");
    }
}
