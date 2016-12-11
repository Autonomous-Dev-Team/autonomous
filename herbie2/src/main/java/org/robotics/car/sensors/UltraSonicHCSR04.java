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

import org.robotics.car.sensors.Sensor;
import org.robotics.car.exception.TimeoutException;

import com.pi4j.util.CommandArgumentParser;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

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
    private final static int TIMEOUT = 1000;
	
	int gpioEcho    = -1;
	int gpioTrigger = -1;
	
	private GpioPinDigitalInput  echoIn = null;
	private GpioPinDigitalOutput triggerOut = null;
	
	/* Default settings */
	private Pin echoPin 	= null; //RaspiPin.GPIO_02;
	private Pin triggerPin	= null; //RaspiPin.GPIO_03;
	
	// create gpio controller
	private GpioController gpio = GpioFactory.getInstance();
	
	double distance = 0.0;	
	
	// Constructor
	public UltraSonicHCSR04(String name, int gpioEcho, int gpioTrigger) {
		super(name);

		// Create the GPIO pins from the input params
		echoPin = RaspiPin.getPinByAddress(gpioEcho);
		triggerPin = RaspiPin.getPinByAddress(gpioTrigger);

		System.out.println("Echo Pin [" + echoPin.getAddress() + "] has name "+ echoPin.getName() );
		System.out.println("Trigger Pin [" + triggerPin.getAddress() + "] has name "+ triggerPin.getName() );

		// Sonic Sensor specific members
		this.gpioEcho	 = gpioEcho;
		this.gpioTrigger = gpioTrigger;
		this.distance = -1;

		// Provision pins
		this.echoIn		= gpio.provisionDigitalInputPin(echoPin, "EchoSignal", PinPullResistance.PULL_UP);
		this.triggerOut	= gpio.provisionDigitalOutputPin(triggerPin, "TriggerSignal", PinState.LOW);
		
		triggerOut.low();	
	}

	// Setters and Getters
	public float getDistance() {
		return 0;
		
	}

	/**
	 * Public methods start here
	 *
	 */
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
     /** This method returns the distance measured by the sensor in cm
     * 
     * @throws TimeoutException if a timeout occurs
     */
    public float measureDistance() throws TimeoutException {
		long startTime = System.currentTimeMillis();
        this.triggerSensor();
        this.waitForSignal();
        long duration = this.measureSignal();
        
        //System.out.printf("Time %d ms to run measurement. ", System.currentTimeMillis() - startTime);

		// Round up result before returning
		float result = duration * SOUND_SPEED / ( 2 * 10000 );

		// round up result -- Maurice will do it
		return (float) Math.ceil(result);
	}

	public float measureDistanceAverage(int samplesize) throws TimeoutException {
		//Local Variables
		float measurements = 0;
		for (int i = 0; i < samplesize; i++) {
			try {
				measurements = measurements + measureDistance();
				Thread.sleep(200);
			} catch (TimeoutException te){
				// Repeat measurement
				i--;

		//		System.out.println("Sensor was not ready, reinitailizing loop");
			} catch (InterruptedException ie) {
				System.out.println("Loop interrupted..");
			}
		}
		//Calculate the Average and return the result]
		return measurements/samplesize;

	}

	/**
	 * Start of private methods
	 */

	private void triggerSensor() {
	try {
            this.triggerOut.high();
            Thread.sleep( 0, TRIG_DURATION_IN_MICROS * 1000 );
            this.triggerOut.low();
        } catch (InterruptedException ex) {
            System.err.println( "Interrupt during trigger" );
        }
	}
	
	/**
     * Wait for a high on the echo pin
     * 
     * @throws .TimeoutException if no high appears in time
     */
    private void waitForSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        
        while( this.echoIn.isLow() && countdown > 0 ) {
            countdown--;
        }
        
        if( countdown <= 0 ) {
            throw new TimeoutException( "Timeout waiting for signal start" );
        }
    }
    
    /**
     * @return the duration of the signal in micro seconds
     * @throws .TimeoutException if no low appears in time
     */
    private long measureSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        long start = System.nanoTime();
        while( this.echoIn.isHigh() && countdown > 0 ) {
            countdown--;
        }
        long end = System.nanoTime();
        
        if( countdown <= 0 ) {
            throw new TimeoutException( "Timeout waiting for signal end" );
        }
        
        return (long)Math.ceil( ( end - start ) / 1000.0 );  // Return micro seconds
    }

}
