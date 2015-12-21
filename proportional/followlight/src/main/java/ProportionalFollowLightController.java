import entity.Wheel;
import util.LightSensorRegistry;
import util.WheelRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public class ProportionalFollowLightController extends ProportionalController {
    private static final int LIGHT_SENSOR_FREQUENCY = 10;
    private static final int DISTANCE_SENSOR_FREQUENCY = 10;
    private static final double PROXIMITY_VALUE = 500;
    private static final double PROXIMITY_PERCENT = translateToLightPercent(PROXIMITY_VALUE);

    private ProportionalFollowLightController(
        int lightSensorFrequency,
        int distanceSensorFrequency,
        Map<WheelRegistry, Wheel> wheels
    ) {
        super(lightSensorFrequency, distanceSensorFrequency, wheels);
    }

    @Override
    protected double modifySensorPercentValue(double sensorPercentValue) {
        if (sensorPercentValue <= PROXIMITY_PERCENT) return -7 - sensorPercentValue;
        else return getRemainingPercentage(sensorPercentValue);
    }

    @Override
    protected LightSensorRegistry[] getRegisteredSensors() {
        return LightSensorRegistry.values();
    }

    public static void main(String[] args) {

        Map<LightSensorRegistry, Double> leftControllerMap = new HashMap<>();
        leftControllerMap.put(LightSensorRegistry.LEFT, -1.0);
        leftControllerMap.put(LightSensorRegistry.BACK_LEFT, -0.01);
        leftControllerMap.put(LightSensorRegistry.BINOCULAR_RIGHT, 1.0);
        leftControllerMap.put(LightSensorRegistry.RIGHT, 1.0);
        leftControllerMap.put(LightSensorRegistry.BACK_RIGHT, 0.01);

        Map<LightSensorRegistry, Double> rightControllerMap = new HashMap<>();
        rightControllerMap.put(LightSensorRegistry.LEFT, 1.0);
        rightControllerMap.put(LightSensorRegistry.BACK_LEFT, 0.01);
        rightControllerMap.put(LightSensorRegistry.BINOCULAR_LEFT, 1.0);
        rightControllerMap.put(LightSensorRegistry.RIGHT, -1.0);
        rightControllerMap.put(LightSensorRegistry.BACK_RIGHT, -0.01);

        Map<WheelRegistry, Wheel> wheels = new HashMap<>();
        wheels.put(WheelRegistry.LEFT, new Wheel(leftControllerMap));
        wheels.put(WheelRegistry.RIGHT, new Wheel(rightControllerMap));

        new ProportionalFollowLightController(
            LIGHT_SENSOR_FREQUENCY,
            DISTANCE_SENSOR_FREQUENCY,
            wheels
        ).run();
    }
}
