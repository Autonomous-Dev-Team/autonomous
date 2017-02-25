/***********************************************
 * Copyright 2016 Autonomous Open Source Project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.robotics.car.controller;


// Imports for the RaspberryPI libraries that give access to the hardware
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import org.robotics.car.exception.TimeoutException;
// Classes developed to use the different sensors
import org.robotics.car.sensors.UltraSonicHCSR04;


/**
 * This code was based on the sample code from Robert Savage member of the Pi4j team
 * 
 * Enhanced for the use in the Innovation Lab at TL for educational purposes.
 * 
 * @author Roger Ruttimann
 * 
 */
public class CarEngine {
	
	// Constants
	static final int DEFAULT_LOOP_TIME = 300; // 1 sec

    // Minimal distance between sensor and object to do an action
    static float MINIMAL_DISTANCE = 20;
    static int SLEEPING_TIME_BETWEEN_LOOP = 500; // in ms
    static int SAMPLESIZE = 3; //samplesize for average calculation
    static int DEGREES_TO_TURN = 90;


    // create gpio controller instance
    static final GpioController gpio = GpioFactory.getInstance();

    static MotorController motorController = null;

    // LED instances
    static final GpioPinDigitalOutput ledGreen	= gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "CarEngine", PinState.LOW);
    static final GpioPinDigitalOutput ledBlue	= gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "CarForward", PinState.LOW);
    static final GpioPinDigitalOutput ledYellow	= gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "CarReverse", PinState.LOW);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods
    //
    // main() --  Entry point for the robotics process
    //
    public static void main(String[] args) throws InterruptedException {

        // Initialize Sensors, motor controller
        // Create an instance of the Sonic Sensor using GPIO Pin #4 for ECHO signal and GPIO Pin # 5 for TRIGGER
        UltraSonicHCSR04 frontSensor = new UltraSonicHCSR04("Front sensor", 5, 6, 3);
        UltraSonicHCSR04 leftSensor = new UltraSonicHCSR04("Left sensor", 0, 2, 3);
        UltraSonicHCSR04 rightSensor = new UltraSonicHCSR04("Right sensor", 4, 3, 3);

        // Start measurement for each sensor
        try {
            frontSensor.start();
            Thread.sleep(163);
            leftSensor.start();
            Thread.sleep(157);
            rightSensor.start();
            Thread.sleep(149);

            while (rightSensor.getDistance() == 0) {
                Thread.sleep(350);
            }
        } catch (InterruptedException ie) {
            System.out.println("Sleep interruppted");
        }


        System.out.println("Sensors initialized and ready to measure distances...");


        // Create Motor controller
        motorController = new MotorController();

        // local variables
        float frontmeasuredDistance = 0;
        float leftmeasuredDistance = 0;
        float rightmeasuredDistance = 0;

        System.out.println("Starting car Engine vrooom...");

        // Car ENgine started turn on Green LED
       ledGreen.high();

        // Car is not moving turn off other LED's
        ledBlue.low();
        ledYellow.low();

        /**
         * Pseudo code to run the car:
         *
         * while forever
         * sleep 500ms
         * CHeck as following
         * 1) Check front distance (SensorFront)
         * 2) If distance less than 15 cm Motor forward
         * 2.1 else check sensor Left, check Sensor Right
         * 2.2 if right clear and left blocked turn Motor left
         * 2.3 if left clear and right clear turn Motor right
         * 2.4 if both clear turn Motor left
         * 2.5 if both blocked  Motor reverse
         *
         * repeat
         *
         *
         */

        System.out.println("Herbie Online and Ready to Go");
        while (true) {

            try {

                Thread.sleep(SLEEPING_TIME_BETWEEN_LOOP);
                //read all the sensors
                System.out.println("Measure distance ..");

                //frontmeasuredDistance = frontSensor.measureDistanceAverage(SAMPLESIZE);
                frontmeasuredDistance = (float) frontSensor.getDistance();
                System.out.println("Front measurement: " + frontmeasuredDistance);
                //leftmeasuredDistance = leftSensor.measureDistanceAverage(SAMPLESIZE);
                leftmeasuredDistance = (float) leftSensor.getDistance();
                System.out.println("Left measurement: " + leftmeasuredDistance);
                //rightmeasuredDistance = rightSensor.measureDistanceAverage(SAMPLESIZE);
                rightmeasuredDistance = (float) rightSensor.getDistance();
                System.out.println("Right measurement: " + rightmeasuredDistance);

                //System.out.println("Measured: Front " + frontmeasuredDistance + " Left Side " + leftmeasuredDistance + " Right Side " + rightmeasuredDistance);

                if (frontmeasuredDistance > MINIMAL_DISTANCE) {
                    // Motor forward
                    System.out.println("Front clear move forward");
                    motorController.forward("MEDIUM");
                } else if (frontmeasuredDistance < MINIMAL_DISTANCE &&
                        leftmeasuredDistance > MINIMAL_DISTANCE &&
                        rightmeasuredDistance > MINIMAL_DISTANCE) {
                    System.out.println("Front blocker Left clear, right clear turn left");
                    motorController.left(DEGREES_TO_TURN);
                    motorController.forward("MEDIUM");
                } else if (frontmeasuredDistance < MINIMAL_DISTANCE &&
                           leftmeasuredDistance < MINIMAL_DISTANCE &&
                           rightmeasuredDistance > MINIMAL_DISTANCE) {
                    System.out.println("Front blocked Left blocked, right clear turn right");
                    motorController.right(DEGREES_TO_TURN);
                    System.out.println("Front clear move forward");
                    motorController.forward("MEDIUM");
                } else if (frontmeasuredDistance < MINIMAL_DISTANCE &&
                            leftmeasuredDistance > MINIMAL_DISTANCE &&
                            rightmeasuredDistance < MINIMAL_DISTANCE) {
                    System.out.println("Front blocked Left clear, right blocked turn left");
                    motorController.left(DEGREES_TO_TURN);
                    motorController.forward("MEDIUM");
                } else if (frontmeasuredDistance < MINIMAL_DISTANCE &&
                           leftmeasuredDistance < MINIMAL_DISTANCE &&
                           rightmeasuredDistance < MINIMAL_DISTANCE) {
                    System.out.println("Front blocked Left blocked, right blocked going backwards");
                    motorController.backward("MEDIUM");

                    Thread.sleep(200);

                    boolean stopmoving = false;
                    while (stopmoving == false) {

                        // Sensors
                        leftmeasuredDistance = leftSensor.measureDistanceAverage(SAMPLESIZE);
                        rightmeasuredDistance = rightSensor.measureDistanceAverage(SAMPLESIZE);

                        // If any of sensor is unblocked continue with loop
                        if ((leftmeasuredDistance > MINIMAL_DISTANCE) || (rightmeasuredDistance > MINIMAL_DISTANCE)) {
                            stopmoving = true;

                            if (leftmeasuredDistance > MINIMAL_DISTANCE)
                                motorController.left(DEGREES_TO_TURN);
                            else
                                motorController.right(DEGREES_TO_TURN);
                        }
                    }
                }

            } catch (TimeoutException te) {
                System.out.println("Sensor time out -- stop car");
                motorController.stop();
                break;
            }
        }

        // Stop sensors
        frontSensor.shutdown();
        leftSensor.shutdown();
        rightSensor.shutdown();


		// stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
        System.out.println("Exiting ControlGpioExample");
    }
}
