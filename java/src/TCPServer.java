import java.io.*;
import java.net.*;
/**
 * Created by Thomas on 1-10-2017.
 */
class TCPServer {
    /***
     * @param database
     * @param startPort
     * @param receivers
     */
    public TCPServer(RssiDatabase database, int startPort, int receivers) {

        // Creates XX number of receiver sockets. Each with a unique port
        for(int i = 0; i < receivers; i++) {
            final int tempPort = startPort+i;
            createInputSocket(tempPort, database);
        }
    }

    /***
     *  Method for creating an input socket using a new thread.
     * @param port
     * @param database
     */
    private static void createInputSocket(int port, RssiDatabase database) {
        //mDatabase = new RssiDatabase();
        Thread t = new Thread(() -> {
            System.out.println("starting thread for socket " + port);

            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(port);
                Socket connectionSocket = serverSocket.accept();

                // While loop that reads the incoming data.
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

                    database.putBeaconRssi("" + LoPyId, "" + beaconId, RSSI);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }


}