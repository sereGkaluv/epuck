package util;

import api.IClassifiable;

/**
 * Created by sereGkaluv on 12-Dec-15.
 */
public enum LightSensorRegistry implements IClassifiable {
    BINOCULAR_RIGHT("ls0"),
    PERIPHERAL_RIGHT("ls1"),
    RIGHT("ls2"),
    BACK_RIGHT("ls3"),
    BACK_LEFT("ls4"),
    LEFT("ls5"),
    PERIPHERAL_LEFT("ls6"),
    BINOCULAR_LEFT("ls7");

    private final String _internalConstant;

    LightSensorRegistry(String internalConstant) {
        _internalConstant = internalConstant;
    }

    @Override
    public String getNamingConstant() {
        return _internalConstant;
    }
}
