package impl;

import api.IClassifiable;
import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.LightSensor;
import util.DistanceSensorRegistry;
import util.LightSensorRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sereGkaluv on 12-Dec-15.
 */
public abstract class RobotController extends DifferentialWheels implements Runnable {
    public static final int MAX_LIGHT_VALUE = 4095;
    public static final int MAX_DISTANCE_VALUE = 3500;

    public static final int STANDBY_MOTOR_SPEED = 0;
    public static final int MAX_ABSOLUTE_MOTOR_SPEED = 1000;
    public static final int MAX_FORWARD_MOTOR_SPEED = MAX_ABSOLUTE_MOTOR_SPEED;
    public static final int MAX_BACKWARD_MOTOR_SPEED = -MAX_ABSOLUTE_MOTOR_SPEED;

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
            LightSensor sensor = new LightSensor(sensorId.getNamingConstant());
            sensor.enable(_lightSensorFrequency);

            _lightSensors.put(sensorId, sensor);
        }

        //initializing distance sensors
		_distanceSensors = new HashMap<>();
        _distanceSensorFrequency = distanceSensorFrequency;

        for (DistanceSensorRegistry sensorId : DistanceSensorRegistry.values()) {
            DistanceSensor sensor = new DistanceSensor(sensorId.getNamingConstant());
            sensor.enable(_distanceSensorFrequency);

            _distanceSensors.put(sensorId, sensor);
        }
    }
	
	/**
	 * Converts given relative speed to real speed.
	 * 
	 * @return real speed value.
	 */
	protected double convertRelativeToRealSpeed(double relativeSpeed) {
		return relativeSpeed * MAX_ABSOLUTE_MOTOR_SPEED;
	}

    /**
     * Returns value in percent form of the given sensor if registered.
     *
     * @param sensorId given sensor to be checked.
     * @return percent value of the given sensor.
     * @throws IllegalArgumentException if sensor is not registered.
     */
    public double getSensorPercentValueFor(IClassifiable sensorId)
    throws IllegalArgumentException {
        if (sensorId != null) {
            if (sensorId instanceof LightSensorRegistry) {
                return getLightSensorPercentValueFor((LightSensorRegistry) sensorId);
            }

            if (sensorId instanceof DistanceSensorRegistry) {
                return getDistanceSensorPercentValueFor((DistanceSensorRegistry) sensorId);
            }
        }
        throw new IllegalArgumentException("Unknown sensor lookup detected - " + sensorId);
    }

	/**
	 * Returns value of the given light sensor if registered.
	 *
	 * @param lightSensorId given light sensor to be checked.
	 * @return value of the given light sensor.
	 * @throws IllegalArgumentException if sensor is not registered.
	 */
    public double getLightSensorValueFor(LightSensorRegistry lightSensorId)
	throws IllegalArgumentException {
		if (lightSensorId != null) {
			LightSensor lightSensor = _lightSensors.get(lightSensorId);

            //If sensor is registered return real value (value >= 0);
			if (lightSensor != null) return Math.max(0, lightSensor.getValue());
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
    public double getLightSensorPercentValueFor(LightSensorRegistry lightSensorId)
	throws IllegalArgumentException {
		return translateToLightPercent(getLightSensorValueFor(lightSensorId));
	}

    /**
     * Returns all light sensors.
     *
     * @return Collection<LightSensor> all light sensors.
     */
    public Collection<LightSensor> getAllLightSensors() {
        return _lightSensors.values();
    }

	/**
	 * Returns value of the given distance sensor if registered.
	 * 
	 * @param distanceSensorId given distance sensor to be checked.
	 * @return value of the given distance sensor.
	 * @throws IllegalArgumentException if sensor is not registered.
	 */
    public double getDistanceSensorValueFor(DistanceSensorRegistry distanceSensorId)
	throws IllegalArgumentException {
        if (distanceSensorId != null) {
            DistanceSensor distanceSensor = _distanceSensors.get(distanceSensorId);

            //If sensor is registered return real value (value >= 0);
            if (distanceSensor != null) return Math.max(0, distanceSensor.getValue());
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
    public double getDistanceSensorPercentValueFor(DistanceSensorRegistry distanceSensorId)
    throws IllegalArgumentException {
        return translateToDistancePercent(getDistanceSensorValueFor(distanceSensorId));
    }

	/**
	 * Returns all distance sensors.
	 *
	 * @return all distance sensors.
	 */
    public Collection<DistanceSensor> getAllDistanceSensors() {
        return _distanceSensors.values();
    }
	
	/**
	 * Returns remaining percentage, e.g. if given value will be 60 returned value will be 40.
	 * 
	 * @param actualPercentage given percentage
	 * @return remaining percentage
	 */
    public static double getRemainingPercentage(double actualPercentage) {
		if (actualPercentage > 0) {
			return 1 - actualPercentage;
		} else if (actualPercentage < 0) {
			return -1 - actualPercentage;
		}
		return 0;
	}

    /**
     * Translates common light to percent value.
     *
     * @param value to be translated to percent.
     * @return translated (percent) value.
     */
    public static double translateToLightPercent(double value) {
        return value / MAX_LIGHT_VALUE;
    }

    /**
     * Translates common distance to percent value.
     *
     * @param value to be translated to percent.
     * @return translated (percent) value.
     */
    public static double translateToDistancePercent(double value) {
        return value / MAX_DISTANCE_VALUE;
    }
}
