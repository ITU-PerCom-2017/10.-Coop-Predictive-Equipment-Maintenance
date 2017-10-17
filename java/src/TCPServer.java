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
        ServerSocket beaconSocket2 = new ServerSocket(6790);
        ServerSocket beaconSocket3 = new ServerSocket(6791);
        ServerSocket beaconSocket4 = new ServerSocket(6792);


        while (true) {
            //Instantiate sockets
            Socket connectionSocketBeacon1 = beaconSocket1.accept();
            Socket connectionSocketBeacon2 = beaconSocket2.accept();
            Socket connectionSocketBeacon3 = beaconSocket3.accept();
            Socket connectionSocketBeacon4 = beaconSocket4.accept();

            //Instantiate Buffers
            BufferedReader inFromBeaacon1 =
                    new BufferedReader(new InputStreamReader(connectionSocketBeacon1.getInputStream()));

            BufferedReader inFromBeaacon2 =
                    new BufferedReader(new InputStreamReader(connectionSocketBeacon2.getInputStream()));
            BufferedReader inFromBeaacon3 =
                    new BufferedReader(new InputStreamReader(connectionSocketBeacon3.getInputStream()));

            BufferedReader inFromBeaacon4 =
                    new BufferedReader(new InputStreamReader(connectionSocketBeacon4.getInputStream()));


            //message for testing purposes
            String beacon1Sentence = inFromBeaacon1.readLine();
            String beacon2Sentence = inFromBeaacon2.readLine();
            String beacon3Sentence = inFromBeaacon3.readLine();
            String beacon4Sentence = inFromBeaacon4.readLine();


            //Create beacon object with data
            Beacon beacon1 = new Beacon(2951,54413,12321,connectionSocketBeacon1.getInetAddress());
            Beacon beacon2 = new Beacon(43684,33774,12321,connectionSocketBeacon2.getInetAddress());
            Beacon beacon3 = new Beacon(60236,2927,12321,connectionSocketBeacon3.getInetAddress());
            Beacon beacon4 = new Beacon(36290,59043,12321,connectionSocketBeacon4.getInetAddress());

            //Add beacon to datamap, using ip adress as key and beacon object as value
            dataMap.put(connectionSocketBeacon1.getInetAddress(),beacon1);
            dataMap.put(connectionSocketBeacon2.getInetAddress(),beacon2);
            dataMap.put(connectionSocketBeacon3.getInetAddress(),beacon3);
            dataMap.put(connectionSocketBeacon4.getInetAddress(),beacon4);


            //Prints the incomming messages on the server, for testing purposes.
            System.out.println("From " + connectionSocketBeacon1.getInetAddress() + " : " + beacon1Sentence);
            System.out.println("From " + connectionSocketBeacon2.getInetAddress() + " : " + beacon2Sentence);
            System.out.println("From " + connectionSocketBeacon3.getInetAddress() + " : " + beacon3Sentence);
            System.out.println("From " + connectionSocketBeacon4.getInetAddress() + " : " + beacon4Sentence);

        }
    }
}