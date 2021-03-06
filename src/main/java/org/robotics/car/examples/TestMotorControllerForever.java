package org.robotics.car.examples;

/*
 *  Copyright 2016 Autonomous Open Source Project
 *
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

import org.robotics.car.controller.MotorController6;

/**
 * Created by maurice on 3/18/17.
 */
public class TestMotorControllerForever {

    static MotorController6 motorController = null;


    public static void main(String[] args) throws InterruptedException {

        motorController = new MotorController6();

        boolean bLoop = true;

        while (bLoop) {

            System.out.println("Move forward for a 250ms ..");
            motorController.forward();

            System.out.println("Scan the distance ..");
            motorController.stepLeft(100);

            Thread.sleep(100);

            System.out.println("Turn left for 10 degrees ..");
            motorController.left(10);

            System.out.println("Move backward for a 250ms ..");
            motorController.backward();

            System.out.println("Scan the distance ..");
            motorController.stepRight(100);

            Thread.sleep(100);

            System.out.println("Turn right for 10 degrees ..");
            motorController.right(10);
        }

        motorController.stop();
        motorController.uninitialize();

    }
}
