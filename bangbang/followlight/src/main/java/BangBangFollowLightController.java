import util.LightSensorRegistry;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public class BangBangFollowLightController extends BangBangController {
    private static final int LIGHT_SENSOR_FREQUENCY = 10;
    private static final int DISTANCE_SENSOR_FREQUENCY = 10;
    private static final double PROXIMITY_VALUE = 500;

    private BangBangFollowLightController(int lightSensorFrequency, int distanceSensorFrequency) {
        super(lightSensorFrequency, distanceSensorFrequency);
    }

    @Override
    public void run() {
        while (step(DISTANCE_SENSOR_FREQUENCY) != -1) {

            if (!isInProximityRangeOf(PROXIMITY_VALUE)) {
                double leftSpeed = getLeftSensorsValue();
                double rightSpeed = getRightSensorsValue();

                if (leftSpeed < rightSpeed) turnLeft();
                else if (rightSpeed < leftSpeed) turnRight();
                else if (leftSpeed != 0 && rightSpeed != 0) moveForward();
                else standby();

            } else standby();
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

    /**
     * Checks if light sensor value in the front of the robot is in the given proximity range.
     *
     * @param proximityValue given proximity value that will be used for comparison.
     * @return true if in range, false if not.
     */
    private boolean isInProximityRangeOf(double proximityValue) {
        return getLightSensorValueFor(LightSensorRegistry.BINOCULAR_LEFT) <= proximityValue &&
               getLightSensorValueFor(LightSensorRegistry.BINOCULAR_RIGHT) <= proximityValue;
    }

    public static void main(String[] args) {
        new BangBangFollowLightController(
            LIGHT_SENSOR_FREQUENCY,
            DISTANCE_SENSOR_FREQUENCY
        ).run();
    }
}
