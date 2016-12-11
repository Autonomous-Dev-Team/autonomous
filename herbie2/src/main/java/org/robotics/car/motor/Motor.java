package org.robotics.car.motor;

/**
 * Created by rogerrut on 11/24/16.
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