
package org.robotics.car.examples;

/*
 *  Copyright 2018 Autonomous Open Source Project
 *
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

// Classes developed to use the different sensors
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.util.Console;
import org.robotics.car.sensors.LidarLite;

/**
 * Created by maurice on 12/10/16.
 */
public class TestLidarSensor {

    public static void main(String ars[]) throws Exception {

        /**
         * Wireing for the sensor for running the test and production
         * Black: GND
         * Red: +5V
         * Blue: Pin 3 SDA
         * Green: Pin 5 SCL
         * Orange / Yellow unused
         *
         */
        final Console console = new Console();
        console.title("<-- TL Maker Lab Lidar Sensor Test program");

        LidarLite laserSensor = new LidarLite();

        if (laserSensor.init(I2CBus.BUS_1, 0x62)) {
            console.println("Lidar Lite 3 Initialization sucessful");
            console.println("Writing and Reading to LIDAR to get the distance");

            // Start measurements
            for (int i=0; i<20; i++) {

                console.println("Measurement [" + i + "]");
                console.println("Distance: " + laserSensor.getMeasurement());
                Thread.sleep(1000);
            }

            // Done with measurements
            console.println("Power down sensor");
            laserSensor.uninitialize();

        }
        else
        {
            console.println("Laser Sensor init failed. Correct error before proceed.");
        }
    }
}
