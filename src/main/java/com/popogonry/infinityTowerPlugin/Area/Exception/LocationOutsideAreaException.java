package com.popogonry.infinityTowerPlugin.Area.Exception;

import com.popogonry.infinityTowerPlugin.Reference;

import java.util.ArrayList;
import java.util.Collections;

public class LocationOutsideAreaException extends RuntimeException {
    public LocationOutsideAreaException(double[] location) {
        super(Reference.prefix_exception + location[0] + " " + location[1] + " " + location[2] + " outside the area.");
    }
}
