import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {

    public UDPServer(RssiDatabase database) {
        Thread t = new Thread(() -> listenForData(database));
        t.start();
        System.out.println("UDP SERVER STARTED");
    }

    private void listenForData(RssiDatabase database){
        long startTime = System.currentTimeMillis();
        PrintStream console = System.out;
        PrintStream outputFile = null;
        DatagramSocket aSocket = null;

        try {
            outputFile = new PrintStream(new File("log/log.txt"));
            System.out.println("log/log.txt PrintStream ready");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            // Creates a socket on the specified port
            aSocket = new DatagramSocket(CoopMap.PORT);

            while (true) {
                byte[] buffer = new byte[100];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);

                byte[] data = request.getData();
                String message = new String(data);
                String[] contentOfMsg = message.split(",");
                String LoPyId = "R" + contentOfMsg[0];
                String beaconId = "B" + contentOfMsg[1];
                int RSSI = Integer.parseInt(contentOfMsg[2]);
                long timePassed = (long) ((System.currentTimeMillis() - startTime)* 0.001);

                if (timePassed != 0 && LoPyId != null && beaconId != null && RSSI != 0) {
                    database.putBeaconRssi(LoPyId, beaconId, RSSI);
                    System.setOut(outputFile);
                    System.out.println(timePassed + ","+ LoPyId +  "," + beaconId + "," + RSSI);
                    System.setOut(console);
                    System.out.println(timePassed + ","+ LoPyId +  "," + beaconId + "," + RSSI);
                }
            }

        } catch (SocketException e) {System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {System.out.println("IO: " + e.getMessage());
        } finally {if (aSocket != null) aSocket.close();}
    }
}
