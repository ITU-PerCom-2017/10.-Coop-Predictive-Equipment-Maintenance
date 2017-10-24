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
                ServerSocket beaconSocket = null;
                try {
                    beaconSocket = new ServerSocket(tempPort);
                    Socket connectionSocketBeacon = beaconSocket.accept();
                    BufferedReader inFromBeacon = new BufferedReader(new InputStreamReader(connectionSocketBeacon.getInputStream()));
                    String beaconSentence = inFromBeacon.readLine();
                    System.out.println("From " + connectionSocketBeacon.getInetAddress() + " : " + beaconSentence);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            t.start();
        }

    }
}