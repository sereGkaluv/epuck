import util.DistanceSensorRegistry;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public class BangBangFollowWallController extends BangBangController {
    private static final int LIGHT_SENSOR_FREQUENCY = 10;
    private static final int DISTANCE_SENSOR_FREQUENCY = 10;
    private static final int DISTANCE_TO_THE_WALL = 100;

    private BangBangFollowWallController(int lightSensorFrequency, int distanceSensorFrequency) {
        super(lightSensorFrequency, distanceSensorFrequency);
    }

    @Override
    public void run() {
        while (step(DISTANCE_SENSOR_FREQUENCY) != -1) {
            double binocularRight = getDistanceSensorValueFor(DistanceSensorRegistry.PERIPHERAL_RIGHT);
            double binocularLeft = getDistanceSensorValueFor(DistanceSensorRegistry.BINOCULAR_LEFT);
            double peripheralLeft = getDistanceSensorValueFor(DistanceSensorRegistry.PERIPHERAL_LEFT);
            double left = getDistanceSensorValueFor(DistanceSensorRegistry.LEFT);
            double backLeft = getDistanceSensorValueFor(DistanceSensorRegistry.BACK_LEFT);

            //If there is a wall in the front and followed wall turns left - turn left.
            if (binocularLeft > DISTANCE_TO_THE_WALL && binocularLeft < binocularRight) turnLeft();

            //If there is a wall in the front - turn right (common case).
            else if (binocularLeft > DISTANCE_TO_THE_WALL && binocularLeft >= binocularRight) turnRight();

            //Angle between the robot's back and wall on the left is too big. Turning right.
            else if (peripheralLeft > DISTANCE_TO_THE_WALL) turnRight();

            //Angle between the front of the robot and wall on the left is too big. Turning left.
            else if (backLeft > DISTANCE_TO_THE_WALL) turnLeft();

            //Wall turns itself. Turning left to find it again.
            else if (
                peripheralLeft < DISTANCE_TO_THE_WALL &&
                left < DISTANCE_TO_THE_WALL &&
                backLeft < DISTANCE_TO_THE_WALL
            ) turnLeft();

            //Robot is positioned under correct angle. Moving forward.
            else moveForward();
        }
    }

    public static void main(String[] args) {
        new BangBangFollowWallController(
            LIGHT_SENSOR_FREQUENCY,
            DISTANCE_SENSOR_FREQUENCY
        ).run();
    }
}
