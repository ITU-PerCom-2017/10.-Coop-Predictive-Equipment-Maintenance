import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

class TCPServer {
    public static Map<InetAddress,Beacon> dataMap = new HashMap<>();

    public static void main(String argv[]) throws Exception {
        String beacon1Sentence;
        String capitalizedSentence1;
        ServerSocket beaconSocket1 = new ServerSocket(6789);

        while (true) {
            Socket connectionSocketBeacon1 = beaconSocket1.accept();
            BufferedReader inFromBeaacon1 =
                    new BufferedReader(new InputStreamReader(connectionSocketBeacon1.getInputStream()));
            DataOutputStream outToClient1 = new DataOutputStream(connectionSocketBeacon1.getOutputStream());
            beacon1Sentence = inFromBeaacon1.readLine();
            Beacon beacon = new Beacon(123123);
            dataMap.put(connectionSocketBeacon1.getInetAddress(),beacon);
            System.out.println("Received: " + beacon1Sentence);
            capitalizedSentence1 = beacon1Sentence.toUpperCase() + '\n';
            outToClient1.writeBytes(capitalizedSentence1);
        }
    }
}