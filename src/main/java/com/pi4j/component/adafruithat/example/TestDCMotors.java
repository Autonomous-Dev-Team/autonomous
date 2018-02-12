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

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
public class TestDCMotors {

	static private float DEFAULT_POWER_LEVEL = 80.0f;


	public static void main(String[] args) {
		/*
			Create an instance of the Motor controller
		 */
		final int motorHATAddress = 0X60;
		final AdafruitMotorHat motorHat = new AdafruitMotorHat(motorHATAddress);

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


		/**
		 * Configure the motor controller. Make sure the wire match the configurations
		 */
		//	AdafruitDcMotor motorBackRight  = motorHat.getDcMotor("M1");
		//	AdafruitDcMotor motorBackLeft   = motorHat.getDcMotor("M2");
		//	AdafruitDcMotor motorFrontLeft  = motorHat.getDcMotor("M3");
		//			AdafruitDcMotor motorFrontRight = motorHat.getDcMotor("M4");

		AdafruitDcMotor motorBackRight = motorHat.getDcMotor("M2");
		AdafruitDcMotor motorBackLeft = motorHat.getDcMotor("M1");
		AdafruitDcMotor motorFrontLeft = motorHat.getDcMotor("M3");
		AdafruitDcMotor motorFrontRight = motorHat.getDcMotor("M4");

		AdafruitDcMotor motorMiddleLeft = motorHat.getDcMotor("M1");
		AdafruitDcMotor motorMiddleRight = motorHat.getDcMotor("M2");



		//A speed value of 100 sets the DC motor to .maximum throttle.
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
		motorHat.sleep(6000);





		//move forward at power level specified above
		/*System.out.println("Move foward for 2 sec");
		motorFrontLeft.forward();
		motorFrontRight.forward();
		motorBackLeft.forward();
		motorBackRight.forward();
		motorHat.sleep(2000);

		// Turn 180 degrees
		// Turn left 1500 ms for 90 degrees
		System.out.println("Make a left turn ...");
		motorFrontLeft.reverse();
		motorBackRight.forward();
		motorFrontRight.forward();
		motorBackLeft.reverse();
		motorHat.sleep(3200);

		System.out.println("Drive back to starting point ..");
		motorFrontLeft.forward();
		motorFrontRight.forward();
		motorBackLeft.forward();
		motorBackRight.forward();
		motorHat.sleep(2000);

		System.out.println("Make a right turn ...");
		motorFrontRight.reverse();
		motorBackLeft.forward();
		motorFrontLeft.forward();
		motorBackRight.reverse();
		motorHat.sleep(3200);

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
		*/
	}
}