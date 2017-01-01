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

import com.pi4j.component.adafruithat.AdafruitDcMotor;
import com.pi4j.component.adafruithat.AdafruitMotorHat;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by rogerrut on 11/24/16.
 */
public class MotorController {

    // Constant
    // private double TIME_FOR_ONE_DEGREE_TURN = 16.667; //milli seconds per degree
    private double TIME_FOR_ONE_DEGREE_TURN = 15; //milli seconds per degree

    static private float DEFAULT_POWER_LEVEL = 30.0f;
    static private float DEFAULT_SPEED = 20.0f;

    static private float LOW = DEFAULT_SPEED/2;
    static private float MEDIUM = DEFAULT_SPEED;
    static private float HIGH = 30.0f;

    final int motorHATAddress = 0X60;
    private AdafruitMotorHat motorHat = null;

    private static AdafruitDcMotor motorBackRight  = null;
    private static AdafruitDcMotor motorBackLeft   = null;
    private static AdafruitDcMotor motorFrontLeft  = null;
    private static AdafruitDcMotor motorFrontRight = null;

    private AtomicBoolean isSystemInitialized = new AtomicBoolean(false);

    public MotorController() {

    }

    public boolean initialize() {
        if (this.isSystemInitialized.get() == false) {
            System.out.println("Initialize Motor Shield");

            // Initialize Hat
            motorHat = new AdafruitMotorHat(this.motorHATAddress);

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
                    motorHat.stopAll();
                }
            });

            // Initialize motors
            this.motorBackRight = motorHat.getDcMotor("M1");
            if (motorBackRight != null)
                System.out.println("Back Right Motor state " + this.motorBackRight.getState().toString());
            else
                System.out.println("Back Right Motor not initialized ...");


            this.motorBackLeft = motorHat.getDcMotor("M2");
            this.motorFrontLeft = motorHat.getDcMotor("M3");
            this.motorFrontRight = motorHat.getDcMotor("M4");
            // Set default power range
            this.motorFrontLeft.setPowerRange(DEFAULT_POWER_LEVEL * 2);
            this.motorFrontRight.setPowerRange(DEFAULT_POWER_LEVEL * 2);
            this.motorBackLeft.setPowerRange(DEFAULT_POWER_LEVEL * 2);
            this.motorBackRight.setPowerRange(DEFAULT_POWER_LEVEL * 2);

            this.motorFrontLeft.speed(MEDIUM);
            this.motorFrontRight.speed(MEDIUM);
            this.motorBackLeft.speed(MEDIUM);
            this.motorBackRight.speed(MEDIUM);

            // Done
            this.isSystemInitialized.set(true);
        }

        return true;
    }

    public boolean stop(){

        motorFrontLeft.setBrakeMode(true);
        motorFrontLeft.stop();

        motorFrontRight.setBrakeMode(true);
        motorFrontRight.stop();

        motorBackLeft.setBrakeMode(true);
        motorBackLeft.stop();

        motorBackRight.setBrakeMode(true);
        motorBackRight.stop();


        return true;
    }

    public boolean forward(String speed) {

        initialize();
        // Initialization
        float powerLevel = 0.0f;

  /*      // Stop all motors and break mode
        motorFrontLeft.setBrakeMode(true);
        this.motorFrontLeft.stop();

        motorFrontRight.setBrakeMode(true);
        this.motorFrontRight.stop();

        motorBackLeft.setBrakeMode(true);
        this.motorBackLeft.stop();

        motorBackRight.setBrakeMode(true);
        this.motorBackRight.stop();

        // Set power level (speed) for all 4 motors
        if (speed.equalsIgnoreCase("low"))
            powerLevel = this.LOW;
        else if (speed.equalsIgnoreCase("high"))
            powerLevel = this.HIGH;
        else
            powerLevel= MEDIUM;

    //    System.out.println("Power level forward: " + powerLevel);

        this.motorFrontLeft.setBrakeMode(false);
        this.motorFrontRight.setBrakeMode(false);
        this.motorBackLeft.setBrakeMode(false);
        this.motorBackRight.setBrakeMode(false);*/


        /*this.motorFrontLeft.setPowerRange(powerLevel);
        this.motorFrontRight.setPowerRange(powerLevel);
        this.motorBackLeft.setPowerRange(powerLevel);
        this.motorBackRight.setPowerRange(powerLevel);*/
        this.motorFrontLeft.speed(DEFAULT_SPEED);
        this.motorFrontRight.speed(DEFAULT_SPEED);
        this.motorBackLeft.speed(DEFAULT_SPEED);
        this.motorBackRight.speed(DEFAULT_SPEED);

        // All 4 motors forward
      //  System.out.println("Start motors forward");
        this.motorFrontLeft.forward();
        this.motorFrontRight.forward();
        this.motorBackLeft.forward();
        this.motorBackRight.forward();

        //this.motorHat.sleep(1000);

        //System.out.println("Done running forward");

        return true;
    }

    public boolean left(int degrees) {
        long timeToRun = (long)(TIME_FOR_ONE_DEGREE_TURN * degrees);

        // Stop all motors
        motorFrontLeft.setBrakeMode(true);
        motorFrontLeft.stop();

        motorFrontRight.setBrakeMode(true);
        motorFrontRight.stop();

        motorBackLeft.setBrakeMode(true);
        motorBackLeft.stop();

        motorBackRight.setBrakeMode(true);
        motorBackRight.stop();


        // Set default power mode for tuen
        /*motorFrontLeft.setPowerRange(HIGH);
        motorFrontRight.setPowerRange(HIGH);
        motorBackLeft.setPowerRange(HIGH);
        motorBackRight.setPowerRange(HIGH);*/

        this.motorFrontLeft.speed(HIGH);
        this.motorFrontRight.speed(HIGH);
        this.motorBackLeft.speed(HIGH);
        this.motorBackRight.speed(HIGH);


        this.motorFrontRight.reverse();
        this.motorFrontLeft.forward();
        this.motorBackRight.reverse();
        this.motorBackLeft.forward();

        motorHat.sleep(timeToRun);

        this.motorFrontLeft.speed(DEFAULT_SPEED);
        this.motorFrontRight.speed(DEFAULT_SPEED);
        this.motorBackLeft.speed(DEFAULT_SPEED);
        this.motorBackRight.speed(DEFAULT_SPEED);


        return true;
    }

    public boolean right(int degrees) {
        // Read the degrees
        // determine time to run (hint multiply the constant
        // turn on for time
        // time elapsed turn off
        //stop all motors
        initialize();
        motorFrontLeft.setBrakeMode(true);
        motorFrontLeft.stop();

        motorFrontRight.setBrakeMode(true);
        motorFrontRight.stop();

        motorBackLeft.setBrakeMode(true);
        motorBackLeft.stop();

        motorBackRight.setBrakeMode(true);
        motorBackRight.stop();

        //set all motor speeds
        /*motorFrontLeft.setPowerRange(HIGH);
        motorFrontRight.setPowerRange(HIGH);
        motorBackLeft.setPowerRange(HIGH);
        motorBackRight.setPowerRange(HIGH);*/

        this.motorFrontLeft.speed(HIGH);
        this.motorFrontRight.speed(HIGH);
        this.motorBackLeft.speed(HIGH);
        this.motorBackRight.speed(HIGH);


        long timeToRun = (long)(TIME_FOR_ONE_DEGREE_TURN * degrees);
        this.motorFrontRight.forward();
        this.motorBackLeft.reverse();
        this.motorFrontLeft.reverse();
        this.motorBackRight.forward();

        motorHat.sleep(timeToRun);

        this.motorFrontLeft.speed(DEFAULT_SPEED);
        this.motorFrontRight.speed(DEFAULT_SPEED);
        this.motorBackLeft.speed(DEFAULT_SPEED);
        this.motorBackRight.speed(DEFAULT_SPEED);


        return true;
    }

    public boolean backward(String speed) {
        // Initialization
        float powerLevel = 0.0f;

        initialize();
        motorFrontLeft.setBrakeMode(true);
        motorFrontLeft.stop();

        motorFrontRight.setBrakeMode(true);
        motorFrontRight.stop();

        motorBackLeft.setBrakeMode(true);
        motorBackLeft.stop();

        motorBackRight.setBrakeMode(true);
        motorBackRight.stop();

        // Set power level (speed) for all 4 motors

        // Set power level (speed) for all 4 motors
        if (speed.equalsIgnoreCase("low"))
            powerLevel = this.LOW;
        else if (speed.equalsIgnoreCase("high"))
            powerLevel = this.HIGH;
        else
            powerLevel= MEDIUM;

        /*motorFrontLeft.setPowerRange(powerLevel);
        motorFrontRight.setPowerRange(powerLevel);
        motorBackLeft.setPowerRange(powerLevel);
        motorBackRight.setPowerRange(powerLevel);*/

        this.motorFrontLeft.speed(DEFAULT_SPEED);
        this.motorFrontRight.speed(DEFAULT_SPEED);
        this.motorBackLeft.speed(DEFAULT_SPEED);
        this.motorBackRight.speed(DEFAULT_SPEED);

        this.motorFrontLeft.reverse();
        this.motorFrontRight.reverse();
        this.motorBackLeft.reverse();
        this.motorBackRight.reverse();

        this.motorHat.sleep(1000);
        return true;
    }
}
