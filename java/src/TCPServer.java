import com.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
import com.sun.xml.internal.ws.util.ASCIIUtility;
import sun.misc.IOUtils;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPServer {
    private static final int START_PORT = 6789; // Port to start from
    private static final int RECEIVERS = 4; // Number of receivers

    private static RssiDatabase mDatabase;

    // Method for creating an input socket using a new thread.
    private static void createInputSocket(int port) {

        Thread t = new Thread(() -> {
            System.out.println("starting thread for socket " + port);

            ServerSocket serverSocket = null;
            boolean connected = false;
            try {

                serverSocket = new ServerSocket(port);
                Socket connectionSocket = serverSocket.accept();

                // While loop that reads the incoming data.
                if(connectionSocket.isConnected() && !connected){
                    connected = true;
                    System.out.println("Connected on socket " + port);

                }

                while(true) {
                    InputStream inputStream = connectionSocket.getInputStream();

                    System.out.println("inputStream. " + inputStream);
                    System.out.println("inputStream.read() " + inputStream.read());
                    byte[] result = inputMethod(inputStream);
                   // if (result.length() >= 1 ){
                        System.out.println("result " + result);
                    //}

                    // The client is not connected if the data is null.
                    // It closes the connection and open it again.
                    connectionSocket.close();
                    connectionSocket = serverSocket.accept();

                    // Translate the data here and store it in the rssi database. Example:
                    //mDatabase.putBeaconRssi("receiverId", "beaconId", 50);
                    //System.out.println("From " + connectionSocket.getInetAddress() + " : " + resultString);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
    private static byte[] inputMethod(InputStream i) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int length;

        while((length = i.read(buffer)) != -1){
            byteArrayOutputStream.write(buffer,0,length);
        }


        System.out.println("Test String ABC bytes to Ascii " + stringToBytesASCII("ABC"));
        System.out.println("Input in ASCII "+ byteArrayOutputStream.toByteArray());

        System.out.println("byteArrayOutputStream "+ byteArrayOutputStream);
        System.out.println("Input in STRING "+ asciiBytesToString(byteArrayOutputStream.toByteArray()));
        return byteArrayOutputStream.toByteArray();
       // return asciiBytesToString(byteArrayOutputStream.toByteArray());
    }

    private static byte[] stringToBytesASCII(String str) {
        byte[] b = new byte[str.length()];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) str.charAt(i);
        }
        return b;
    }
    private static String asciiBytesToString( final byte[] ascii )
    {
        //deprecated constructor allowing data to be copied directly into String char[]. So convenient...
        return new String( ascii, 0 );
    }

    public static void main(String args[]) {

        // Creates XX number of receiver sockets. Each with a unique port
        for(int i = 0; i < RECEIVERS; i++) {
            final int tempPort = START_PORT+i;
            createInputSocket(tempPort);
        }
    }
}