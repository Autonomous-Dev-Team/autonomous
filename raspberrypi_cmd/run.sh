#!/bin/bash
#
# Run the java program
#
sudo java -cp target/herbie-0.0.1-SNAPSHOT.jar:/opt/pi4j/lib/'*' org.robotics.car.controller.CarEngine

