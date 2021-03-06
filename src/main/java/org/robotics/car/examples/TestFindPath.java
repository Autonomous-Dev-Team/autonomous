package org.robotics.car.examples;

/**
 * Testing routine to validate and check the implementation of finding the best path for the robot.
 *
 * The validation is to pass in array's of distance measurements and in return we'll get a signed number.
 *      Positive Turn right for the angle Example: 23.5 means turn right for 23.5 degrees
 *      Negative Turn left for the angle. Example -2.5 means turn left for 2.5 degrees
 *      0 means no change continue forward
 *      Any number bigger than 90 will move the car backwards
 *      Any number bigger than 1000 will stop the car (emergency break)
 */

import org.robotics.car.controller.FindTheHole;

public class TestFindPath {

    static FindTheHole findThePathObject = null;


    /* Entry point for the test program */
    public static void main(String[] args) throws InterruptedException {

        /*
            Test data

            Test Arrays
         */
        double[] moveRight = {1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,50,50,50,50,50,50,50,50,50,50,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10};
        double[] moveLeft  = {1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,50,50,50,50,50,50,50,50,50,50,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10};

        double[] move45Right = {1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,50,50,50,50,50,50,50,50,50,50,50,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,50,50,50,50,50,50,50,50,50,50,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10};
        double[] move10Left  = {1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,5,5,5,5,5,5,5,5,5,5,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,50,50,50,50,50,50,50,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10};

        double[] moveBackward = {10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,};
        double[] stop  = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,};


        /*
         *  Test will use 100 measurement for 180 degrees. Constructor takes increment of 1.8
         */
        findThePathObject = new FindTheHole(1.8);

        double result = 0;

        result = findThePathObject.getPathAngle(moveLeft,40);

        System.out.println("Input moveLeft outcome: " + result);
        System.out.println("Hint: we should expect a negative number around 34");

        result = findThePathObject.getPathAngle(moveRight, 40);

        System.out.println("Input moverRight outcome: " + result);
        System.out.println("Hint: we should expect a POSITIVE number around 55");

        result = findThePathObject.getPathAngle(move10Left,40);

        System.out.println("Input moveLeft outcome: " + result);
        System.out.println("Hint: we should expect a negative number around 10");

        result = findThePathObject.getPathAngle(move45Right, 40);

        System.out.println("Input moverRight outcome: " + result);
        System.out.println("Hint: we should expect a POSITIVE number around 45");


        result = findThePathObject.getPathAngle(moveBackward, 40);

        System.out.println("Input moveBackward outcome: " + result);
        System.out.println("Hint: we should expect a number greater than 90 and smaller than 1000");

        result = findThePathObject.getPathAngle(stop, 40);

        System.out.println("Input moverIGHT outcome: " + result);
        System.out.println("Hint: we should expect a number greater than 1000");


    }
}
