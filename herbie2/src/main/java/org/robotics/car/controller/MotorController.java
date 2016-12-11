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

/**
 * Created by rogerrut on 11/24/16.
 */
public class MotorController {

    // Constant
    private double TIME_FOR_ONE_DEGREE_TURN = 16.667; //milli seconds per degree

    static private float DEFAULT_POWER_LEVEL = 30.0f;

    static private float LOW = DEFAULT_POWER_LEVEL/2;
    static private float MEDIUM = DEFAULT_POWER_LEVEL;
    static private float HIGH = DEFAULT_POWER_LEVEL + 20;

    final int motorHATAddress = 0X60;
    final AdafruitMotorHat motorHat = new AdafruitMotorHat(motorHATAddress);

    private AdafruitDcMotor motorBackRight  = motorHat.getDcMotor("M1");
    private AdafruitDcMotor motorBackLeft   = motorHat.getDcMotor("M2");
    private AdafruitDcMotor motorFrontLeft  = motorHat.getDcMotor("M3");
    private AdafruitDcMotor motorFrontRight = motorHat.getDcMotor("M4");

    public MotorController() {
        // Set default power range
        motorFrontLeft.setPowerRange(DEFAULT_POWER_LEVEL*2);
        motorFrontRight.setPowerRange(DEFAULT_POWER_LEVEL*2);
        motorBackLeft.setPowerRange(DEFAULT_POWER_LEVEL*2);
        motorBackRight.setPowerRange(DEFAULT_POWER_LEVEL*2);
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
        // Initialization
        float powerLevel = 0.0f;

        // Stop all motors and break mode
        motorFrontLeft.setBrakeMode(true);
        motorFrontLeft.stop();

        motorFrontRight.setBrakeMode(true);
        motorFrontRight.stop();

        motorBackLeft.setBrakeMode(true);
        motorBackLeft.stop();

        motorBackRight.setBrakeMode(true);
        motorBackRight.stop();

        // Set power level (speed) for all 4 motors
        if (speed.equalsIgnoreCase("low"))
            powerLevel = this.LOW;
        else if (speed.equalsIgnoreCase("high"))
            powerLevel = this.HIGH;
        else
            powerLevel= MEDIUM;

        motorFrontLeft.setPowerRange(powerLevel);
        motorFrontRight.setPowerRange(powerLevel);
        motorBackLeft.setPowerRange(powerLevel);
        motorBackRight.setPowerRange(powerLevel);


        // All 4 motors forward
        this.motorFrontLeft.forward();
        this.motorFrontRight.forward();
        this.motorBackLeft.forward();
        this.motorBackRight.forward();

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
        motorFrontLeft.setPowerRange(MEDIUM);
        motorFrontRight.setPowerRange(MEDIUM);
        motorBackLeft.setPowerRange(MEDIUM);
        motorBackRight.setPowerRange(MEDIUM);


        this.motorFrontRight.forward();
        this.motorFrontLeft.reverse();
        this.motorBackRight.forward();
        this.motorBackLeft.reverse();

        motorHat.sleep(timeToRun);
        return true;
    }

    public boolean right(int degrees) {
        // Read the degrees
        // determine time to run (hint multiply the constant
        // turn on for time
        // time elapsed turn off
        //stop all motors
        motorFrontLeft.setBrakeMode(true);
        motorFrontLeft.stop();

        motorFrontRight.setBrakeMode(true);
        motorFrontRight.stop();

        motorBackLeft.setBrakeMode(true);
        motorBackLeft.stop();

        motorBackRight.setBrakeMode(true);
        motorBackRight.stop();

        //set all motor speeds
        motorFrontLeft.setPowerRange(MEDIUM);
        motorFrontRight.setPowerRange(MEDIUM);
        motorBackLeft.setPowerRange(MEDIUM);
        motorBackRight.setPowerRange(MEDIUM);

        long timeToRun = (long)(TIME_FOR_ONE_DEGREE_TURN * degrees);
        this.motorFrontRight.reverse();
        this.motorBackLeft.forward();
        this.motorFrontLeft.forward();
        this.motorBackRight.reverse();

        motorHat.sleep(timeToRun);
        return true;
    }

    public boolean backward(String speed) {
        // Initialization
        float powerLevel = 0.0f;

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

        motorFrontLeft.setPowerRange(powerLevel);
        motorFrontRight.setPowerRange(powerLevel);
        motorBackLeft.setPowerRange(powerLevel);
        motorBackRight.setPowerRange(powerLevel);

        this.motorFrontLeft.reverse();
        this.motorFrontRight.reverse();
        this.motorBackLeft.reverse();
        this.motorBackRight.reverse();
        return true;
    }
}
