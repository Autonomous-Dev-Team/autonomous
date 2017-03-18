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

import org.robotics.car.controller.MotorController;

/**
 * Created by maurice on 3/18/17.
 */
public class TestMotorController {

    static MotorController motorController = null;


    public static void main(String[] args) throws InterruptedException {

        motorController = new MotorController("carpet", "M1", "M2", "M3", "M4");

        motorController.forward();

        Thread.sleep(1000);

        motorController.left(180);

        motorController.forward();

        Thread.sleep(1000);

        motorController.right(180);

    }

}
