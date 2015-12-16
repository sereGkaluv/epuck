package api;

/**
 * Created by sereGkaluv on 12-Dec-15.
 */
public interface Movable extends Runnable {
	
	/**
	 * Movable turns itself to the left side.
	 */
	void turnLeft();
	
	/**
	 * Movable turns itself to the right side.
	 */
	void turnRight();

	/**
	 * Movable moves forward.
	 */
	void moveForward();
	
	/**
	 * Movable moves backward.
	 */
	void moveBackward();
	
	/**
	 * Movable stops.
	 */
	void standby();
}
