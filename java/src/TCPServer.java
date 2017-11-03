

import java.io.*;
import java.net.*;

class TCPServer {
    private static final int START_PORT = 6789; // Port to start from
    private static final int RECEIVERS = 4; // Number of receivers

    private static RssiDatabase mDatabase;

    // Method for creating an input socket using a new thread.
    private static void createInputSocket(int port) {
        mDatabase = new RssiDatabase();
        Thread t = new Thread(() -> {
            System.out.println("starting thread for socket " + port);

            ServerSocket serverSocket = null;
            boolean connected = false;
            try {

                serverSocket = new ServerSocket(port);
                Socket connectionSocket = serverSocket.accept();

                // While loop that reads the incoming data.
                if(connectionSocket.isConnected() && !connected){
                    connected = true;
                    System.out.println("Connected on socket " + port);

                }

                while(true) {
                    InputStream inputStream = connectionSocket.getInputStream();

                    int LoPyId = inputStream.read();
                    int beaconId = inputStream.read();
                    int RSSI = inputStream.read();

                    System.out.println("LoPy Id " + LoPyId + " beacon Id " + beaconId + " RSSI " + RSSI);

                    // The client is not connected if the data is null.
                    // It closes the connection and open it again.
                    connectionSocket.close();
                    connectionSocket = serverSocket.accept();

                    // Translate the data here and store it in the rssi database. Example:
                    mDatabase.putBeaconRssi(""+ LoPyId, "" + beaconId, RSSI);
                    //System.out.println("From " + connectionSocket.getInetAddress() + " : " + resultString);

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