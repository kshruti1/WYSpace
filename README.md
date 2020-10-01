# WYSpace README
                                                      
Introduction

WY Space exercise

This program takes a schedule in text file and base station bandwidth as input. It will then find the 30 minute period where the total downlink will be at its maximum. Furthermore, it will determine whether the ground station has the bandwidth to support the best pass. And find the new best pass for the base station as per given bandwidth.

Requirements

Requirements for this program - 
1.	JDK installed on the system to run the .jar file.
2.	Source text file with schedule of satellites passes and bandwidth to pass to the program

Algorithm

1.	Read and extract data from the provided text file.
2.	Split the data according to "," in the file and store satellite name, bandwidth, start and end time of a pass in a class object called Schedule.
3.	Divided 24 hours of a day into 48 slots ( each of which is of 30 minutes duration).
4.	Divided and created 30 minutes slots of every pass and then matched them with 48 slots created earlier.
5.	Added bandwidths of every slot of a day and then searched for maximum summation from all. That is our maximum bandwidth and the passes with the same maximum bandwidth are the best passes for the day.
6.	After that, base station bandwidth is compared with maximum bandwidth we just calculated to decide whether base station can support  this new bandwidth.
7.	Furthermore, if base station bandwidth is unable to support the maximum bandwidth , we have calculated new best passes for the base station where which it can support.
8.	Additionally, we find if we have more pass with same maximum bandwidth and display that too.

Assumptions

1.	User selects appropriate schedule text file. 
2.	Base station bandwidth input range is from 0 to 99.
3.	Satellite pass starts and ends with whole hour or half hour in provided text file (e.g 05:00 to 05:30 or 16:30 to 19:00)
4.	Considering all passes are minimum of 30 minutes and passes are given for 24 hours. So we can have 48 slots in 1 day.
5.	No delay in connection with satellite.

Source code repository structure

1.	WYSpace - Project folder
2.	WYSpace/source - source code
3.	WYSpace/source/WYSpace - package folder
4.	pass-schedule.txt - with satellite passes, can be found in WYSpace folder
5.	SatellitePass.java - Application code with main method
6.	ResultFrame.java - displaying result
7.	Class SatellitePass - java application with main method and GUI
8.	Class Schedule - stores data from the file
9.	Class optimalTime - stores calculated results

Installation 

1.	Download and extract the zip folder WYSpace.zip to WYSpace
2.	Open command prompt and go to the project directory
    cd path/to/WYSpace
3.	run the application with following command
    java -jar SatellitePass.jar
 

Troubleshooting

In GUI, once entered bandwidth cannot be reset/ deleted by backspace. Solution - Please close the application and start again.
Use JAR file or Eclipse IDE to execute the application.

