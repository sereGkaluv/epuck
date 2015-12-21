import api.IMovable;
import impl.RobotController;

/**
 * Created by sereGkaluv on 15-Dec-15.
 */
public abstract class BangBangController extends RobotController implements IMovable {

	protected BangBangController(int lightSensorFrequency, int distanceSensorFrequency) {
		super(lightSensorFrequency, distanceSensorFrequency);
	}

    @Override
    public void turnLeft() {
        setSpeed(STANDBY_MOTOR_SPEED, MAX_FORWARD_MOTOR_SPEED);
    }

    @Override
    public void turnRight() {
        setSpeed(MAX_FORWARD_MOTOR_SPEED, STANDBY_MOTOR_SPEED);
    }

    @Override
    public void moveForward() {
        setSpeed(MAX_FORWARD_MOTOR_SPEED, MAX_FORWARD_MOTOR_SPEED);
    }

    @Override
    public void moveBackward() {
        setSpeed(MAX_BACKWARD_MOTOR_SPEED, MAX_BACKWARD_MOTOR_SPEED);
    }

    @Override
    public void rotate() {
        setSpeed(MAX_FORWARD_MOTOR_SPEED, MAX_BACKWARD_MOTOR_SPEED);
    }

    @Override
    public void standby() {
        setSpeed(STANDBY_MOTOR_SPEED, STANDBY_MOTOR_SPEED);
    }
}
