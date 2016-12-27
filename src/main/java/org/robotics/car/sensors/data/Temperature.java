package org.robotics.car.sensors.data;

import org.robotics.car.sensors.SensorType;
import org.robotics.car.sensors.data.DataMeasured;
import org.robotics.car.sensors.data.Measurement;

/**
 * DTO for measurements
 */

public class Temperature extends Measurement {

    // Temeprature sensors will provide humidity, temperature in Celsius and Fahrenheit
    private float temperature_c = 0;
    private float temperature_f = 0;
    private float humidity = 0;

    private static String TEMP_MEASUREMENT_NAME = "Temperature-Humidity-Sensor";
    private static String TEMP_MEASUREMENT_DESCRIPTION = "Temeprature and humidity measurement sensor";

    // Constructor takes measurements
    public Temperature(float temp_c, float temp_f, float humidity) {
        super(TEMP_MEASUREMENT_NAME, SensorType.TEMPERATURE, TEMP_MEASUREMENT_DESCRIPTION);
        this.humidity = humidity;
        this.temperature_c = temp_c;
        this.temperature_f = temp_f;
    }

    /**
     * Temperature secific data points
     * @param type What type of data point you expect back
     * @return
     */
    public float getValue(DataMeasured type) {

        switch( type) {
            case HUMIDITY:
                return this.humidity;
            case TEMPERATUR_CELSIUS:
                return this.temperature_c;
            case TEMPERATUR_FAHRENHEIT:
                return this.temperature_f;
            default:
                throw new IllegalArgumentException("Measurement of type " +type + " is not available");
        }
    }
}
