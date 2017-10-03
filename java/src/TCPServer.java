import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

class TCPServer {
    public static Map<InetAddress,Beacon> dataMap = new HashMap<>();

    public static void main(String argv[]) throws Exception {


        //Server socket, idealy it should be one socket for all beacons,
        //it seams that tcp does not allow for that


        ServerSocket beaconSocket1 = new ServerSocket(6789);

        while (true) {
            /*
            Huston we have a problem, only one client can be connected to the host on
            the same port at the time, when using TCP. We can solve this
             by suing UDP or we can use MQTT. which has the features of TCP,
              but the freedom of UDP
             */


            //Instantiate a socket for beacon 1
            Socket connectionSocketBeacon1 = beaconSocket1.accept();

            //Instantiate Buffer for data comming from beacon 1
            BufferedReader inFromBeaacon1 =
                    new BufferedReader(new InputStreamReader(connectionSocketBeacon1.getInputStream()));

            //Output stream to client, for testing purposes,
            // for now we can use netcat to send messages to the server
            DataOutputStream outToClient1 = new DataOutputStream(connectionSocketBeacon1.getOutputStream());

            //message for testing purposes
            String beacon1Sentence;
            beacon1Sentence = inFromBeaacon1.readLine();

            //Create beacon object with data
            Beacon beacon = new Beacon(123123,12321,connectionSocketBeacon1.getInetAddress());

            //Add beacon to datamap, using ip adress as key and beacon object as value
            dataMap.put(connectionSocketBeacon1.getInetAddress(),beacon);

            //Prints the incomming messages on the server, for testing purposes.
            System.out.println("Received: " + beacon1Sentence);

            //Sending data back to client for testing purposes
            outToClient1.writeBytes(":" + beacon1Sentence);
        }
    }
}