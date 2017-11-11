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
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(() -> {
            System.out.println("starting thread for socket " + port);
            ServerSocket serverSocket = null;
            // Store current System.out before assigning a new value
            PrintStream console = System.out;
            //Create logfile and set output stream
            PrintStream o = null;
            try {
                System.out.println(port + ".txt file created");
                o = new PrintStream(new File("log/"+port + ".txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                serverSocket = new ServerSocket(port);
                Socket connectionSocket = serverSocket.accept();

                // While loop that reads the incoming data.
                while(true) {
                    InputStream inputStream = connectionSocket.getInputStream();
                    int LoPyId = inputStream.read();
                    int beaconId = inputStream.read();
                    int RSSI = inputStream.read();
                    long timePassed = (long) ((System.currentTimeMillis() - startTime)* 0.001);
                    // The client is not connected if the data is null.
                    // It closes the connection and open it again.
                    connectionSocket.close();
                    connectionSocket = serverSocket.accept();

                    //Write data to log file
                    database.putBeaconRssi("B" + beaconId, "R" + LoPyId, RSSI);
                    System.setOut(o);
                    System.out.println("Time " + timePassed + " , B" + beaconId + "," + RSSI + "");
                    System.setOut(console);
                    System.out.println("R" + LoPyId + ", Time " + timePassed + " , B" + beaconId + "," + RSSI + "");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        });
        t.start();
    }
}