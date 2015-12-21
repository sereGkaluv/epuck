import util.LightSensorRegistry;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public class BangBangAttackLightController extends BangBangController {
    private static final int LIGHT_SENSOR_FREQUENCY = 10;
    private static final int DISTANCE_SENSOR_FREQUENCY = 10;

    private BangBangAttackLightController(int lightSensorFrequency, int distanceSensorFrequency) {
        super(lightSensorFrequency, distanceSensorFrequency);
    }

    @Override
    public void run() {
        while (step(DISTANCE_SENSOR_FREQUENCY) != -1) {
            double leftSpeed = getLeftSensorsValue();
            double rightSpeed = getRightSensorsValue();

            if (leftSpeed < rightSpeed) turnLeft();
            else if (rightSpeed < leftSpeed) turnRight();
            else if (leftSpeed != 0 && rightSpeed != 0) moveForward();
            else standby();
        }
    }

    /**
     * Calculates movement speed for Left wheel at the moment of method call.
     *
     * @return leftSpeed value.
     */
    private double getLeftSensorsValue() {
        return getLightSensorValueFor(LightSensorRegistry.BINOCULAR_LEFT) +
               getLightSensorValueFor(LightSensorRegistry.PERIPHERAL_LEFT) +
               getLightSensorValueFor(LightSensorRegistry.LEFT) +
               getLightSensorValueFor(LightSensorRegistry.BACK_LEFT);
    }

    /**
     * Calculates movement speed for Right wheel at the moment of method call.
     *
     * @return rightSpeed value.
     */
    private double getRightSensorsValue() {
        return getLightSensorValueFor(LightSensorRegistry.BINOCULAR_RIGHT) +
               getLightSensorValueFor(LightSensorRegistry.PERIPHERAL_RIGHT) +
               getLightSensorValueFor(LightSensorRegistry.RIGHT) +
               getLightSensorValueFor(LightSensorRegistry.BACK_RIGHT);
    }

    public static void main(String[] args) {
        new BangBangAttackLightController(
            LIGHT_SENSOR_FREQUENCY,
            DISTANCE_SENSOR_FREQUENCY
        ).run();
    }
}
