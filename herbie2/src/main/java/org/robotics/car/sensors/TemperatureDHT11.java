package org.robotics.car.sensors;
/*
 * Implementing Temeprature Sensor module  Model DHT11
 * Datasheet: http://www.uugear.com/portfolio/dht11-humidity-temperature-sensor-module/
 *
 * @author Roger Ruttimann
 *
 ***********************************************
 * Copyright 2016 Roger Ruttimann
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

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import org.robotics.car.sensors.data.Temperature;


public class TemperatureDHT11 {
    private static final int MAXTIMINGS = 85;
    private int[] dht11_dat = {0, 0, 0, 0, 0};

    public TemperatureDHT11() {

        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        GpioUtil.export(3, GpioUtil.DIRECTION_OUT);
    }

    public Temperature getTemperature() {
        int laststate = Gpio.HIGH;
        int j = 0;
        dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;

        Gpio.pinMode(3, Gpio.OUTPUT);
        Gpio.digitalWrite(3, Gpio.LOW);
        Gpio.delay(18);

        Gpio.digitalWrite(3, Gpio.HIGH);
        Gpio.pinMode(3, Gpio.INPUT);

        for (int i = 0; i < MAXTIMINGS; i++) {
            int counter = 0;
            while (Gpio.digitalRead(3) == laststate) {
                counter++;
                Gpio.delayMicroseconds(1);
                if (counter == 255) {
                    break;
                }
            }

            laststate = Gpio.digitalRead(3);

            if (counter == 255) {
                break;
            }

      /* ignore first 3 transitions */
            if ((i >= 4) && (i % 2 == 0)) {
         /* shove each bit into the storage bytes */
                dht11_dat[j / 8] <<= 1;
                if (counter > 16) {
                    dht11_dat[j / 8] |= 1;
                }
                j++;
            }
        }
        // check we read 40 bits (8bit x 5 ) + verify checksum in the last
        // byte
        if ((j >= 40) && checkParity()) {
            float h = (float) ((dht11_dat[0] << 8) + dht11_dat[1]) / 10;
            if (h > 100) {
                h = dht11_dat[0];   // for DHT11
            }
            float c = (float) (((dht11_dat[2] & 0x7F) << 8) + dht11_dat[3]) / 10;
            if (c > 125) {
                c = dht11_dat[2];   // for DHT11
            }
            if ((dht11_dat[2] & 0x80) != 0) {
                c = -c;
            }
            float f = c * 1.8f + 32;
            return new Temperature(c,f,h);

        } else {
            System.out.println("Data not good, skip");
            return null;
        }
    }

    private boolean checkParity() {
        return (dht11_dat[4] == ((dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3]) & 0xFF));
    }
}