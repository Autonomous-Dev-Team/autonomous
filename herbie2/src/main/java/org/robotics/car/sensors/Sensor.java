package org.robotics.car.sensors;

/**
*
*
*/

import java.util.concurrent.atomic.AtomicBoolean;


public class Sensor extends Thread {
	
	private String sensorName = null;
	private	int    sensorStatus = 0;
	private int    sensorValue = 0;

	
	AtomicBoolean isRunning = new AtomicBoolean(false);
	
	// Constructor
	public Sensor(String sensorName) {
		this.sensorName = sensorName;
	};
	
	
	
	public int getStatus() {
		return this.sensorStatus;
	}
	public int getValue() {
		return this.sensorValue;
	}
	
	// Setter & getter
	public String getSensorName() {
		return this.sensorName;
	}
	
	public boolean shutdown() {
		// interrupt the thread
		System.out.println("Received shutdown request ...");
		//if (isRunning.get() == true ) {
				interrupt();
		//}	
		
		// Success 
		isRunning.set(false);
		return true;
	}
	
	/**
	 * Run method will read sensor and determine if there was a signal in the last time interval
	 * Thread runs until interrupted
	 */
	
	@Override
    public void run() {
		
		System.out.println("Base class run. Should never been called overwiritten by Sensors");
            
        try {
			Thread.sleep(200);
        } catch (InterruptedException e) {
            // Interrupted exception will occur if
            // the Worker object's interrupt() method
            // is called. interrupt() is inherited
            // from the Thread class.
        }
	}
}
