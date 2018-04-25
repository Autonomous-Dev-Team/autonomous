package org.robotics.car.controller;

public class FindTheHole {

    // Local members !!!!!In centimeters!!!!!
    private double incrementPerIndex = 0;
    private double minObstacleDistance = 22 ;
    private double carWidth = 30.48;
    private double carLength = 43.18;

    // Constructor
    public FindTheHole(double incrementPerIndex, double minObstacleDistance, double carWidth, double carLength) {
        this.incrementPerIndex = incrementPerIndex;
        this.minObstacleDistance = minObstacleDistance;
        this.carWidth = carWidth;
        this.carLength = carLength;
    }

    // Constructor using the default values for the car Pollux model
    public FindTheHole(double incrementPerIndex){
        this.incrementPerIndex = incrementPerIndex;
    }

    /**
     * Calculates the angle to move the robot
     * @param distances
     * @return result
     *
     * Output: Return the angle between 0 - (incrementPerIndex * size of array)
     */
    public double getPathAngle(double[] distances, tooFar) {
        private double increment = 180 / distances.length;
        private double result = 0;
        private double holeArcs = [];
        private double startCritAngle;
        private double endCritAngle;
        private double startRadius;
        private double endRadius;
        private bool open = false;
        private double bestHoleArc;

        for(i = distances.length; i =< 0; i--)
        {
            if(distances.i >= tooFar && open = false)
            {
                startCritAngle = i-1;
                startRadius = distances[(i-1)];
                open = true;
            } else if(open = true && !(distances.i >= tooFar) {
                endCritAngle = i;
                endRadius = distances.i;
                open = false;
                holeArcs[].Add(new HoleArc((startCritAngle*increment), (endCritAngle*increment), startRadius, endRadius));
            }
        }

        for(h = holeArcs.length; h =< 0; h--)
        {
            bestHoleArc = holeArcs[0]
            if(bestHoleArc.getHoleWidth() <= holeArcs.h.getHoleWidth())
            {
                bestHoleArc = holeArcs.h;
            }
        }
        result = ((bestHoleArc.getStartCritAngle() + bestHoleArc.getEndCritAngle())/2);

        return result;
    }


    // Private methods might be necessary
}
