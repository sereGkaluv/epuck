package impl;

import java.util.LinkedHashMap;
import java.util.Map;

import api.Movable;
import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.LightSensor;
import util.DistanceSensorRegistry;
import util.LightSensorRegistry;
import util.MovementSpeed;

/**
 * Created by sereGkaluv on 12-Dec-15.
 */
public abstract class RobotController extends DifferentialWheels implements Movable {
	private static final int MAX_FORWARD_MOTOR_SPEED = 1000;
    private static final int MAX_BACKWARD_MOTOR_SPEED = -1000;
    private static final int STANDBY_MOTOR_SPEED = 0;
       
    private final int _maxLightValue;
    private final int _lightSensorFrequency;
    private final Map<LightSensorRegistry, LightSensor> _lightSensors;
    
    private final int _maxDistanceValue;
    private final int _distanceSensorFrequency;
	private final Map<DistanceSensorRegistry, DistanceSensor> _distanceSensors;
	
	protected RobotController(
		int maxLightValue,
		int maxDistanceValue,
		int lightSensorFrequency,
		int distanceSensorFrequency
	) {
		super();
		
		_maxLightValue = maxLightValue;
		_maxDistanceValue = maxDistanceValue;
		
		_lightSensors = new LinkedHashMap<>();
		
		_lightSensors.put(LightSensorRegistry.LEFT, new LightSensor("ls6"));
		_lightSensors.put(LightSensorRegistry.FRONT_LEFT, new LightSensor("ls7"));
		_lightSensors.put(LightSensorRegistry.FRONT_RIGHT, new LightSensor("ls0"));
		_lightSensors.put(LightSensorRegistry.RIGHT, new LightSensor("ls1"));
		
		_distanceSensors = new LinkedHashMap<>();
	
		_distanceSensors.put(DistanceSensorRegistry.LEFT, new DistanceSensor("ps6"));
		_distanceSensors.put(DistanceSensorRegistry.FRONT_LEFT, new DistanceSensor("ps7"));
		_distanceSensors.put(DistanceSensorRegistry.FRONT_RIGHT, new DistanceSensor("ps0"));
		_distanceSensors.put(DistanceSensorRegistry.RIGHT, new DistanceSensor("ps1"));
	
		//initializing light sensors
		_lightSensorFrequency = lightSensorFrequency;
		_lightSensors.values().forEach(sensor -> sensor.enable(_lightSensorFrequency));
		
		//initializing distance sensors
		_distanceSensorFrequency = distanceSensorFrequency;
		_distanceSensors.values().forEach(sensor -> sensor.enable(_distanceSensorFrequency));
	}
	
	@Override
	public void turnLeft() {
		setSpeed(MAX_BACKWARD_MOTOR_SPEED, MAX_FORWARD_MOTOR_SPEED);
	}
	
	@Override
	public void turnRight() {
		setSpeed(MAX_FORWARD_MOTOR_SPEED, MAX_BACKWARD_MOTOR_SPEED);
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
	public void standby() {
		setSpeed(STANDBY_MOTOR_SPEED, STANDBY_MOTOR_SPEED);
	}
	
	@Override
	public void run() {
        while (step(_distanceSensorFrequency) != -1) {
            MovementSpeed movementSpeed = calculateMovementSpeed();
            
            double rawSpeedLeft = Math.max(movementSpeed.getSpeedLeft(), MAX_BACKWARD_MOTOR_SPEED);
            double normalisedSpeedLeft = Math.min(rawSpeedLeft, MAX_FORWARD_MOTOR_SPEED);
            
            double rawSpeedRight = Math.max(movementSpeed.getSpeedRight(), MAX_BACKWARD_MOTOR_SPEED);
            double normalisedSpeedRight = Math.min(rawSpeedRight, MAX_FORWARD_MOTOR_SPEED);
            
            setSpeed(normalisedSpeedLeft, normalisedSpeedRight);
        }
        
        standby();
	}
	
	/**
	 * Calculates movement speed for the call moment.
	 * 
	 * @return instance of MovementSpeed object.
	 * @throws IllegalArgumentException; 
	 */
	protected abstract MovementSpeed calculateMovementSpeed()
	throws IllegalArgumentException;
	
	/**
	 * Calculates movement speed for the call moment.
	 * 
	 * @return instance of MovementSpeed object. 
	 */
	protected double convertToRealSpeed(double percentage) {
		if (percentage > 0) {
			return MAX_FORWARD_MOTOR_SPEED * percentage;
		} else if (percentage < 0) {
			return MAX_BACKWARD_MOTOR_SPEED * percentage;
		}
		return STANDBY_MOTOR_SPEED;
	}
	
	/**
	 * Returns value of the given light sensor if registered.
	 * 
	 * @param lightSensor given light sensor to be checked.
	 * @return value of the given light sensor.
	 * @throws IllegalArgumentException if sensor is not registered.
	 */
	protected double getLightSensorValue(LightSensorRegistry lightSensor)
	throws IllegalArgumentException {
		if (lightSensor != null) {
			LightSensor lSensor = _lightSensors.get(lightSensor);
			
			if (lSensor != null) {
				return lSensor.getValue();
			}
		}
		throw new IllegalArgumentException(
			"Unknown sensor lookup detected - " + lightSensor.name()
		);
	}
	
	/**
	 * Returns value of the given light sensor if registered in percentage form.
	 * 
	 * @param lightSensor given light sensor to be checked.
	 * @return value of the given light sensor in percentage form.
	 * @throws IllegalArgumentException if sensor is not registered.
	 */
	protected double getLightSensorPercentValue(LightSensorRegistry lightSensor)
	throws IllegalArgumentException {
		return getLightSensorValue(lightSensor) / _maxLightValue;
	}

	/**
	 * Returns value of the given distance sensor if registered.
	 * 
	 * @param distanceSensor given distance sensor to be checked.
	 * @return value of the given distance sensor.
	 * @throws IllegalArgumentException if sensor is not registered.
	 */
	protected double getDistanceSensorValue(DistanceSensorRegistry distanceSensor)
	throws IllegalArgumentException {
		return getSensor(distanceSensor).getValue();
	}
	
	/**
	 * Returns value of the given distance sensor if registered in percentage form.
	 * 
	 * @param distanceSensor given distance sensor to be checked.
	 * @return value of the given distance sensor in percentage form.
	 * @throws IllegalArgumentException if sensor is not registered.
	 */
	protected double getDistanceSensorPercentValue(DistanceSensorRegistry distanceSensor)
	throws IllegalArgumentException {
		return getDistanceSensorValue(distanceSensor) / _maxDistanceValue;
	}
	
	/**
	 * Returns all distance sensors.
	 * 
	 * @return all distance sensors.
	 */
	protected abstract DistanceSensor[] getAllDistanceSensors();
	
	/**
	 * Returns all distance sensors.
	 * 
	 * @return all distance sensors.
	 */
	protected DistanceSensor getSensor(DistanceSensorRegistry distanceSensor) {
		if (distanceSensor != null) {
			DistanceSensor dSensor = _distanceSensors.get(distanceSensor);
			
			if (dSensor != null) return dSensor;
		}
		throw new IllegalArgumentException(
			"Unknown sensor lookup detected - " + distanceSensor
		);
	}
	
	/**
	 * Returns remaining percentage, e.g. if given value will be 60 returned value will be 40.
	 * 
	 * @param actualPercentage given percentage
	 * @return remaining percentage
	 */
	protected double getRemainingPercentage(double actualPercentage) {
		if (actualPercentage > 0) {
			return 1 - actualPercentage;
		} else if (actualPercentage < 0) {
			return -1 - actualPercentage;
		}
		return 0;
	}
}
