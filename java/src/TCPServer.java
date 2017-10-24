import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

class TCPServer {
    private static final int PORT = 6789;
    private static PathDatabase mDatabase;

    public static void main(String argv[])  {
        for(int i = 0; i<4; i++){
            final int tempPort = PORT+i;
            Thread t = new Thread(() -> {
                ServerSocket beaconSocket = null;
                try {
                    beaconSocket = new ServerSocket(tempPort);
                    Socket connectionSocket = beaconSocket.accept();

                    while(true) {
                        BufferedReader beaconReader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                        String beaconSentence = beaconReader.readLine();
                        System.out.println("From " + connectionSocket.getInetAddress() + " : " + beaconSentence);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }

    }
}