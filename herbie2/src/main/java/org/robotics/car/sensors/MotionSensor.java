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

/**
* Implementing motion sensor Type: 
* Datasheet: http://
*/



public class MotionSensor extends Sensor {
	
	// Sensor specific variables
	int    timeInerval = 5000;
	int	   gpioSignal = -1;
		
	
	// Constructor
	public MotionSensor(String sensorName, int gpioSignal, int timeInterval) {
		super(sensorName);
		
		// Motion specific members
		this.gpioSignal = gpioSignal;
		this.timeInerval = timeInerval;
	}
	
	public void initialize( int gpio, int timeInterval){
		
		// If the thread is running interrupt before re-initializing
		if (isRunning.get() == true ) {
			interrupt();
		}
		
		this.gpioSignal = gpio;
		this.timeInerval = timeInterval;
	}
	
	/**
	 * Run method will read sensor and determine if there was a signal in the last time interval
	 * Thread runs until interrupted
	 */
	
	@Override
    public void run() {
		for(int i=0; i<10; i++) {
            System.out.println(i + " looping ...");
            
            // Sleep for a while
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // Interrupted exception will occur if
                // the Worker object's interrupt() method
                // is called. interrupt() is inherited
                // from the Thread class.
                break;
            }
        }
	}
}
