package org.robotics.car.examples;

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
