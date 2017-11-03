import javax.swing.*;
import java.awt.*;

/**
 * Created by Johnni on 03-11-2017.
 */
public class CoopMap {
    private static final int START_PORT = 6789; // Port to start from
    private static final int RECEIVERS = 4; // Number of receivers
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 1000;


    public static void main(String[] args) {
        RssiDatabase database = new RssiDatabase();
        TCPServer server = new TCPServer(database, START_PORT, RECEIVERS);
        MapCanvas canvas = new MapCanvas(FRAME_WIDTH,FRAME_HEIGHT);

        JFrame frame = new JFrame("COOP Indoor Location Lap");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        Container container = frame.getContentPane();
        container.add(canvas);
        frame.show();
    }


}
