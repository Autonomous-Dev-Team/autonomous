# autonomous -- Creating a toolkit for RaspberryPI based robots
The goal is to create and continously enhance a robotics toolkit for educational purpose. Teach students to program classes
to control sensors, DC motors, LED's, stepper motors etc and to connect them.

It's all Open Source for education released under Apache2 license.

# Project pre-requisites
## Required components
PI4J libraries (http://pi4j.com)

PJ4J This project is intended to provide a friendly object-oriented I/O API and
implementation libraries for Java Programmers to access the full I/O capabilities of the Raspberry Pi platform.
This project abstracts the low-level native integration and interrupt monitoring to enable Java programmers to focus on implementing their application business logic.

Java JDK 8 -- All samples and code is built and tested with Java 8

git -- repository management tool

## Optional
maven -- Build and project management tool.


# Setup
You can either develop on your workstation and deploy the libray to the raspberry PI or do all 
development on the raspberry PI.
## Development environment Desktop
Use the IDE of your choice InteliJ (community edition is great) or Eclipse and install maven from the
Apache site http://maven.apache.org.

Clone the project from within the IDE

From the command line you can build the library using maven. This will build the library (herbie-0.0.3-SNAPSHOT.jar)
in the target directory.

Just deploy the library and the run.sh to the raspberry pi and run the car.

## Development on Raspberry PI
Make sure that the pre-requisit are installed
Java JDK 8 -- should be already on the latest Raspberry PI
PI4J libraries -- Follow the intstructions here: http://pi4j.com/install.html

Once the pre-requisits are installed do the following:
* create a project folder: mkdir ~/TLMaker
* step into the folder: cd ~/TLMaker
* Get the source code from github: git clone https://github.com/Autonomous-Dev-Team/autonomous.git

Build the library
* step into the directory that contains all build scripts: cd autonomous/raspberrypi_cmd
* Build the library: ./compile.sh

At this point you can start the car by executing: ./run.sh

After modifying the source code just execute the compile.sh script to create a new library.

On the Raspberry P you should use Geany as your editor.


# Getting started

Step By Step How to Build (Google Document):
https://docs.google.com/document/d/125Z1s9gamgt9x0oC5xvN6PVvQiNFwhoTcr-QRgdYTZ4/edit?usp=sharing

Little segments of how we did it (Google Slides):
https://docs.google.com/presentation/d/1RyZcO2RkUrDu2S91-6pBHclkt5ZsnYKfXWGcaNwbrqs/edit?usp=sharing
