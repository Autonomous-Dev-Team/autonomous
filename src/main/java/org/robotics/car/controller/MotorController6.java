package org.robotics.car.controller;

import com.pi4j.component.adafruithat.AdafruitDcMotor;
import com.pi4j.component.adafruithat.AdafruitMotorHat;
import com.pi4j.component.adafruithat.AdafruitStepperMotor;
import com.pi4j.component.adafruithat.StepperMode;

import java.util.concurrent.atomic.AtomicBoolean;

public class MotorController6 {
   // Constant

    static private float DEFAULT_POWER_LEVEL = 40.0f;
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
    final int motorHATUpperAddress = 0X61; // middle motors and stepper

    public AdafruitMotorHat motorHatLower = null;
    public AdafruitMotorHat motorHatUpper = null;

    private static AdafruitDcMotor motorBackRight  = null;
    private static AdafruitDcMotor motorBackLeft   = null;
    private static AdafruitDcMotor motorFrontLeft  = null;
    private static AdafruitDcMotor motorFrontRight = null;
    private static AdafruitDcMotor motorMiddleRight = null;
    private static AdafruitDcMotor motorMiddleLeft = null;

    private static AdafruitStepperMotor stepper = null;

    private AtomicBoolean isSystemInitialized = new AtomicBoolean(false);

    // Default constructor will use test setup
    public MotorController6() {
        initializeMotorController("M4","M3","M2","M1","M2","M1");
    }

    // Class constructor called when calss is initialized
    public MotorController6( String motorFrontLeft, String motorFrontRight, String motorBackLeft ,String motorBackRight,  String motorMiddleRight, String motorMiddleLeft  ) {

        initializeMotorController(motorFrontLeft, motorFrontRight, motorBackLeft ,motorBackRight,  motorMiddleRight, motorMiddleLeft);

    }

    public void initializeMotorController(String motorFrontLeft, String motorFrontRight, String motorBackLeft ,String motorBackRight,  String motorMiddleRight, String motorMiddleLeft) {

        // Initialize Hat
        motorHatUpper = new AdafruitMotorHat(this.motorHATUpperAddress);
        motorHatLower = new AdafruitMotorHat(this.motorHATLowerAddress);


        this.motorBackRight = motorHatLower.getDcMotor(motorBackRight);         // M1
        this.motorBackLeft = motorHatLower.getDcMotor(motorBackLeft);           // M2
        this.motorFrontLeft = motorHatLower.getDcMotor(motorFrontLeft);         // M4
        this.motorFrontRight = motorHatLower.getDcMotor(motorFrontRight);       // M3

        // Upper motor shield
        this.motorMiddleRight = motorHatUpper.getDcMotor(motorMiddleRight);         // M2
        this.motorMiddleLeft = motorHatUpper.getDcMotor(motorMiddleLeft);          // M1

        /**
         * Initialize stepper motor
         */

        ////////////////////////////////////////////////////////
        // Wire Configuration please follow as described below
        //
        //
        // 		M3                M4
        // blue, yellow  empty  red green
        //
        /////////////////////////////////////////////////////////

        //Create an instance for this stepper motor. A motorHAT can command
        //two stepper motors ("SM1" and "SM2")
        this.stepper = motorHatUpper.getStepperMotor("SM2");

        //Set Stepper Mode to SINGLE_PHASE
        this.stepper.setMode(StepperMode.SINGLE_PHASE);

        //Set the number of motor steps per 360 degree
        //revolution for this stepper mode.
        this.stepper.setStepsPerRevolution(200);

        //Time between each step in milliseconds.
        //In this example, "true" indicates to terminate if
        //stepper motor can not achieve 100ms per step.
        this.stepper.setStepInterval(5, false);


        // Set default power range
        this.motorFrontLeft.setPowerRange(DEFAULT_POWER_LEVEL * 2);
        this.motorFrontRight.setPowerRange(DEFAULT_POWER_LEVEL * 2);

        this.motorBackLeft.setPowerRange(DEFAULT_POWER_LEVEL * 2);
        this.motorBackRight.setPowerRange(DEFAULT_POWER_LEVEL * 2);

        this.motorMiddleLeft.setPowerRange(DEFAULT_POWER_LEVEL * 2);
        this.motorMiddleRight.setPowerRange(DEFAULT_POWER_LEVEL * 2);


        //Set power but do not set or change the motor state (stop, forward, reverse)
        //The power value will be used with the next forward() or reverse() command and
        //does not otherwise change the current motor power level.
        this.motorFrontLeft.setPower(DEFAULT_POWER_LEVEL);
        this.motorFrontRight.setPower(DEFAULT_POWER_LEVEL);
        this.motorBackLeft.setPower(DEFAULT_POWER_LEVEL);
        this.motorBackRight.setPower(DEFAULT_POWER_LEVEL);

        this.motorMiddleLeft.setPower(DEFAULT_POWER_LEVEL);
        this.motorMiddleRight.setPower(DEFAULT_POWER_LEVEL);

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
                motorHatLower.stopAll();
                motorHatUpper.stopAll();
            }
        });

    }

    public void uninitialize() {
        motorHatUpper.stopAll();
        motorHatLower.stopAll();
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
                    motorHatUpper.stopAll();
                    motorHatLower.stopAll();
                }
            });

            // Done
            this.isSystemInitialized.set(true);
        }

        return true;
    }

    public boolean stepRight(int nbOfSteps){
        this.stepper.step(nbOfSteps * -1);
        return true;
    }

    public boolean stepLeft(int nbOfSteps){
        this.stepper.step(nbOfSteps);
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

        motorMiddleLeft.setBrakeMode(true);
        motorMiddleLeft.stop();

        motorMiddleRight.setBrakeMode(true);
        motorMiddleRight.stop();


        return true;
    }

    public boolean forward() {

        this.motorFrontLeft.setPower(DEFAULT_POWER_LEVEL/2);
        this.motorFrontRight.setPower(DEFAULT_POWER_LEVEL/2);
        this.motorBackLeft.setPower(DEFAULT_POWER_LEVEL/2);
        this.motorBackRight.setPower(DEFAULT_POWER_LEVEL/2);

        this.motorMiddleLeft.setPower(DEFAULT_POWER_LEVEL/2);
        this.motorMiddleRight.setPower(DEFAULT_POWER_LEVEL/2);


        // All 4 motors forward
        //  System.out.println("Start motors forward");
        this.motorFrontLeft.forward();
        this.motorFrontRight.forward();
        this.motorBackLeft.forward();
        this.motorBackRight.forward();
        this.motorMiddleLeft.forward();
        this.motorMiddleRight.forward();

        return true;
    }

    public boolean right(int degrees) {

        int turn_time = degrees*200;

        this.motorFrontLeft.setPower(DEFAULT_POWER_LEVEL*2);
        this.motorFrontRight.setPower(DEFAULT_POWER_LEVEL*2);
        this.motorBackLeft.setPower(DEFAULT_POWER_LEVEL*2);
        this.motorBackRight.setPower(DEFAULT_POWER_LEVEL*2);

        this.motorMiddleLeft.setPower(DEFAULT_POWER_LEVEL*2);
        this.motorMiddleRight.setPower(DEFAULT_POWER_LEVEL*2);

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


        motorFrontRight.reverse();
        motorBackLeft.forward();
        motorFrontLeft.forward();
        motorBackRight.reverse();
        motorMiddleRight.reverse();
        motorMiddleLeft.forward();
        motorHatLower.sleep(turn_time);


        return true;
    }

    public boolean left(int degrees) {

        int turn_time = degrees*200;

        this.motorFrontLeft.setPower(DEFAULT_POWER_LEVEL*2);
        this.motorFrontRight.setPower(DEFAULT_POWER_LEVEL*2);
        this.motorBackLeft.setPower(DEFAULT_POWER_LEVEL*2);
        this.motorBackRight.setPower(DEFAULT_POWER_LEVEL*2);

        this.motorMiddleLeft.setPower(DEFAULT_POWER_LEVEL*2);
        this.motorMiddleRight.setPower(DEFAULT_POWER_LEVEL*2);

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
        motorFrontRight.forward();
        motorMiddleRight.forward();
        motorMiddleLeft.reverse();
        motorBackLeft.reverse();

        motorHatLower.sleep(turn_time);

        return true;
    }

    public boolean backward() {

        this.motorFrontLeft.setPower(DEFAULT_POWER_LEVEL/2);
        this.motorFrontRight.setPower(DEFAULT_POWER_LEVEL/2);
        this.motorBackLeft.setPower(DEFAULT_POWER_LEVEL/2);
        this.motorBackRight.setPower(DEFAULT_POWER_LEVEL/2);

        this.motorMiddleLeft.setPower(DEFAULT_POWER_LEVEL/2);
        this.motorMiddleRight.setPower(DEFAULT_POWER_LEVEL/2);

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
