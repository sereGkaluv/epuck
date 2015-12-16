package entity;

import api.Classifiable;
import util.WheelRegistry;

import java.util.Map;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public class Wheel<T extends Enum<? extends Classifiable>> {
    private static final int DEFAULT_RELATIVE_SPEED_VALUE = 0;
    private static final int DEFAULT_VECTOR_TOKEN_VALUE = 0;

    private final Map<T, Double> _velocityMap;

    private double _relativeSpeed = DEFAULT_RELATIVE_SPEED_VALUE;

    public Wheel(Map<T, Double> velocityMap) {
        _velocityMap = velocityMap;
    }

    public Map<T, Double> getVelocityMap() {
        return _velocityMap;
    }

    public double getVelocityForSensor(T sensorId) {
        if (_velocityMap.containsKey(sensorId)) return _velocityMap.get(sensorId);
        return DEFAULT_VECTOR_TOKEN_VALUE;
    }

    public double getRelativeSpeed() {
        return _relativeSpeed;
    }

    public void setRelativeSpeed(double relativeSpeed) {
        _relativeSpeed = relativeSpeed;
    }
}
