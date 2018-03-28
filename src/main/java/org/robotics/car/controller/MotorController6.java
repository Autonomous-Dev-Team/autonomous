package org.robotics.car.controller;

import com.pi4j.component.adafruithat.AdafruitDcMotor;
import com.pi4j.component.adafruithat.AdafruitMotorHat;

public class MotorController6 {
   // Constant

    static private float DEFAULT_POWER_LEVEL = 20.0f;
    static private float DEFAULT_SPEED = 20.0f;

    static private float CARPET_SPEED = 1.5f;
    static private float CARPET_TURN = 5.2f;

    //One degree turn for terrain
    static double BASETIME_FOR_ONE_DEGREE = 2.8;

    static double terrain_carpet = 13.3*3;
    static private float LOW = DEFAULT_SPEED/2;
    static private float MEDIUM = DEFAULT_SPEED;
    static private float HIGH = 30.0f;

    private String terrainType = "tile";

    final int motorHATLowerAddress = 0X60; // Front and back motors
    final int motorHATUpperAddress = 0X61; // middle motors

    public AdafruitMotorHat motorHatLower = null;
    public AdafruitMotorHat motorHatUpper = null;

    private static AdafruitDcMotor motorBackRight  = null;
    private static AdafruitDcMotor motorBackLeft   = null;
    private static AdafruitDcMotor motorFrontLeft  = null;
    private static AdafruitDcMotor motorFrontRight = null;
    private static AdafruitDcMotor motorMiddleRight = null;
    private static AdafruitDcMotor motorMiddleLeft = null;

    private AtomicBoolean isSystemInitialized = new AtomicBoolean(false);

    // Class constructor called when calss is initialized
    public MotorController6( String motorFrontLeft, String motorFrontRight, String motorBackLeft ,String motorBackRight,  String motorMiddleRight, String motorMiddleLeft  ) {

        // Initialize Hat
        motorHatUpper = new AdafruitMotorHat(this.motorHATUpperAddress);
        motorHatLower = new AdafruitMotorHat(this.motorHATLowerAddress);


        this.motorBackRight = motorHatLower.getDcMotor(motorBackRight);         // M1
        this.motorBackLeft = motorHatLower.getDcMotor(motorBackLeft);           // M2
        this.motorFrontLeft = motorHatLower.getDcMotor(motorFrontLeft);         // M4
        this.motorFrontRight = motorHatLower.getDcMotor(motorFrontRight);       // M2

        // Upper motor shield
        this.motorMiddleRight = motorHatUpper.getDcMotor(motorMiddleRight);         // M2
        this.motorMiddleLeft = motorHatUpper.getDcMotor(motorMiddleLeft);          // M1



        // Set default power range
        motorFrontLeft.setPowerRange(DEFAULT_POWER_LEVEL * 2);
        motorFrontRight.setPowerRange(DEFAULT_POWER_LEVEL * 2);

        motorBackLeft.setPowerRange(DEFAULT_POWER_LEVEL * 2);
        motorBackRight.setPowerRange(DEFAULT_POWER_LEVEL * 2);

        motorMiddleLeft.setPowerRange(DEFAULT_POWER_LEVEL * 2);
        motorMiddleRight.setPowerRange(DEFAULT_POWER_LEVEL * 2);


        //Set power but do not set or change the motor state (stop, forward, reverse)
        //The power value will be used with the next forward() or reverse() command and
        //does not otherwise change the current motor power level.
        motorFrontLeft.setPower(DEFAULT_POWER_LEVEL);
        motorFrontRight.setPower(DEFAULT_POWER_LEVEL);
        motorBackLeft.setPower(DEFAULT_POWER_LEVEL);
        motorBackRight.setPower(DEFAULT_POWER_LEVEL);

        motorMiddleLeft.setPower(DEFAULT_POWER_LEVEL);
        motorMiddleRight.setPower(DEFAULT_POWER_LEVEL);

    }

    public void uninitialize() {
        motorHat.stopAll();
    }

    public boolean initialize() {
        if (this.isSystemInitialized.get() == false) {
            System.out.println("Initialize Motor Shield");

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

    public boolean forward() {



        // All 4 motors forward
        //  System.out.println("Start motors forward");
        this.motorFrontLeft.forward();
        this.motorFrontRight.forward();
        this.motorBackLeft.forward();
        this.motorBackRight.forward();
        motorMiddleLeft.forward();
        motorMiddleRight.forward();

        return true;
    }

    public boolean right(int degrees) {

        // Stop all motors
        motorFrontLeft.setBrakeMode(true);
        motorFrontLeft.stop();

        motorFrontRight.setBrakeMode(true);
        motorFrontRight.stop();

        motorBackLeft.setBrakeMode(true);
        motorBackLeft.stop();

        motorBackRight.setBrakeMode(true);
        motorBackRight.stop();

        motorMiddleRight.setBrakeMode(true);
        motorMiddleRight.stop();

        motorMiddleLeft.setBrakeMode(true);
        motorMiddleLeft.stop();


        motorFrontLeft.forward();
        motorBackRight.reverse();
        motorMiddleLeft.forward();
        motorMiddleRight.reverse();
        motorFrontRight.reverse();
        motorBackLeft.forward();


        return true;
    }

    public boolean left(int degrees) {

        motorFrontLeft.setBrakeMode(true);
        motorFrontLeft.stop();

        motorFrontRight.setBrakeMode(true);
        motorFrontRight.stop();

        motorBackLeft.setBrakeMode(true);
        motorBackLeft.stop();

        motorBackRight.setBrakeMode(true);
        motorBackRight.stop();

        motorMiddleRight.setBrakeMode(true);
        motorMiddleRight.stop();

        motorMiddleLeft.setBrakeMode(true);
        motorMiddleLeft.stop();

        motorFrontLeft.reverse();
        motorBackRight.forward();
        motorMiddleLeft.reverse();
        motorMiddleRight.forward();
        motorFrontRight.forward();
        motorBackLeft.reverse();


        return true;
    }

    public boolean backward() {


        motorFrontLeft.setBrakeMode(true);
        motorFrontLeft.stop();

        motorFrontRight.setBrakeMode(true);
        motorFrontRight.stop();

        motorBackLeft.setBrakeMode(true);
        motorBackLeft.stop();

        motorBackRight.setBrakeMode(true);
        motorBackRight.stop();

        // Set power level (speed) for all 4 motors
        this.motorFrontLeft.reverse();
        this.motorFrontRight.reverse();
        this.motorBackLeft.reverse();
        this.motorBackRight.reverse();
        this.motorMiddleLeft.reverse();
        this.motorMiddleRight.reverse();

        return true;
    }
}
