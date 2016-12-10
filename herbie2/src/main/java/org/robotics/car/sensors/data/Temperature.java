package org.robotics.car.sensors.data;
/*
Copyright 2016 Autonomous Open Source Project

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.

        Author: Roger Ruttimann
*/
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
