package com.pi4j.component.adafruithat.example;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  AdafruitDcMotorExample.java
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
 * #L%
 */
import com.pi4j.component.adafruithat.AdafruitDcMotor;
import com.pi4j.component.adafruithat.AdafruitMotorHat;
import com.pi4j.component.adafruithat.AdafruitStepperMotor;
import com.pi4j.component.adafruithat.StepperMode;

/**
 * Example program commanding a DC Motor wired to a AdafruitMotorHat.
 * <p>
 * <a href="https://www.adafruit.com/products/2348">See MotorHAT</a>
 * <p>
 * In this example four DC motors are wired to the Adafruit motor HAT. They are commanded at
 * different speeds and motor direction.
 * 
 * @author Eric Eliason
 * @see AdafruitDcMotor
 * @see AdafruitMotorHat
 *
 */
public class Test6DCMotors {

	static private float DEFAULT_POWER_LEVEL = 80.0f;


	public static void main(String[] args) {
		/*
			Create an instance of the Motor controller
		*/

		/* Lower board with the 4 DC Motors Front, middle motors */
		final int motorHATAddressDCMotors = 0X60;

		/* M1, M2 are the DC motors and M3, M4 is the stepper (SM2) */
		final int motorHATAddressSteper = 0X61;

		final AdafruitMotorHat motorHatLower = new AdafruitMotorHat(motorHATAddressDCMotors);
		final AdafruitMotorHat motorHatUp	 = new AdafruitMotorHat(motorHATAddressSteper);

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
				motorHatUp.stopAll();
			}
		});


		/**
		 * Configure the motor controller. Make sure the wire match the configurations
		 */

        /* Initialize DC motors */
        /* Lower board */
		AdafruitDcMotor motorBackRight  = motorHatLower.getDcMotor("M1");
		AdafruitDcMotor motorBackLeft   = motorHatLower.getDcMotor("M2");
		AdafruitDcMotor motorFrontLeft  = motorHatLower.getDcMotor("M4");
		AdafruitDcMotor motorFrontRight = motorHatLower.getDcMotor("M3");

		/* Upper Board */
		AdafruitDcMotor motorMiddleLeft = motorHatUp.getDcMotor("M1");
		AdafruitDcMotor motorMiddleRight = motorHatUp.getDcMotor("M2");


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
		AdafruitStepperMotor stepper = motorHatUp.getStepperMotor("SM2");

		//Set Stepper Mode to SINGLE_PHASE
		stepper.setMode(StepperMode.SINGLE_PHASE);

		//Set the number of motor steps per 360 degree
		//revolution for this stepper mode.
		stepper.setStepsPerRevolution(200);

		//Time between each step in milliseconds.
		//In this example, "true" indicates to terminate if
		//stepper motor can not achieve 100ms per step.
		stepper.setStepInterval(5, false);

		for (int i=0; i <5; i++)
		{

			stepper.step(-100);
			System.out.format("100 steps");

			// Remain for 200 ms before next measurement
			try {
				Thread.sleep(200);
			} catch(Exception e) {

				System.out.println("Interrupt");
			}

			//reverse
			stepper.step(100);
			System.out.format("100 steps");

			// Remain for 200 ms before next measurement
			try {
				Thread.sleep(200);
			} catch(Exception e) {

				System.out.println("Interrupt");
			}

			//Move 0.5 revolutions at the fastest possible speed in forward direction
//		stepper.setStepInterval(5,false);
//		stepper.rotate(-0.5);
//		System.out.format("180 degrees currentStep: %d\n",stepper.getCurrentStep());


			//Move 2.5 revolutions at the fastest possible speed in forward direction
//		stepper.setStepInterval(5,false);
//		stepper.rotate(0.5);
//		System.out.format("Minis 180 degrees. currentStep: %d\n",stepper.getCurrentStep());
		}





		//A speed value of 100 sets the DC motor to maximum throttle.
		//The default power range is 1.0.

                /*
                 * Observtion setting all motors to 100 generates a spike that causes the
                 * Raspberry PI to reboot. Setting the max to 50 seems more reasonable
                 */
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



		//move forward at power level specified above
		System.out.println("Move foward for 2 sec");
		motorFrontLeft.forward();
		motorFrontRight.forward();

        motorBackLeft.forward();
        motorBackRight.forward();

		motorMiddleLeft.forward();
		motorMiddleRight.forward();
		motorHatLower.sleep(2000);

		//move forward at power level specified above
		System.out.println("Move back for 2 sec");
		motorFrontLeft.reverse();
		motorFrontRight.reverse();

		motorBackLeft.reverse();
		motorBackRight.reverse();

		motorMiddleLeft.reverse();
		motorMiddleRight.reverse();
		motorHatLower.sleep(2000);


		//move forward at power level specified above
		/*System.out.println("Move foward for 2 sec");
		motorFrontLeft.forward();
		motorFrontRight.forward();
		motorBackLeft.forward();
		motorBackRight.forward();
		motorHat.sleep(2000);
*/
		// Turn 180 degrees
		//Turn left 1500 ms for 90 degrees
		System.out.println("Make a left turn ...");
		motorFrontLeft.reverse();
		motorBackRight.forward();
		motorFrontRight.forward();
		motorMiddleRight.forward();
		motorMiddleLeft.reverse();
		motorBackLeft.reverse();
		motorHatLower.sleep(1200);

		System.out.println("Make a left turn ...");
		motorFrontLeft.reverse();
		motorBackRight.forward();
		motorFrontRight.forward();
		motorMiddleRight.forward();
		motorMiddleLeft.reverse();
		motorBackLeft.reverse();
		motorHatLower.sleep(1200);

		if (false) {

			System.out.println("Make a right turn ...");
			motorFrontRight.reverse();
			motorBackLeft.forward();
			motorFrontLeft.forward();
			motorBackRight.reverse();
			motorMiddleRight.reverse();
			motorMiddleLeft.forward();
			motorHatLower.sleep(1200);

			System.out.println("Make a left turn ...");
			motorFrontLeft.reverse();
			motorBackRight.forward();
			motorFrontRight.forward();
			motorMiddleRight.forward();
			motorMiddleLeft.reverse();
			motorBackLeft.reverse();

			motorHatLower.sleep(1200);

			System.out.println("Make a right turn ...");
			motorFrontRight.reverse();
			motorBackLeft.forward();
			motorFrontLeft.forward();
			motorBackRight.reverse();
			motorMiddleRight.reverse();
			motorMiddleLeft.forward();
			motorHatLower.sleep(1200);

			System.out.println("Make a left turn ...");
			motorFrontLeft.reverse();
			motorBackRight.forward();
			motorFrontRight.forward();
			motorMiddleRight.forward();
			motorMiddleLeft.reverse();
			motorBackLeft.reverse();

			motorHatLower.sleep(1200);

			System.out.println("Make a right turn ...");
			motorFrontRight.reverse();
			motorBackLeft.forward();
			motorFrontLeft.forward();
			motorBackRight.reverse();
			motorMiddleRight.reverse();
			motorMiddleLeft.forward();
			motorHatLower.sleep(1200);

		}

		//Stop motors in brake mode.
		motorFrontLeft.setBrakeMode(true);
		motorFrontLeft.stop();

		motorFrontRight.setBrakeMode(true);
		motorFrontRight.stop();

		motorBackLeft.setBrakeMode(true);
		motorBackLeft.stop();

		motorBackRight.setBrakeMode(true);
		motorBackRight.stop();

		System.out.println("Done. Should be at the starting point ...");

	}
}