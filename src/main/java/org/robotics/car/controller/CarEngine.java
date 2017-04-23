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
 * <p>
 * Enhanced for the use in the Innovation Lab at TL for educational purposes.
 *
 * @author Roger Ruttimann
 */
public class CarEngine {

    // Constants
    static final int DEFAULT_LOOP_TIME = 300; // 1 sec

    // Minimal distance between sensor and object in cm to do an action
    static float MINIMAL_DISTANCE = 10;
    static int SLEEPING_TIME_BETWEEN_LOOP = 500; // in ms
    static int SAMPLESIZE = 3; //samplesize for average calculation
    static int DEGREES_TO_TURN = 90;


    // create gpio controller instance
    static final GpioController gpio = GpioFactory.getInstance();

    static MotorController motorController = null;

    // LED instances
    /* static final GpioPinDigitalOutput ledGreen = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "CarEngine", PinState.LOW);
    static final GpioPinDigitalOutput ledBlue = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "CarForward", PinState.LOW);
    static final GpioPinDigitalOutput ledYellow = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "CarReverse", PinState.LOW);
*/
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods
    //
    // main() --  Entry point for the robotics process
    //
    public static void main(String[] args) throws InterruptedException {

        String terrain = "concrete";

        // Create Motor controller
        motorController = new MotorController(terrain, "M1", "M2", "M3", "M4");

                 /*
                 * Because the Adafruit motor HAT uses PWMs that pulse independently of
                 * the Raspberry Pi the motors will keep running at its current direction
                 * and power levels if the program abnormally terminates.
                 * A shutdown hook like the one in this example is useful to stop the
                 * motors when the program is abnormally interrupted.
                 */
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Turn off all motors");
                motorController.motorHat.stopAll();
            }
        });

        // ust for testing
        String fronLeftMotor = "M1";





        // Initialize Sensors, motor controller
        // Create an instance of the Sonic Sensor using GPIO Pin #4 for ECHO signal and GPIO Pin # 5 for TRIGGER
        UltraSonicHCSR04 rightSensor = new UltraSonicHCSR04("Right sensor", 27, 24);
        UltraSonicHCSR04 leftSensor = new UltraSonicHCSR04("Left sensor", 7, 0);
        UltraSonicHCSR04 frontright = new UltraSonicHCSR04("Front Right sensor", 5, 6);
        UltraSonicHCSR04 frontmiddle = new UltraSonicHCSR04("Front Middle sensor", 3, 4);
        UltraSonicHCSR04 frontleft = new UltraSonicHCSR04("Front Left sensor", 1, 2);

        // Start measurement for each sensor
        try {
            rightSensor.start();
            Thread.sleep(163);
            leftSensor.start();
            Thread.sleep(157);
            frontright.start();
            Thread.sleep(149);
            frontmiddle.start();
            Thread.sleep(140);
            frontleft.start();
            Thread.sleep(131);


            while (frontright.getDistance() == 0) {
                Thread.sleep(350);
            }
        } catch (InterruptedException ie) {
            System.out.println("Sleep interruppted");
        }

        System.out.println("Sensors initialized and ready to measure distances...");

        // local variables
        float frontRightMeasuredDistance = 0;
        float frontLeftMeasuredDistance = 0;
        float frontMiddleMeasuredDistance = 0;
        float leftMeasuredDistance = 0;
        float rightMeasuredDistance = 0;

        System.out.println("Starting car Engine vrooom...");

        // Car ENgine started turn on Green LED
//        ledGreen.high();

        // Car is not moving turn off other LED's
  //      ledBlue.low();
    //    ledYellow.low();


        System.out.println("Autonomous is Online and Ready to Go");
        try {
            // Run forever
            while (true) {
                // Read all the measurements
                frontRightMeasuredDistance = frontright.getDistance();
                frontLeftMeasuredDistance = frontleft.getDistance();
                frontMiddleMeasuredDistance = frontmiddle.getDistance();
                leftMeasuredDistance = leftSensor.getDistance();
                rightMeasuredDistance = rightSensor.getDistance();

                System.out.println("Front Right Sensor: " + frontRightMeasuredDistance);
                System.out.println("Front Middle Sensor:" + frontMiddleMeasuredDistance);
                System.out.println("Front Left Sensor:  " + frontLeftMeasuredDistance);
                System.out.println("Left Sensor:" + leftMeasuredDistance);
                System.out.println("Right Sensor:  " + rightMeasuredDistance);


                System.out.println("Autonomous : Read distance now decides what to do ...");
                //Logic table

                if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.forward();
                    System.out.println("None blocked, move forward");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.right(45);
                    System.out.println("FrontLeft blocked, turn right 45 degrees");

                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.left(45);
                    System.out.println("FrontMiddle blocked, turn left 45 degrees");

                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.left(45);
                    System.out.println("FrontRight blocked, turn left 45 degrees");

                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.forward();
                    System.out.println("Left blocked, move forward");

                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.forward();
                    System.out.println("Right blocked, move forward");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.right(90);
                    System.out.println("FrontLeft and FrontMiddle blocked, turn right 90 degrees");

                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.left(90);
                    System.out.println("FrontMiddle and FrontRight blocked, turn left 90 degrees");
//check in
                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.right(20);
                    System.out.println("Left and FrontRight blocked, turn right 20 degrees");

                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.forward();
                    System.out.println("Left and Right blocked, move forward");
//check in
                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.right(20);
                    System.out.println("FrontLeft and Right blocked, turn left 20 degrees");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.left(90);
                    System.out.println("FrontLeft and FrontRight blocked, turn left 90 degrees");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.left(180);
                    System.out.println("All blocked, turn left 180 degrees");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.left(90);
                    System.out.println("FrontLeft, FrontMiddle, FrontRight blocked, turn left 90 degrees");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.left(180);
                    System.out.println("FrontLeft, FrontMiddle, Left, and Right blocked, turn left 180 degrees");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.left(90);
                    System.out.println("FrontLeft, FrontMiddle, FrontRight, and Right blocked, turn left 90 degrees");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.right(90);
                    System.out.println("FrontLeft, FrontMiddle, FrontRight, and Left blocked, turn right 90 degrees");

                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.left(180);
                    System.out.println("FrontMiddle, FrontRight, Left, and Right blocked, turn left 180 degrees");

                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.right(90);
                    System.out.println("FrontMiddle, Front Right, and Left blocked, turn right 90 degrees");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.left(90);
                    System.out.println("FrontLeft, FrontMiddle, and Right blocked, turn left 90 degrees");

                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.left(20);
                    System.out.println("FrontRight, Left, Right blocked: turn left 20 degrees");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.right(20);
                    System.out.println("FrontLeft, Left, Right blocked: turn right 20 degrees");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.left(180);
                    System.out.println("FrontMiddle, FrontRight, Left, Right blocked: turn left 180 degrees");

                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.left(180);
                    System.out.println("FrontMiddle, FrontRight, Left, Right blocked: turn left 180 degrees");
                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.left(110);
                    System.out.println("FrontLeft, FrontRight, Left, blocked: turn left 110 degrees");
                } else if ((frontLeftMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.right(110);
                    System.out.println("FrontLeft, FrontMiddle, Right blocked: turn left 180 degrees");
                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance < MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance > MINIMAL_DISTANCE)
                        && (leftMeasuredDistance < MINIMAL_DISTANCE)
                        && (rightMeasuredDistance > MINIMAL_DISTANCE)) {
                    motorController.right(45);
                    System.out.println("Front Middle, Left blocked, turn right 45 degrees");
                } else if ((frontLeftMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontMiddleMeasuredDistance > MINIMAL_DISTANCE)
                        && (frontRightMeasuredDistance < MINIMAL_DISTANCE)
                        && (leftMeasuredDistance > MINIMAL_DISTANCE)
                        && (rightMeasuredDistance < MINIMAL_DISTANCE)) {
                    motorController.left(45);
                    System.out.println("Right Front Middle, Right blocked, turn right 45 degrees");


                    System.out.println("Autonomous: Motor and directions updated -- get to next");
                }

            }
        } catch (Exception ex)

        {
            System.out.println("Encountered Exception: " + ex);
        }

        // Stop car
        motorController.motorHat.stopAll();

        // Stop sensors
        frontright.shutdown();
        frontmiddle.shutdown();
        frontleft.shutdown();
        leftSensor.shutdown();
        rightSensor.shutdown();


        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
        System.out.println("Exiting ControlGpioExample");
    }
}
