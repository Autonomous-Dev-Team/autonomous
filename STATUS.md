# Status, To Do list of autonomous self-driving model car project
Summarize progress and what needs to be done next to continue the project. Many tasks remain and any help and input are highly appreciated.

# Overview
The original project of autonomous, which resulted in three cars for the Makers Faire 2017, used basic components (4 DC motors, sonic sensors, and a Raspberry PI). Base on what was learned during the year the group decided to design and built a more robust Next Gen self-driving model car.

The current version of autonomous (Version: Next Gen) is built on top of a bigger chassis (6 wheels), bigger battery packs (7.2 V / 3.6 Amps) using a laser sensor mounted on a stepper motor to measure the distance. Having 6 DC motors and a stepper motor 2 motor shields were necessary to control the new configuration. 

The modified hardware configuration requires as an additional controller (6 motors, stepper)  and sensor (Lidar Lite 3) classes. 

# Current status Summer 2018
The status of the project can be summarized as follows:
## Hardware
* Car components (battery, motor shields, stepper, laser) fully mounted. 
* Tested individual components (stepper motor, 6 wheel drive, Laser sensor) using the current version of the Autonomous software
## Software
* Classes for controllers (6 wheels, stepper) developed and tested
* Class for Lidar Lite laser sensor developed and tested
* Classes to analyze the readings from the laser sensor (FindTheHole) developed and tested 

## To Do List
As mentioned above the individual components have been assembled, mounted and tested and what remains is to re-write the main controller: CarEngine to make use of the hardware and software elements

### Controller for Laser Sensor and Stepper motor
A new controller class needs to be created that combines the laser sensor (distance measurement routines) and the stepper function. The controller should scan 180 degrees and on each step (1.8 degrees) make a measurement. 

### CarEngine ToDo
This is the brain of autonomous connecting and controlling the motor (controllers) and sensor. The current version was used for the 1st Gen cars and needs to be re-written.

### Tasks
* Create Laser Sensor/ Stepper controller to encapsulate measurement by step and return an array of results that can be processed in the FindTheHole method.
* Create new class CarEngineNextGen
* Adjust initialization two initialize, 2 motor shields, the laser sensor, and FindTheHole class
* Implement the logic as follows:
** Initialize
** Make the measurement (Laser Sensor/ Stepper controller)
** Analyze result (FindTheGole)
** Pass the result to 6Wheel controller to adjust the direction
** Repeat measurement/analyze/motor calls until internal/external conditions are met

### Future Tasks
* Add more sensors (Sonic, touch, motion) to detect dangerous situation (hole, cliff, etc) to better control the car
* Use onboard camera to stream video to screen, device
* Use more powerful Motor shields and more batteries to support all terrain.

