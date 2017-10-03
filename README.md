# Pervasive project
## Rapsberry Pi
will be the backend - The RPI will function as an accesspoint, which takes care of all the incomming data comming from the LoPy's, which works as recievers for the Estimote beacons. 

The Rpi will be running several java programs and it will be hosting a mySQL database


## JAVA
* Java class for handling incomming data
* Algorithm for calculating paths of custormers
* Algorithm for calculating position of items 


## LoPy - Pycon 
Recievers for Estimote beacons. The LoPy will only take care of measuring RSSI, ID, and timecode on each regisetered Estimote beacon. these dates will be passed on from the recievers (LoPy) to the backend placed on the Access point(RPI)

The LoPy communicates via WiFi with the accesspoint with the backend,  and it recieves data  bluetooth low energy on the beacons 

## Estimote beacons
Esitmote beacons will function as the marking of where each customer / basket is
