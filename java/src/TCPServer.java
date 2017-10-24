import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

class TCPServer {
    private static PathDatabase mDatabase;

    public static void main(String argv[])  {
        int port = 6789;
        for(int i = 0; i<4; i++){
            final int tempPort = port+i;
            Thread t = new Thread(() -> {
                ServerSocket beaconSocket1 = null;
                try {
                    beaconSocket1 = new ServerSocket(tempPort);
                    Socket connectionSocketBeacon1 = beaconSocket1.accept();
                    BufferedReader inFromBeacon1 = new BufferedReader(new InputStreamReader(connectionSocketBeacon1.getInputStream()));
                    //message for testing purposes
                    String beacon1Sentence = inFromBeacon1.readLine();
                    //Prints the incoming messages on the server, for testing purposes.
                    System.out.println("From " + connectionSocketBeacon1.getInetAddress() + " : " + beacon1Sentence);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            t.start();
        }

    }
}