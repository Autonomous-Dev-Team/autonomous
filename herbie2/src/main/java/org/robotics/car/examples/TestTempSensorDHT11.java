package org.robotics.car.examples;

/**
 * Created by rogerrut on 12/3/16.
 */

import org.robotics.car.sensors.data.DataMeasured;
import org.robotics.car.sensors.data.Temperature;
import org.robotics.car.sensors.TemperatureDHT11;

public class TestTempSensorDHT11 {


    public static void main(String ars[]) throws Exception {

        TemperatureDHT11 dht = new TemperatureDHT11();
        Temperature measurement = null;

        // Measure the temperatur for 10 times
        for (int i = 0; i < 10; i++) {
            measurement = dht.getTemperature();
            if (measurement !=null)
                System.out.println("Humidity = " + measurement.getValue(DataMeasured.HUMIDITY) + " Temperature = " + measurement.getValue(DataMeasured.TEMPERATUR_CELSIUS) + "(" + measurement.getValue(DataMeasured.TEMPERATUR_FAHRENHEIT) + "f)");
            else
                System.out.println("No value received. Make sure sensor is connected");

            Thread.sleep(2000);
        }

        System.out.println("Done!!");

    }
}
