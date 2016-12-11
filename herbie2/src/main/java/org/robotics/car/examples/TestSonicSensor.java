
/*
 *
 * Copyright 2016 Autonomous Open Source Project
 *
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 *
 *         Author: Maurice Ruttimann
 *
 */
package org.robotics.car.examples;

// Classes developed to use the different sensors
import org.robotics.car.exception.TimeoutException;
import org.robotics.car.sensors.UltraSonicHCSR04;

/**
 * Created by maurice on 12/10/16.
 */
public class TestSonicSensor {

    public static void main(String ars[]) throws Exception {

        // Create an instance of the Sonic Sensor using GPIO Pin #4 for ECHO signal and
        UltraSonicHCSR04 frontSensor = new UltraSonicHCSR04("Sonic sensor", 2, 3);
        float distance =0;
        for (int i=0; i < 20; i++) {

            System.out.println("Measure distance...");

            try {
                distance = frontSensor.measureDistanceAverage(5);
                System.out.println("Measured distance: " + distance + " cm");

            } catch (TimeoutException te) {
                System.out.println("Timeout from sensor " + te);
            }


            Thread.sleep(1000);
        }

    }
}
