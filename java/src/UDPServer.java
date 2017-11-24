import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {

    public UDPServer(RssiDatabase database) {
        // Creates XX number of receiver sockets. Each with a unique port
        Thread t = new Thread(() -> {
            listenToData(database);

        });
        t.start();
    }
    private void listenToData(RssiDatabase database){
        long startTime = System.currentTimeMillis();
        PrintStream console = System.out;
        PrintStream o = null;
        try {
            System.out.println("log.txt file created");
            o = new PrintStream(new File("log/log.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DatagramSocket aSocket = null;
        try{
            aSocket = new DatagramSocket(7007);
            // create socket at agreed port
            while(true){
                byte[] buffer = new byte[100];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);

                byte[] data = request.getData();
                String message = new String(data);

                String[] contentOfMsg = message.split(",");
                String LoPyId = contentOfMsg[0];
                String beaconId = contentOfMsg[1];
                int RSSI = Integer.parseInt(contentOfMsg[2]);
                long timePassed = (long) ((System.currentTimeMillis() - startTime)* 0.001);
                if(timePassed != 0 && LoPyId != null && beaconId != null && RSSI != 0){
                    database.putBeaconRssi(beaconId, LoPyId, RSSI);
                    System.setOut(o);
                    System.out.println(timePassed + ","+ LoPyId +  "," + beaconId + "," + RSSI);
                    System.setOut(console);
                    System.out.println(timePassed + ","+ LoPyId +  "," + beaconId + "," + RSSI);
                }


            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }


}
