package org.robotics.car.controller;

public class HoleArc
{
    private double   startCritAngle = 0, // Starting critical angle
                    endCritAngle = 0, // Ending critical angle
                    startRadius = 0, // Distance at starting angle
                    endRadius = 0, // Distance at ending angle.
                    startCritPositionX = 0, startCritPositionY = 0, // The start bound crit point in cartesian space.
                    endCritPositionX = 0, endCritPositionY = 0, // The end bound crit point in cartesian space
                    midpointPositionX = 0, midpointPositionY = 0, // The midpoint between the crit points in cartesian space
                    holeWidth = 0; // The size of the hole / width between crit points.

    public HoleArc(double startCritAngle, double endCritAngle, double startRadius, double endRadius)
    {
        // Initial values
        this.startCritAngle = startCritAngle;
        this.endCritAngle = endCritAngle;
        this.startRadius = startRadius;
        this.endRadius = endRadius;

        // Start / End Position
        startCritPositionX = startRadius * Math.cos(startCritAngle);
        startCritPositionY = startRadius * Math.sin(startCritAngle);

        endCritPositionX = endRadius * Math.cos(endCritAngle);
        endCritPositionY = endRadius * Math.sin(endCritAngle);

        // Midpoint Position
        midpointPositionX = ( startCritPositionX - endCritPositionX ) / 2;
        midpointPositionY = (startCritPositionY - endCritPositionY) / 2;

        // Midpoint Size
        double deltaX = startCritPositionX - endCritPositionX;
        double deltaY = startCritPositionY - endCritPositionY;
        holeWidth = Math.sqrt( deltaX*deltaX + deltaY*deltaY );
    }

    public double getStartCritAngle() {
        return startCritAngle;
    }

    public double getEndCritAngle() {
        return endCritAngle;
    }

    public double getStartRadius() {
        return startRadius;
    }

    public double getEndRadius() {
        return endRadius;
    }

    public double getStartCritPositionX() {
        return startCritPositionX;
    }

    public double getStartCritPositionY() {
        return startCritPositionY;
    }

    public double getEndCritPositionX() {
        return endCritPositionX;
    }

    public double getEndCritPositionY() {
        return endCritPositionY;
    }

    public double getMidpointPositionX() {
        return midpointPositionX;
    }

    public double getMidpointPositionY() {
        return midpointPositionY;
    }

    public double getHoleWidth() {
        return holeWidth;
    }
}
