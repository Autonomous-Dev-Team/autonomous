package org.robotics.car.motor;

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
public class Motor {

    //enum motorStatus {"idle","on", "out of battery"}

    // Define members

    private String motorStatus = "idle";

    // Constructor
    public Motor(int PinReleaisForward, int pinRelaisReverse, int pinOnOffSwitch) {

    }

    // Methods
    public void forward() {

        // Turn on motor
        // TBD: call GPIO for Relais to turn motor on

        this.setMotorStatus("on");

    }

    public void reverse() {

        // Turn on motor
        // TBD: call GPIO for Relais to turn motor on reverse

        this.setMotorStatus("on");

    }

    public void stopMotor() {

        //
        this.setMotorStatus("idle");
    }

    public String getMotorStatus() {
        return this.motorStatus;
    }

    public void setMotorStatus(String status) {
        this.motorStatus = status;
    }
}