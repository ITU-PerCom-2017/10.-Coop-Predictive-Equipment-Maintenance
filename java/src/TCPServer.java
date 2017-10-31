import sun.misc.IOUtils;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPServer {
    private static final int START_PORT = 6789; // Port to start from
    private static final int RECEIVERS = 4; // Number of receivers

    private static RssiDatabase mDatabase;

    // Method for creating an input socket using a new thread.
    private static void createInputSocket(int port) {
        Thread t = new Thread(() -> {

            ServerSocket serverSocket = null;

            try {

                serverSocket = new ServerSocket(port);
                Socket connectionSocket = serverSocket.accept();

                // While loop that reads the incoming data.
                if(connectionSocket.isConnected()){
                    System.out.println("Connected on socket " + port);

                }

                while(true) {

//                    String beaconSentence =  new BufferedInputStream(new InputStreamReader(connectionSocket.getInputStream()));
                    Scanner s = new Scanner(connectionSocket.getInputStream()).useDelimiter("&#092");
                    String results = s.hasNext() ? s.next() : "";

                    // The client is not connected if the data is null.
                    // It closes the connection and open it again.
                    if(results == null) {

                        connectionSocket.close();

                        System.out.println("Connection lost on socket " + port);
                        connectionSocket = serverSocket.accept();

                        if(connectionSocket.isConnected()){
                            System.out.println("Connected on socket " + port);

                        }
                    }

                    // Translate the data here and store it in the rssi database. Example:
                    //mDatabase.putBeaconRssi("receiverId", "beaconId", 50);
                    System.out.println("From " + connectionSocket.getInetAddress() + " : " + results);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }



    public static void main(String args[]) {

        // Creates XX number of receiver sockets. Each with a unique port
        for(int i = 0; i < RECEIVERS; i++) {
            final int tempPort = START_PORT+i;
            createInputSocket(tempPort);
        }
    }
}