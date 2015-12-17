import api.Classifiable;
import entity.Wheel;
import impl.RobotController;
import util.WheelRegistry;

import java.util.Map;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public abstract class ProportionalController<T extends Enum<? extends Classifiable>> extends RobotController {
    private static final double DEFAULT_FORWARD_NORMALIZER = 1;
    private static final double DEFAULT_BACKWARD_NORMALIZER = -1;
    private final double _constant;
    private final Map<WheelRegistry, Wheel<T>> _wheels;
		
	public ProportionalController(
        int lightSensorFrequency,
        int distanceSensorFrequency,
        double constant,
        Map<WheelRegistry, Wheel<T>> wheels
    ) {
		super(lightSensorFrequency, distanceSensorFrequency);

		_constant = constant;
        _wheels = wheels;
	}

    protected abstract double getSensorValue(T sensorId);

    @Override
    protected abstract T[] getRegisteredSensors();

    @Override
    protected Map<WheelRegistry, Wheel<T>> calculateMovementSpeed()
    throws IllegalArgumentException {

        T[] sensors = getRegisteredSensors();

        //Calculating raw relative speed value for each wheel
        for (T sensor : sensors) {
            double value = getSensorValue(sensor);

            for (Wheel<T> wheel : _wheels.values()) {
                double sensorVelocity = wheel.getVelocityForSensor(sensor);
                double relativeSpeed = wheel.getRelativeSpeed();

                if (sensorVelocity != 0) relativeSpeed += sensorVelocity * value;
                relativeSpeed += _constant;

                wheel.setRelativeSpeed(relativeSpeed);
            }
        }

        double forwardNormalizer = DEFAULT_FORWARD_NORMALIZER;
        double backwardNormalizer = DEFAULT_BACKWARD_NORMALIZER;

        int i = 0;
        //Defining normalization values
        for (Wheel<T> wheel : _wheels.values()) {
            double relativeSpeed = wheel.getRelativeSpeed();

            System.out.println(i++ + " - " + relativeSpeed);

            if (relativeSpeed > forwardNormalizer) forwardNormalizer = relativeSpeed;
            else if (relativeSpeed < backwardNormalizer) backwardNormalizer = relativeSpeed;
        }

        //Calculating absolute vales, so normalizer value will not affect movement direction
        forwardNormalizer = Math.abs(forwardNormalizer);
        backwardNormalizer = Math.abs(backwardNormalizer);

        //Normalizing speed value for each wheel
        for (Wheel<T> wheel : _wheels.values()) {
            double relativeSpeed = wheel.getRelativeSpeed();

            if (relativeSpeed > 0) wheel.setRelativeSpeed(relativeSpeed / forwardNormalizer);
            else if (relativeSpeed < 0) wheel.setRelativeSpeed(relativeSpeed / backwardNormalizer);
        }
        
        return _wheels;
	}
}
