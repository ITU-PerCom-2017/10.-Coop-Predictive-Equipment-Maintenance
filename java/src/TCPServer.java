import java.io.*;
import java.net.*;

class TCPServer {
    private static final int START_PORT = 6789; // Port to start from
    private static final int RECEIVERS = 4; // Number of receivers

    private static RssiDatabase mDatabase;

    // Method for creating an input socket using a new thread.
    private static void createInputSocket(int port) {
        Thread t = new Thread(() -> {

            ServerSocket serverSocket = null;

            try {
<<<<<<< HEAD
                serverSocket = new ServerSocket(port);
                Socket connectionSocket = serverSocket.accept();

                // While loop that reads the incoming data.
=======
                beaconSocket = new ServerSocket(port);
                Socket connectionSocket = beaconSocket.accept();
                if(connectionSocket.isConnected()){
                    System.out.println("Connected on socket " + port);

                }
>>>>>>> 92e547aafbc107b5a414cca4b2963c9ee7aabe27
                while(true) {
                    BufferedReader beaconReader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    String beaconSentence = beaconReader.readLine();

                    // The client is not connected if the data is null.
                    // It closes the connection and open it again.
                    if(beaconSentence == null) {
                        connectionSocket.close();
<<<<<<< HEAD
                        connectionSocket = serverSocket.accept();
=======
                        System.out.println("Connection lost on socket " + port);
                        connectionSocket = beaconSocket.accept();
                        if(connectionSocket.isConnected()){
                            System.out.println("Connected on socket " + port);

                        }g
>>>>>>> 92e547aafbc107b5a414cca4b2963c9ee7aabe27
                    }

                    // Translate the data here and store it in the rssi database. Example:
                    //mDatabase.putBeaconRssi("receiverId", "beaconId", 50);
                    System.out.println("From " + connectionSocket.getInetAddress() + " : " + beaconSentence);
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