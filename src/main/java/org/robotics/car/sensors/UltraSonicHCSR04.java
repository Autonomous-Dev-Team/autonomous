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
package org.robotics.car.sensors;

import org.robotics.car.exception.TimeoutException;

import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Implementing Sonic Sensor Model HC-SR04
 * Datasheet: http://www.micropik.com/PDF/HCSR04.pdf
 *
 * @author Roger Ruttimann
 */

public class UltraSonicHCSR04 extends Sensor {

    // Section for constants

    // Speed of Sound is 340.29 m per sec
    private final static float SOUND_SPEED = 340.29f;  // speed of sound in m/s

    private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration of 10 micro s
    private final static int WAIT_DURATION_IN_MILLIS = 60; // wait 60 milli s

    // Sensor probing timeout
    private final static int TIMEOUT = 30000;

    int gpioEcho = -1;
    int gpioTrigger = -1;

    private GpioPinDigitalInput echoIn = null;
    private GpioPinDigitalOutput triggerOut = null;

    /* Default settings */
    private Pin echoPin = null; //RaspiPin.GPIO_02;
    private Pin triggerPin = null; //RaspiPin.GPIO_03;

    // create gpio controller
    private GpioController gpio = GpioFactory.getInstance();

    double distance = 0.0;

    private long[] measurementSet = null;
    private int numberOfPointsForAverage = 1;

    /* Interval between measurements. 450 ms allows two measurement per second */
    private int MEASUREMENT_INTERVAL = 450;

    // Constants
    private short NUMBER_OF_POINTS_FOR_AVERAGE = 3;

    // Measurement array
    private AtomicLongArray measurmentPoints = new AtomicLongArray(NUMBER_OF_POINTS_FOR_AVERAGE);
    private AtomicLong avgDistance = new AtomicLong(0);

    // Constructor
    public UltraSonicHCSR04(String name, int gpioEcho, int gpioTrigger) {
        super(name);

        // Create the GPIO pins from the input params
        echoPin = RaspiPin.getPinByAddress(gpioEcho);
        triggerPin = RaspiPin.getPinByAddress(gpioTrigger);

        System.out.println("Echo Pin [" + echoPin.getAddress() + "] for name " + name);
        System.out.println("Trigger Pin [" + triggerPin.getAddress() + "] for name " + name);


        // Sonic Sensor specific members
        this.gpioEcho = gpioEcho;
        this.gpioTrigger = gpioTrigger;
        this.distance = -1;

        // Provision pins
        this.echoIn = gpio.provisionDigitalInputPin(echoPin, "EchoSignal", PinPullResistance.PULL_UP);
        this.triggerOut = gpio.provisionDigitalOutputPin(triggerPin, "TriggerSignal", PinState.LOW);

        triggerOut.low();

        // Set measurement semaphore
        this.isRunning.set(true);
    }

    /**
     * Public methods start here
     */


    /**
     * Thread that runs the measurement until sensor is de-initialized
     */
    @Override
    public void run() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println("Start sensor " + this.getSensorName() + " Time " + sdf.format(cal.getTime()));

        long measuredDistance = 0l;

        // Run loop until shutdown /uninitialize is called
        while (isRunning.get()) {

            try {
                // Measure distance
                measuredDistance = (long) measureObjDistance();

                avgDistance.set(measuredDistance);

                // Sleep before next measurement
                Thread.sleep(MEASUREMENT_INTERVAL);
            } catch (InterruptedException e) {
                System.out.println("Wait interrupted -- just continue...");
            } catch (TimeoutException te) {
                // Just log
                System.out.println("Timeout exception" + te);
            }

        }
        System.out.println("Stop sensor " + this.getSensorName());
    }

    /**
     * getDistance returns the current calculated value.
     *
     * @return measured distance that is averaged over several measurements
     */
    public long getDistance() {
        return this.avgDistance.get();
    }

    /**
     * Start of private methods
     */

    private void triggerSensor() {
        try {
            // Settle trigger signal
            this.triggerOut.low();
            Thread.sleep(50);
            // Start measurement
            this.triggerOut.high();

            //Sleep for 10 micro seconds;
            //Thread.sleep(0,TRIG_DURATION_IN_MICROS *1000);

            // 100 micro seconds
            Thread.sleep(0, 100 * 1000);

            this.triggerOut.low();

        } catch (InterruptedException ex) {
            System.err.println("Interrupt during trigger");
        }
    }

    /**
     * Wait for a high on the echo pin
     *
     * @throws .TimeoutException if no high appears in time
     */
    private void waitForSignal() throws TimeoutException {
        int countdown = TIMEOUT;

        while (this.echoIn.isLow() && countdown > 0) {
            countdown--;
        }

        if (countdown <= 0) {
            throw new TimeoutException("Timeout waiting for signal start");
        }
    }

    /**
     * @return the duration of the signal in micro seconds
     * @throws .TimeoutException if no low appears in time
     */
    private long measureSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        long start = System.nanoTime();
        while (this.echoIn.isHigh() && countdown > 0) {
            countdown--;
        }
        long end = System.nanoTime();

        if (countdown <= 0) {
            throw new TimeoutException("Timeout waiting for signal end");
        }

        return (long) Math.ceil((end - start) / 1000.0);  // Return micro seconds
    }

    /**
     * Encapsulated measuremnt routine in one method call for optimization
     *
     * @return distance in cm
     * @throws TimeoutException
     */
    private double measureObjDistance() throws TimeoutException {

        int countdown = TIMEOUT;
        this.triggerOut.low();


        try {
            this.triggerOut.low();

            // Settle trigger signal
            Thread.sleep(60);

            // Start measurement
            this.triggerOut.high();

            // 15 micro seconds pulse
            Thread.sleep(0, 15 * 1000);


            /**
             * From the specs for HC-SR04:
             * You can calculate the range through the time interval between sending
             * trigger signal and receiving echo signal.
             * Formula: uS / 58 = centimeters or uS / 148 =inch;
             */

            // Quiet trigger
            this.triggerOut.low();

            // Start measuring
            double start = System.nanoTime();

            while (this.echoIn.isLow() && countdown > 0) {
                countdown--;
            }

            // Echo signal arrived
            double end = System.nanoTime();

            // Calculate duration in micro seconds
            double duration = (end - start) / 1000;

            if (countdown <= 0) {
   //             throw new TimeoutException("Timeout waiting for signal start");
				duration =0;
            }

            /**
             * From the specs for HC-SR04:
             * You can calculate the range througthe
             * range = high level time * velocity (340M/S) / 2

             340m/s --> 34000 cm/s  --> 34 cm/ms --> 0.017 cm/micro second
             */


            //Reset counter
            countdown = TIMEOUT*5;
            start = System.nanoTime();

            // Measure result pulse
            while (this.echoIn.isHigh() && countdown > 0) {
                countdown--;
            }
            end = System.nanoTime();

            if (countdown <= 0) {
                throw new TimeoutException("Timeout waiting for signal end");
            }
            double distEcho = duration / 58.2;
            double distPulse = ((end - start) / 1000.0) * 0.017;

            // Filter distance range
 //           if (distPulse > 400 || distPulse < 2) {
 //               throw new TimeoutException("Distance out of Range");
 //           }

            return distPulse;


        } catch (InterruptedException ex) {
            System.err.println("Interrupt during trigger");
        }

		return 0l;
    }
}
