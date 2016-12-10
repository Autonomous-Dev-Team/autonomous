package org.robotics.car.sensors.data;

import org.robotics.car.sensors.SensorType;

/*
Copyright 2016 Autonomous Open Source Project

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.

        Author: Roger Ruttimann
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
