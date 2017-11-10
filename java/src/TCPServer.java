import java.io.*;
import java.net.*;
/**
 * Created by Thomas on 1-10-2017.
 */
class TCPServer {


    public TCPServer(RssiDatabase database, int startPort, int receivers) {

        // Creates XX number of receiver sockets. Each with a unique port
        for(int i = 0; i < receivers; i++) {
            final int tempPort = startPort+i;
            createInputSocket(tempPort, database);
        }
    }

    // Method for creating an input socket using a new thread.
    private void createInputSocket(int port, RssiDatabase database) {
        Thread t = new Thread(() -> {
            System.out.println("starting thread for socket " + port);

            ServerSocket serverSocket = null;

            FileWriter fw = null;
            BufferedWriter bw = null;


            try {
                serverSocket = new ServerSocket(port);
                Socket connectionSocket = serverSocket.accept();
                //fw = new FileWriter("out.txt");


                File fout = new File("out.txt");
                FileOutputStream fos = new FileOutputStream(fout);

                bw = new BufferedWriter(new OutputStreamWriter(fos));




                // While loop that reads the incoming data.
                while(true) {
                    InputStream inputStream = connectionSocket.getInputStream();

                    int LoPyId = inputStream.read();
                    int beaconId = inputStream.read();
                    int RSSI = inputStream.read();

                    //System.out.println("LoPy Id " + LoPyId + " - Beacon Id " + beaconId + " - RSSI " + RSSI);

                    // The client is not connected if the data is null.
                    // It closes the connection and open it again.
                    connectionSocket.close();
                    connectionSocket = serverSocket.accept();

                    database.putBeaconRssi("B" + beaconId, "R" + LoPyId, RSSI);

                    System.out.println("{B" + beaconId + ",R" + LoPyId + "," + RSSI + "}");
                    bw.write("{B" + beaconId + ",R" + LoPyId + "," + RSSI + "}");
                    bw.newLine();
                    bw.close();


                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (bw != null) {
                    try {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }
}