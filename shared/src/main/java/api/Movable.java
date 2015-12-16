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
	 * Movable moves itself forward.
	 */
	void moveForward();
	
	/**
	 * Movable moves itself backward.
	 */
	void moveBackward();
	
	/**
	 * Movable stops itself.
	 */
	void standby();
}
