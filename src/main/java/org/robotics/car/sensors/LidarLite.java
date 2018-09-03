package org.robotics.car.sensors;

/**
 * LIDAR Lite 3 Rangefinder
 *
 * The following implementation impements the interfacing with the LIDAR Lite 3 sensor using Raspberry PI
 * using the I2C bus
 *
 * Setup:
 *
 * Wireing for the sensor for running the test and production
 * Black: GND
 * Red: +5V
 * Blue: Pin 3 SDA
 * Green: Pin 5 SCL
 * Orange / Yellow unused
 *
 * The wireing is for I2CBus.BUS_1, and Address 0x62
 *
 */

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.util.Console;


public class LidarLite {

    /**
     * Based on the I2C example included in the Pi4J distribution. Adapted to work with the
     * Lidar Lite 3 sensor connected to the Raspberry PI
     */

    // TSL2561 I2C address
        public static final int TSL2561_ADDR = 0x39; // address pin not connected (FLOATING)
        //public static final int TSL2561_ADDR = 0x29; // address pin connect to GND
        //public static final int TSL2561_ADDR = 0x49; // address pin connected to VDD

        // TSL2561 registers
        public static final byte TSL2561_REG_ID = (byte) 0x8A;
        public static final byte TSL2561_REG_DATA_0 = (byte) 0x8C;
        public static final byte TSL2561_REG_DATA_1 = (byte) 0x8E;
        public static final byte TSL2561_REG_CONTROL = (byte) 0x80;

        // TSL2561 power control values
        public static final byte TSL2561_POWER_UP = (byte) 0x03;
        public static final byte TSL2561_POWER_DOWN = (byte) 0x00;


        // LIDAT Lite specific settings
        public static final int LIDAR_LITE_ADDR = 0x62; // I2c configured address for sensor

        public static final byte distWriteReg = 0x00;
        public static final byte distWriteVal = 0x04;
        public static final byte distReadReg1 = (byte)0x8F;
        public static final byte distReadReg2 = 0x10;
        public static final byte velWriteReg  = 0x04;
        public static final byte velWriteVal  = 0x08;
        public static final byte velReadReg   = 0x09;

        // Class variables to control the Laser device
        private I2CBus i2c_bus = null;
        private I2CDevice i2c_device = null;


    /**
     * Initialize the LidatLite Sensor. Input are bus ID (default is 1) and address (default 0x62)
     * @param bus_id
     * @param address
     * @return status true if sucessful, false if failed
     */
        public boolean init(int bus_id, int address ) {
            // Initialize the sensor
            try {
                // Get the i2c bus device from the OS
                this.i2c_bus = I2CFactory.getInstance(bus_id);

                // Get the device at the address
                this.i2c_device =  this.i2c_bus.getDevice(address);

                // Start to power up the Lidar Sensor
                this.i2c_device.write(TSL2561_REG_CONTROL, TSL2561_POWER_UP);

                // wait while the chip collects data
                try {
                    Thread.sleep(300);
                } catch(InterruptedException ie) {
                    System.out.println("Error: Failed to initialize the Lidar Sensor. Error " + ie);
                    return false;
                }

                // Write some values before starting measurements
                this.i2c_device.write(distWriteReg, distWriteVal);

                try {
                    Thread.sleep(100);
                } catch(InterruptedException ie) {
                    System.out.println("Error: Failed to initialize the Lidar Sensor. Error " + ie);
                    return false;
                }

                this.i2c_device.read(distReadReg1);
                this.i2c_device.read(distReadReg2);


                System.out.println("LidarLite sensor initialization complete. Ready for measurement");

                return true;

            }catch (UnsupportedBusNumberException ubne) {
                System.out.println("Error: Failed to initialize the Lidar Sensor. Error " + ubne);
                return false;
            }catch (IOException ioe) {
                System.out.println("Error: Failed to initialize the Lidar Sensor. Error " + ioe);
                return false;
            }
        }

    /**
     * POwer down the sensor
     */
    public void uninitialize() {
        // Power down the sensor
        try {
            this.i2c_device.write(TSL2561_REG_CONTROL, TSL2561_POWER_DOWN);
        } catch (IOException ioe ) {
            System.out.println("Warning: Failed to shutdown the Lidar Sensor properly");
        }
    }


    /**
     * Get the measurement from the initialized sensor
     * @return distance in cm measured by the LidarSensor
     * @throws InterruptedException
     * @throws PlatformAlreadyAssignedException
     * @throws IOException
     * @throws UnsupportedBusNumberException
     */
    public int getMeasurement() throws InterruptedException, PlatformAlreadyAssignedException, IOException, UnsupportedBusNumberException {

        int dist, dist1, dist2;

        this.i2c_device.write(distWriteReg, distWriteVal);
        // wait while the chip collects data
        Thread.sleep(50);

        dist1 = this.i2c_device.read(distReadReg1);
        Thread.sleep(20);
        dist2 = this.i2c_device.read(distReadReg2);
        Thread.sleep(20);
        dist = (dist1 << 8) + dist2;

        System.out.println("Dist One: " + dist1 + " Dist Two: " + dist2 + " Dist combined: " + dist);

        return dist;
    }
}
