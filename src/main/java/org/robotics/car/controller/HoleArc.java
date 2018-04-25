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


    // IMPORTANT: ANGLES ARE IN DEGREES!!!
    public HoleArc(double startCritAngle, double endCritAngle, double startRadius, double endRadius)
    {
        // Initial values
        this.startCritAngle = startCritAngle;
        this.endCritAngle = endCritAngle;
        this.startRadius = startRadius;
        this.endRadius = endRadius;

        // Start / End Position. Add 90 degrees to make 0 degrees positive Y axis
        startCritPositionX = startRadius * Math.cos(Math.toRadians(startCritAngle));
        startCritPositionY = startRadius * Math.sin(Math.toRadians(startCritAngle));

        endCritPositionX = endRadius * Math.cos(Math.toRadians(endCritAngle));
        endCritPositionY = endRadius * Math.sin(Math.toRadians(endCritAngle));

        // Midpoint Position
        midpointPositionX = ( startCritPositionX + endCritPositionX ) / 2;
        midpointPositionY = (startCritPositionY + endCritPositionY) / 2;

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

    @Override
    public String toString() {
        return "HoleArc{" +
                "startCritAngle=" + startCritAngle +
                ", endCritAngle=" + endCritAngle +
                ", startRadius=" + startRadius +
                ", endRadius=" + endRadius +
                ", startCritPositionX=" + startCritPositionX +
                ", startCritPositionY=" + startCritPositionY +
                ", endCritPositionX=" + endCritPositionX +
                ", endCritPositionY=" + endCritPositionY +
                ", midpointPositionX=" + midpointPositionX +
                ", midpointPositionY=" + midpointPositionY +
                ", holeWidth=" + holeWidth +
                '}';
    }

}

