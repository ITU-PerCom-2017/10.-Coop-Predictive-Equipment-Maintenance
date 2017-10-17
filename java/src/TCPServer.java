import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

class TCPServer {
    private static PathDatabase mDatabase;

    public static void main(String argv[]) throws Exception {


        //Server socket, ideally it should be one socket for all beacons,
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
            BufferedReader inFromBeacon1 =
                    new BufferedReader(new InputStreamReader(connectionSocketBeacon1.getInputStream()));

            BufferedReader inFromBeacon2 =
                    new BufferedReader(new InputStreamReader(connectionSocketBeacon2.getInputStream()));

            BufferedReader inFromBeacon3 =
                    new BufferedReader(new InputStreamReader(connectionSocketBeacon3.getInputStream()));

            BufferedReader inFromBeacon4 =
                    new BufferedReader(new InputStreamReader(connectionSocketBeacon4.getInputStream()));


            //message for testing purposes
            String beacon1Sentence = inFromBeacon1.readLine();
            String beacon2Sentence = inFromBeacon2.readLine();
            String beacon3Sentence = inFromBeacon3.readLine();
            String beacon4Sentence = inFromBeacon4.readLine();



            //Prints the incoming messages on the server, for testing purposes.
            System.out.println("From " + connectionSocketBeacon1.getInetAddress() + " : " + beacon1Sentence);
            System.out.println("From " + connectionSocketBeacon2.getInetAddress() + " : " + beacon2Sentence);
            System.out.println("From " + connectionSocketBeacon3.getInetAddress() + " : " + beacon3Sentence);
            System.out.println("From " + connectionSocketBeacon4.getInetAddress() + " : " + beacon4Sentence);

        }
    }
}