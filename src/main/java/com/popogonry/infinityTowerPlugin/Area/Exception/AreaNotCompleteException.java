package com.popogonry.infinityTowerPlugin.Area.Exception;

import com.popogonry.infinityTowerPlugin.Area.Area;
import com.popogonry.infinityTowerPlugin.Reference;

public class AreaNotCompleteException extends RuntimeException {
    public AreaNotCompleteException(Area area) {
        super(Reference.prefix_exception + area.toString() + " not complete.");
    }
}
