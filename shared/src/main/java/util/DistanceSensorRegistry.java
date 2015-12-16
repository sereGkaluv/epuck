package util;

import api.Classifiable;

/**
 * Created by sereGkaluv on 12-Dec-15.
 */
public enum DistanceSensorRegistry implements Classifiable {
    BINOCULAR_RIGHT("ps0"),
    PERIPHERAL_RIGHT("ps1"),
    RIGHT("ps2"),
    BACK_RIGHT("ps3"),
    BACK_LEFT("ps4"),
    LEFT("ps5"),
    PERIPHERAL_LEFT("ps6"),
    BINOCULAR_LEFT("ps7");

    private final String _internalConstant;

    DistanceSensorRegistry(String internalConstant) {
        _internalConstant = internalConstant;
    }

    @Override
    public String getConstant() {
        return _internalConstant;
    }
}
