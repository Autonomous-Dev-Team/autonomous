#!/bin/bash
#
# Run the java program
#
sudo java -cp target/herbie-0.0.3-SNAPSHOT.jar:/opt/pi4j/lib/'*' com.pi4j.component.adafruithat.example.Test6DCMotors$1

