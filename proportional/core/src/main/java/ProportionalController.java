import api.IClassifiable;
import entity.Wheel;
import impl.RobotController;
import util.WheelRegistry;

import java.util.Map;

/**
 * Created by sereGkaluv on 16-Dec-15.
 */
public abstract class ProportionalController extends RobotController {
    private static final double DEFAULT_ZERO_NORMALIZER = 0;

    private final Map<WheelRegistry, Wheel> _wheels;
    private final int _distanceSensorFrequency;

	public ProportionalController(
        int lightSensorFrequency,
        int distanceSensorFrequency,
        Map<WheelRegistry, Wheel> wheels
    ) {
		super(lightSensorFrequency, distanceSensorFrequency);

        _distanceSensorFrequency = distanceSensorFrequency;
        _wheels = wheels;
	}

    @Override
    public void run() {
        while (step(_distanceSensorFrequency) != -1) {
            Map<WheelRegistry, Wheel> wheelMap = calculateMovementSpeed();

            double relativeLeftSpeed = wheelMap.get(WheelRegistry.LEFT).getRelativeSpeed();
            double relativeRightSpeed = wheelMap.get(WheelRegistry.RIGHT).getRelativeSpeed();

            setSpeed(
                convertRelativeToRealSpeed(relativeLeftSpeed),
                convertRelativeToRealSpeed(relativeRightSpeed)
            );
        }
    }

    /**
     * Modifies sensor percent value according to the current implementation.
     *
     * @param sensorPercentValue percent value to be modified.
     * @return modified percent value.
     */
    protected abstract double modifySensorPercentValue(double sensorPercentValue);

    /**
     * Returns array of all sensors required for concrete task.
     *
     * @return array of sensors.
     */
    protected abstract IClassifiable[] getRegisteredSensors();

    /**
     * Calculates movement speed for each given wheel at the moment of method call.
     *
     * @return instance of WheelContainer object.
     * @throws IllegalArgumentException;
     */
    protected Map<WheelRegistry, Wheel> calculateMovementSpeed()
    throws IllegalArgumentException {

        IClassifiable[] sensors = getRegisteredSensors();

        //Calculating raw relative speed value for each wheel
        for (Wheel wheel : _wheels.values()) {
            double relativeSpeed = 0;

            for (IClassifiable sensor : sensors) {
                double sensorValue = modifySensorPercentValue(getSensorPercentValueFor(sensor));
                double controllerValue = wheel.getControllerValueFor(sensor);
                double constantValue = wheel.getConstantFor(sensor);

                if (controllerValue != 0) relativeSpeed += controllerValue * sensorValue;
                relativeSpeed += constantValue;
            }

            System.out.print(wheel.getRelativeSpeed() + "  ");
            wheel.setRelativeSpeed(relativeSpeed);
        }

        System.out.println();

        return normalizeSpeed(_wheels);
	}

    /**
     * Normalizes speed - searches for the biggest speed value in a given collection.
     * After that assumes that the biggest value == 100% (MAX_SPEED).
     * All other speed values are divided by the biggest value, result of this action
     * will be a relative speed value (per Wheel). Possible value range (from -1 to 1).
     *
     * @param wheels Collection of wheels to be normalized.
     * @return Collection of wheels with normalized speed.
     */
    protected Map<WheelRegistry, Wheel> normalizeSpeed(Map<WheelRegistry, Wheel> wheels) {
        double forwardMaxNormalizer = DEFAULT_ZERO_NORMALIZER;
        double backwardMaxNormalizer = DEFAULT_ZERO_NORMALIZER;

        //Defining normalization values
        for (Wheel wheel : wheels.values()) {
            double relativeSpeed = wheel.getRelativeSpeed();

            if (relativeSpeed > forwardMaxNormalizer) forwardMaxNormalizer = relativeSpeed;
            else if (relativeSpeed < backwardMaxNormalizer) backwardMaxNormalizer = relativeSpeed;
        }

        //Calculating absolute vales, so normalizer value will not affect movement direction
        forwardMaxNormalizer = Math.abs(forwardMaxNormalizer);
        backwardMaxNormalizer = Math.abs(backwardMaxNormalizer);

        //Normalizing speed value for each wheel
        for (Wheel wheel : wheels.values()) {
            double relativeSpeed = wheel.getRelativeSpeed();

            if (relativeSpeed > 0) wheel.setRelativeSpeed(relativeSpeed / forwardMaxNormalizer);
            else if (relativeSpeed < 0) wheel.setRelativeSpeed(relativeSpeed / backwardMaxNormalizer);
        }

        return wheels;
    }
}
