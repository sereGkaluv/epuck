import util.DistanceSensorRegistry;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public class BangBangBalanceBallController extends BangBangController {
    private static final int LIGHT_SENSOR_FREQUENCY = 10;
    private static final int DISTANCE_SENSOR_FREQUENCY = 10;

    private BangBangBalanceBallController(int lightSensorFrequency, int distanceSensorFrequency) {
        super(lightSensorFrequency, distanceSensorFrequency);
    }

    @Override
    public void run() {
        while (step(DISTANCE_SENSOR_FREQUENCY) != -1) {
            double binocularLeft = getDistanceSensorValueFor(DistanceSensorRegistry.BINOCULAR_LEFT);
            double binocularRight = getDistanceSensorValueFor(DistanceSensorRegistry.BINOCULAR_RIGHT);
            double peripheralLeft = getDistanceSensorValueFor(DistanceSensorRegistry.PERIPHERAL_LEFT);
            double peripheralRight = getDistanceSensorValueFor(DistanceSensorRegistry.PERIPHERAL_RIGHT);

            //The ball is shifted to the left side. Turning left.
            if (binocularLeft > binocularRight || peripheralLeft > peripheralRight) turnLeft();

            //The ball is shifted to the right side. Turning right.
            else if (binocularRight > binocularLeft || peripheralRight > peripheralLeft) turnRight();

            //The ball is directly in front of the robot. Moving forward.
            else moveForward();
        }
    }

    public static void main(String[] args) {
        new BangBangBalanceBallController(
            LIGHT_SENSOR_FREQUENCY,
            DISTANCE_SENSOR_FREQUENCY
        ).run();
    }
}
