package entity;

import api.IClassifiable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public class Wheel {
    private static final int DEFAULT_RELATIVE_SPEED_VALUE = 0;
    private static final int DEFAULT_CONSTANT_VALUE = 0;
    private static final int DEFAULT_VECTOR_TOKEN_VALUE = 0;

    private double _relativeSpeed = DEFAULT_RELATIVE_SPEED_VALUE;

    private final Map<? extends IClassifiable, Double> _constantsMap;
    private final Map<? extends IClassifiable, Double> _controllerMap;

    public Wheel(Map<? extends IClassifiable, Double> controllerMap) {
        _constantsMap = new HashMap<>();
        _controllerMap = controllerMap;
    }

    public Wheel(Map<? extends IClassifiable, Double> constantsMap, Map<? extends IClassifiable, Double> controllerMap) {
        _constantsMap = constantsMap;
        _controllerMap = controllerMap;
    }

    /**
     * Returns a constant value if specified for given sensor for {@code this} wheel.
     *
     * @param sensorId sensor for which constant will be returned.
     * @return constant value for the given sensor if specified or {@code DEFAULT_CONSTANT_VALUE}.
     */
    public double getConstantFor(IClassifiable sensorId) {
        if (_constantsMap.containsKey(sensorId)) return _constantsMap.get(sensorId);
        return DEFAULT_CONSTANT_VALUE;
    }

    /**
     * Returns controller value if specified for given sensor for {@code this} wheel.
     *
     * @param sensorId sensor for which controller value will be returned.
     * @return controller value for the given sensor if specified or {@code DEFAULT_VECTOR_TOKEN_VALUE}.
     */
    public double getControllerValueFor(IClassifiable sensorId) {
        if (_controllerMap.containsKey(sensorId)) return _controllerMap.get(sensorId);
        return DEFAULT_VECTOR_TOKEN_VALUE;
    }

    /**
     * Returns current value of relative speed field.
     *
     * @return relative speed value.
     */
    public double getRelativeSpeed() {
        return _relativeSpeed;
    }

    /**
     * Setts relative speed for {@code this} wheel.
     *
     * @param relativeSpeed relative speed value to be set.
     */
    public void setRelativeSpeed(double relativeSpeed) {
        _relativeSpeed = relativeSpeed;
    }
}
