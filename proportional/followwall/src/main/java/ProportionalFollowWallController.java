import entity.Wheel;
import util.DistanceSensorRegistry;
import util.LightSensorRegistry;
import util.WheelRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public class ProportionalFollowWallController extends ProportionalController<DistanceSensorRegistry> {
    private static final int LIGHT_SENSOR_FREQUENCY = 10;
    private static final int DISTANCE_SENSOR_FREQUENCY = 10;
    private static final double CONSTANT = 0;

    private ProportionalFollowWallController(
        int lightSensorFrequency,
        int distanceSensorFrequency,
        double constant,
        Map<WheelRegistry, Wheel<DistanceSensorRegistry>> wheels
    ) {
        super(lightSensorFrequency, distanceSensorFrequency, constant, wheels);
    }

    @Override
    protected double getSensorValue(DistanceSensorRegistry sensorId) {
        return getDistanceSensorPercentValue(sensorId);
    }

    @Override
    protected DistanceSensorRegistry[] getRegisteredSensors() {
        return DistanceSensorRegistry.values();
    }

    public static void main(String[] args) {

        Map<DistanceSensorRegistry, Double> rightVelocityMap = new HashMap<>();
        rightVelocityMap.put(DistanceSensorRegistry.LEFT, 1.0);
        rightVelocityMap.put(DistanceSensorRegistry.PERIPHERAL_LEFT, 1.0);
        rightVelocityMap.put(DistanceSensorRegistry.BINOCULAR_RIGHT, -0.5);

        Map<DistanceSensorRegistry, Double> leftVelocityMap = new HashMap<>();
        leftVelocityMap.put(DistanceSensorRegistry.LEFT, 1.0);
        rightVelocityMap.put(DistanceSensorRegistry.PERIPHERAL_LEFT, 1.0);
        leftVelocityMap.put(DistanceSensorRegistry.BINOCULAR_RIGHT, 0.5);

        Map<WheelRegistry, Wheel<DistanceSensorRegistry>> wheels = new HashMap<>();
        wheels.put(WheelRegistry.RIGHT, new Wheel<>(rightVelocityMap));
        wheels.put(WheelRegistry.LEFT, new Wheel<>(leftVelocityMap));

        new ProportionalFollowWallController(
            LIGHT_SENSOR_FREQUENCY,
            DISTANCE_SENSOR_FREQUENCY,
            CONSTANT,
            wheels
        ).run();
    }
}
