package org.robotics.car.controller;

/**
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  ControlGpioExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

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
	static final int DEFAULT_LOOP_TIME = 1000; // 1 sec

    // Minimal distance between sensor and object to do an action
    static float MINIMAL_DISTANCE = 15;
    static int SLEEPING_TIME_BETWEEN_LOOP = 500; // in ms
    static int SAMPLESIZE = 3; //samplesize for average calculation
    static int DEGREES_TO_TURN = 90;


    // create gpio controller instance
    static final GpioController gpio = GpioFactory.getInstance();

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
        UltraSonicHCSR04 frontSensor = new UltraSonicHCSR04("Sonic sensor", 4, 5);
        UltraSonicHCSR04 leftSensor = new UltraSonicHCSR04("Sonic sensor", 6, 7);
        UltraSonicHCSR04 rightSensor = new UltraSonicHCSR04("Sonic sensor", 8, 9);

        // Create Motor controller
       MotorController motorController = new MotorController();

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

        while (true) {

            try {
                System.out.println("Herbie Online and Ready to Go");

                Thread.sleep(SLEEPING_TIME_BETWEEN_LOOP);
                //read all the sensors
                frontmeasuredDistance = frontSensor.measureDistanceAverage(SAMPLESIZE);
                leftmeasuredDistance = leftSensor.measureDistanceAverage(SAMPLESIZE);
                rightmeasuredDistance = rightSensor.measureDistanceAverage(SAMPLESIZE);

                if (frontmeasuredDistance > MINIMAL_DISTANCE) {
                    // Motor forward
                    System.out.println("Front clear move forward");
                    motorController.forward("HIGH");
                } else if (leftmeasuredDistance > MINIMAL_DISTANCE) {
                    //motor left
                    System.out.println("Right clear turn Right");
                    motorController.left(DEGREES_TO_TURN);
                    System.out.println("Front clear move forward");
                    motorController.forward("HIGH");
                } else if (rightmeasuredDistance > MINIMAL_DISTANCE) {
                    //motor right
                    System.out.println("Right clear turn Right");
                    motorController.right(DEGREES_TO_TURN);
                    System.out.println("Front clear move forward");
                    motorController.forward("HIGH");
                } else if ((leftmeasuredDistance > MINIMAL_DISTANCE) && (rightmeasuredDistance > MINIMAL_DISTANCE)) {
                    System.out.println("Left clear turn left");
                    motorController.left(DEGREES_TO_TURN);
                    System.out.println("Front clear move forward");
                    motorController.forward("HIGH");

                } else if (    (frontmeasuredDistance <= MINIMAL_DISTANCE)
                            && (leftmeasuredDistance <= MINIMAL_DISTANCE)
                            && (rightmeasuredDistance <= MINIMAL_DISTANCE)) {
                    //motor backward
                    System.out.println("All Blocked Going Backward");
                    motorController.backward("HIGH");
                    // Since there is no sensor in the back, I want to tell the robot to check all the sensors again and wait until it is clear then
                    // turn left or right
                    boolean stopmoving = false;
                    while (stopmoving == false) {
                        leftmeasuredDistance = leftSensor.measureDistance();
                        rightmeasuredDistance = rightSensor.measureDistance();
                        if (leftmeasuredDistance > MINIMAL_DISTANCE) {
                            motorController.stop();
                            System.out.println("Left clear turn left");
                            motorController.left(DEGREES_TO_TURN);
                            System.out.println("Front clear move forward");
                            motorController.forward("HIGH");
                            // Quit loop
                            stopmoving = true;
                        } else if (rightmeasuredDistance > MINIMAL_DISTANCE) {
                            System.out.println("Right clear turn Right");
                            motorController.right(DEGREES_TO_TURN);
                            System.out.println("Front clear move forward");
                            motorController.forward("HIGH");
                            stopmoving = true;
                        }
                    }
                }
            } catch (TimeoutException te) {
                System.out.println("Sensor time out -- stop car");
                motorController.stop();
                break;
            }
        }

		// stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
        System.out.println("Exiting ControlGpioExample");
    }
}
