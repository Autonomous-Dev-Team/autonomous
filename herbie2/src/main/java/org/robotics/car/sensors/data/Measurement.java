package org.robotics.car.sensors.data;

import org.robotics.car.sensors.SensorType;

/**
 * Created by rogerrut on 12/4/16.
 */
public abstract class Measurement {

    private String sensorName;
    private String description;
    private SensorType sensorType;

    // Constructor
    public Measurement(String name, SensorType type, String description){
        this.sensorName = name;
        this.description = description;
        this.sensorType = type;
    }

    public String getName() {
        return this.sensorName;
    }

    public SensorType getType() {
        return this.sensorType;
    }

    public String getDescription()
    {
        return this.description;
    }

    /**
     * Abstarct implementation that need to be implemented by all dirived classes
     * @param type What type of data point you expect back
     * @return value
     */
    public abstract float getValue(DataMeasured type) throws IllegalArgumentException;
}
