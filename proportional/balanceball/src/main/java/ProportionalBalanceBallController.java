import entity.Wheel;
import util.DistanceSensorRegistry;
import util.WheelRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public class ProportionalBalanceBallController extends ProportionalController {
    private static final int LIGHT_SENSOR_FREQUENCY = 10;
    private static final int DISTANCE_SENSOR_FREQUENCY = 10;

    private ProportionalBalanceBallController(
        int lightSensorFrequency,
        int distanceSensorFrequency,
        Map<WheelRegistry, Wheel> wheels
    ) {
        super(lightSensorFrequency, distanceSensorFrequency, wheels);
    }

    @Override
    protected double modifySensorPercentValue(double sensorPercentValue) {
        return sensorPercentValue;
    }

    @Override
    protected DistanceSensorRegistry[] getRegisteredSensors() {
        return DistanceSensorRegistry.values();
    }

    public static void main(String[] args) {

        Map<DistanceSensorRegistry, Double> leftControllerMap = new HashMap<>();
        leftControllerMap.put(DistanceSensorRegistry.BINOCULAR_LEFT, -1.0);
        leftControllerMap.put(DistanceSensorRegistry.BINOCULAR_RIGHT, 1.0);
        leftControllerMap.put(DistanceSensorRegistry.PERIPHERAL_LEFT, -1.0);
        leftControllerMap.put(DistanceSensorRegistry.PERIPHERAL_RIGHT, 1.0);
        leftControllerMap.put(DistanceSensorRegistry.LEFT, -9.0);
        leftControllerMap.put(DistanceSensorRegistry.RIGHT, 9.0);
        leftControllerMap.put(DistanceSensorRegistry.BACK_LEFT, -9.0);
        leftControllerMap.put(DistanceSensorRegistry.BACK_RIGHT, 9.0);

        Map<DistanceSensorRegistry, Double> rightControllerMap = new HashMap<>();
        rightControllerMap.put(DistanceSensorRegistry.BINOCULAR_LEFT, -1.0);
        rightControllerMap.put(DistanceSensorRegistry.BINOCULAR_RIGHT, 1.0);
        rightControllerMap.put(DistanceSensorRegistry.PERIPHERAL_RIGHT, -1.0);
        rightControllerMap.put(DistanceSensorRegistry.PERIPHERAL_LEFT, 1.0);
        rightControllerMap.put(DistanceSensorRegistry.LEFT, 9.0);
        rightControllerMap.put(DistanceSensorRegistry.RIGHT, -9.0);
        rightControllerMap.put(DistanceSensorRegistry.BACK_LEFT, 9.0);
        rightControllerMap.put(DistanceSensorRegistry.BACK_RIGHT, -9.0);

        Map<DistanceSensorRegistry, Double> constantsMap = new HashMap<>();
        constantsMap.put(DistanceSensorRegistry.BINOCULAR_LEFT, 1.0);
        constantsMap.put(DistanceSensorRegistry.BINOCULAR_RIGHT, 1.0);

        Map<WheelRegistry, Wheel> wheels = new HashMap<>();
        wheels.put(WheelRegistry.LEFT, new Wheel(constantsMap, leftControllerMap));
        wheels.put(WheelRegistry.RIGHT, new Wheel(constantsMap, rightControllerMap));

        new ProportionalBalanceBallController(
            LIGHT_SENSOR_FREQUENCY,
            DISTANCE_SENSOR_FREQUENCY,
            wheels
        ).run();
    }
}
