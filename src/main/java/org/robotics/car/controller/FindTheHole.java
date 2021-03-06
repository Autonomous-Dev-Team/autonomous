package org.robotics.car.controller;

import java.util.ArrayList;

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
    public double getPathAngle(double[] distances, double tooFar) {
         double increment = 180 / distances.length;
         double result = 0;
         ArrayList<HoleArc> holeArcs =  new ArrayList();
         double startCritAngle = -1;
         double endCritAngle = -1;
         double startRadius = -1;
         double endRadius;
         boolean open = false;
         HoleArc bestHoleArc = null;
        for (int i = 0; i <= distances.length-1; i++)
        {
            if(distances[i] >= tooFar && open == false)
            {
                startCritAngle = i-1;
                startRadius = distances[(i-1)];
                open = true;
            } else if(open == true && !(distances[i] >= tooFar)) {
                endCritAngle = i;
                endRadius = distances[i];
                open = false;
                holeArcs.add(new HoleArc((startCritAngle*increment), (endCritAngle*increment), startRadius, endRadius));
            }
        }

        /*for(int h = holeArcs.length; h <= 0; h--)
        {
            bestHoleArc = holeArcs[0]
            if(bestHoleArc.getHoleWidth() <= holeArcs.h.getHoleWidth())
            {
                bestHoleArc = holeArcs.h;
            }
        }

        result = ((bestHoleArc.getStartCritAngle() + bestHoleArc.getEndCritAngle())/2);
*/


        HoleArc best = getBestHoleArc(holeArcs);
        if (best == null)
            return 0.0;

        return ((1.8*best.getStartCritAngle() + 1.8*best.getEndCritAngle())/2)-90;
    }


    // Private methods might be necessary
    private HoleArc getBestHoleArc(ArrayList<HoleArc> arcs)
    {
        double minAngle = Double.MAX_VALUE;
        HoleArc arcSelected = null;

        for(HoleArc arc : arcs)
        {
            double forwardX = 1;
            double forwardY = 0;
            double arcX = arc.getMidpointPositionX();
            double arcY = arc.getMidpointPositionY();

            double dotProductBetweenForwardAndOffset = forwardX*arcX + forwardY*arcY;
            double angleBetween = Math.acos( dotProductBetweenForwardAndOffset / (Math.sqrt(forwardX*forwardX + forwardY*forwardY)*Math.sqrt(arcX*arcX + arcY*arcY)));

            if(angleBetween < minAngle)
            {
                minAngle = angleBetween;
                arcSelected = arc;
            }
        }

        return arcSelected;
    }
}
