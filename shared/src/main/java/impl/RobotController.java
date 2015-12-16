package impl;

import api.Classifiable;
import api.Movable;
import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.LightSensor;
import entity.Wheel;
import util.DistanceSensorRegistry;
import util.LightSensorRegistry;
import util.WheelRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sereGkaluv on 12-Dec-15.
 */
public abstract class RobotController<T extends Enum<? extends Classifiable>> extends DifferentialWheels implements Movable {
    private static final int MAX_FORWARD_MOTOR_SPEED = 1000;
    private static final int STANDBY_MOTOR_SPEED = 0;
    private static final int MAX_BACKWARD_MOTOR_SPEED = -1000;
    private static final int MAX_LIGHT_VALUE = 4095;
    private static final int MAX_DISTANCE_VALUE = 4000;

    private final int _lightSensorFrequency;
    private final Map<LightSensorRegistry, LightSensor> _lightSensors;

    private final int _distanceSensorFrequency;
	private final Map<DistanceSensorRegistry, DistanceSensor> _distanceSensors;
	
	protected RobotController(int lightSensorFrequency, int distanceSensorFrequency) {
		super();

        //initializing light sensors
        _lightSensors = new HashMap<>();
        _lightSensorFrequency = lightSensorFrequency;

        for (LightSensorRegistry sensorId : LightSensorRegistry.values()) {
            LightSensor sensor = new LightSensor(sensorId.getConstant());
            sensor.enable(_lightSensorFrequency);

            _lightSensors.put(sensorId, sensor);
        }

        //initializing distance sensors
		_distanceSensors = new HashMap<>();
        _distanceSensorFrequency = distanceSensorFrequency;

        for (DistanceSensorRegistry sensorId : DistanceSensorRegistry.values()) {
            DistanceSensor sensor = new DistanceSensor(sensorId.getConstant());
            sensor.enable(_distanceSensorFrequency);

            _distanceSensors.put(sensorId, sensor);
        }
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
            Map<WheelRegistry, Wheel<T>> wheelMap = calculateMovementSpeed();

            double relativeLeftSpeed = wheelMap.get(WheelRegistry.LEFT).getRelativeSpeed();
            double relativeRightSpeed = wheelMap.get(WheelRegistry.RIGHT).getRelativeSpeed();

            setSpeed(
                convertRelativeToRealSpeed(relativeLeftSpeed),
                convertRelativeToRealSpeed(relativeRightSpeed)
            );
        }
        
        standby();
	}
	
	/**
	 * Calculates movement speed for each given wheel at the moment of method call.
	 * 
	 * @return instance of WheelContainer object.
	 * @throws IllegalArgumentException; 
	 */
	protected abstract Map<WheelRegistry, Wheel<T>> calculateMovementSpeed()
	throws IllegalArgumentException;
	
	/**
	 * Converts given relative speed to real speed.
	 * 
	 * @return real speed value.
	 */
	protected double convertRelativeToRealSpeed(double relativeSpeed) {
		if (relativeSpeed > 0) {
			return Math.min(MAX_FORWARD_MOTOR_SPEED, relativeSpeed * MAX_FORWARD_MOTOR_SPEED);
		} else if (relativeSpeed < 0) {
			return Math.max(MAX_BACKWARD_MOTOR_SPEED, relativeSpeed * MAX_BACKWARD_MOTOR_SPEED);
		}
		return STANDBY_MOTOR_SPEED;
	}


    protected abstract T[] getRegisteredSensors();

	/**
	 * Returns value of the given light sensor if registered.
	 * 
	 * @param lightSensorId given light sensor to be checked.
	 * @return value of the given light sensor.
	 * @throws IllegalArgumentException if sensor is not registered.
	 */
	protected double getLightSensorValue(LightSensorRegistry lightSensorId)
	throws IllegalArgumentException {
		if (lightSensorId != null) {
			LightSensor lSensor = _lightSensors.get(lightSensorId);
			
			if (lSensor != null) return lSensor.getValue();
		}
		throw new IllegalArgumentException("Unknown sensor lookup detected - " + lightSensorId);
	}
	
	/**
	 * Returns value of the given light sensor if registered in percentage form.
	 * 
	 * @param lightSensorId given light sensor to be checked.
	 * @return value of the given light sensor in percentage form.
	 * @throws IllegalArgumentException if sensor is not registered.
	 */
	protected double getLightSensorPercentValue(LightSensorRegistry lightSensorId)
	throws IllegalArgumentException {
		return getLightSensorValue(lightSensorId) / MAX_LIGHT_VALUE;
	}

    /**
     * Returns all light sensors.
     *
     * @return Collection<LightSensor> all light sensors.
     */
    protected Collection<LightSensor> getAllLightSensors() {
        return _lightSensors.values();
    }

	/**
	 * Returns value of the given distance sensor if registered.
	 * 
	 * @param distanceSensorId given distance sensor to be checked.
	 * @return value of the given distance sensor.
	 * @throws IllegalArgumentException if sensor is not registered.
	 */
	protected double getDistanceSensorValue(DistanceSensorRegistry distanceSensorId)
	throws IllegalArgumentException {
        if (distanceSensorId != null) {
            DistanceSensor dSensor = _distanceSensors.get(distanceSensorId);

            if (dSensor != null) return dSensor.getValue();
        }
        throw new IllegalArgumentException("Unknown sensor lookup detected - " + distanceSensorId);
	}

    /**
     * Returns value of the given distance sensor if registered in percentage form.
     *
     * @param distanceSensorId given distance sensor to be checked.
     * @return value of the given distance sensor in percentage form.
     * @throws IllegalArgumentException if sensor is not registered.
     */
    protected double getDistanceSensorPercentValue(DistanceSensorRegistry distanceSensorId)
    throws IllegalArgumentException {
        return getDistanceSensorValue(distanceSensorId) / MAX_DISTANCE_VALUE;
    }

	/**
	 * Returns all distance sensors.
	 * 
	 * @return all distance sensors.
	 */
	protected Collection<DistanceSensor> getAllDistanceSensors() {
        return _distanceSensors.values();
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
