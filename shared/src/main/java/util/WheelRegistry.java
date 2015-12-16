package util;

import api.Classifiable;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public enum WheelRegistry implements Classifiable {
    LEFT,
    RIGHT;

    @Override
    public String getConstant() {
        return this.name();
    }
}
