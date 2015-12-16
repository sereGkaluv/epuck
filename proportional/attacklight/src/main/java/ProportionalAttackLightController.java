import entity.Wheel;
import util.LightSensorRegistry;
import util.WheelRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public class ProportionalAttackLightController extends ProportionalController<LightSensorRegistry> {
    private static final int LIGHT_SENSOR_FREQUENCY = 10;
    private static final int DISTANCE_SENSOR_FREQUENCY = 10;
    private static final double CONSTANT = 0;

    private ProportionalAttackLightController(
        int lightSensorFrequency,
        int distanceSensorFrequency,
        double constant,
        Map<WheelRegistry, Wheel<LightSensorRegistry>> wheels
    ) {
        super(lightSensorFrequency, distanceSensorFrequency, constant, wheels);
    }

    @Override
    protected double getSensorValue(LightSensorRegistry sensorId) {
        return getLightSensorPercentValue(sensorId);
    }

    @Override
    protected LightSensorRegistry[] getRegisteredSensors() {
        return LightSensorRegistry.values();
    }

    public static void main(String[] args) {

        Map<LightSensorRegistry, Double> rightVelocityMap = new HashMap<>();
        rightVelocityMap.put(LightSensorRegistry.BINOCULAR_RIGHT, 1.0);
        rightVelocityMap.put(LightSensorRegistry.PERIPHERAL_RIGHT, 1.0);
        rightVelocityMap.put(LightSensorRegistry.RIGHT, 1.0);
        rightVelocityMap.put(LightSensorRegistry.BACK_RIGHT, 1.0);

        Map<LightSensorRegistry, Double> leftVelocityMap = new HashMap<>();
        leftVelocityMap.put(LightSensorRegistry.BACK_LEFT, 1.0);
        leftVelocityMap.put(LightSensorRegistry.LEFT, 1.0);
        leftVelocityMap.put(LightSensorRegistry.PERIPHERAL_LEFT, 1.0);
        leftVelocityMap.put(LightSensorRegistry.BINOCULAR_LEFT, 1.0);

        Map<WheelRegistry, Wheel<LightSensorRegistry>> wheels = new HashMap<>();
        wheels.put(WheelRegistry.RIGHT, new Wheel<>(rightVelocityMap));
        wheels.put(WheelRegistry.LEFT, new Wheel<>(leftVelocityMap));

        new ProportionalAttackLightController(
            LIGHT_SENSOR_FREQUENCY,
            DISTANCE_SENSOR_FREQUENCY,
            CONSTANT,
            wheels
        ).run();
    }
}
