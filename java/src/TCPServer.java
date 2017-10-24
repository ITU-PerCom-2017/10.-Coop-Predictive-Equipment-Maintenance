import java.io.*;
import java.net.*;

class TCPServer {
    private static final int START_PORT = 6789; // Port to start from
    private static final int RECEIVERS = 4; // Number of receivers

    private static RssiDatabase mDatabase;

    private static void createInputSocket(int port) {
        Thread t = new Thread(() -> {

            ServerSocket beaconSocket = null;
            try {
                beaconSocket = new ServerSocket(port);
                Socket connectionSocket = beaconSocket.accept();
                while(true) {
                    BufferedReader beaconReader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    String beaconSentence = beaconReader.readLine();
                    if(beaconSentence == null) {
                        connectionSocket.close();
                        connectionSocket = beaconSocket.accept();
                    }
                    System.out.println("From " + connectionSocket.getInetAddress() + " : " + beaconSentence);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }



    public static void main(String argv[])  {

        for(int i = 0; i < RECEIVERS; i++) {
            final int tempPort = START_PORT+i;
            createInputSocket(tempPort);
        }
    }
}