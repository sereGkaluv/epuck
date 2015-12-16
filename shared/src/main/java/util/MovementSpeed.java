package util;

/**
 * Created by sereGkaluv on 12-Dec-15.
 *
 * Entity object that stores speed values for left and right wheels.
 * This Entity is implemented with usage of Builder pattern.
 */
public class MovementSpeed {
	
	private double _speedLeft;
	private double _speedRight;

	public MovementSpeed() {
	}
	
	public MovementSpeed(double speedLeft, double speedRight) {
		_speedLeft = speedLeft;
		_speedRight = speedRight;
	}

	public double getSpeedLeft() {
		return _speedLeft;
	}

	public MovementSpeed getSpeedLeft(double speedLeft) {
		_speedLeft = speedLeft;
		return this;
	}
	
	public double getSpeedRight() {
		return _speedRight;
	}

	public MovementSpeed setSpeedRight(double speedRight) {
		_speedRight = speedRight;
		return this;
	}
}
