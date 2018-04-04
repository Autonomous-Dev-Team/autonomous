package org.robotics.car.controller;

public class FindTheHole {

    // Local members
    private double incrementPerIndex = 0;
    private double minObstacleDistance =22 ;
    private double carWidth =30.48;
    private double carLength = 43.18;

    // Constructor
    public FindTheHole(double incrementPerIndex, double minObstacleDistance, double carWidth, double carLength) {
        this.incrementPerIndex = incrementPerIndex;
        this.minObstacleDistance = minObstacleDistance;
        this.carWidth = carWidth;
        this.carLength = carLength;
    }

    /**
     * Calculates the angle to move the robot
     * @param distances
     * @return result
     *
     * Output: Return the angle between 0 - (incrementPerIndex * size of array)
     */
    public double getPathAngle(double[] distances) {
        double result = 0;

        // TBD

        return result;
    }


    // Private methods might be necessary
}
