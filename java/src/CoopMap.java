import javax.swing.*;
import java.awt.*;

/**
 * Created by Johnni on 03-11-2017.
 */
public class CoopMap {
    private static final int START_PORT = 6789; // Port to start from
    private static final int RECEIVERS = 4; // Number of receivers
    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT = 600;


    public static void main(String[] args) {
        RssiDatabase database = new RssiDatabase();
        TCPServer server = new TCPServer(database, START_PORT, RECEIVERS);
        MapCanvas canvas = new MapCanvas(FRAME_WIDTH,FRAME_HEIGHT);
        JFrame frame = new JFrame("COOP Indoor Location Lap");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        Container container = frame.getContentPane();
       // canvas.addPoint(200,200);
        container.add(canvas);
        canvas.addPoint(1,110,222);
        canvas.addPoint(1,12,22);
        canvas.addPoint(1,1000,522);
        canvas.addPoint(2,120,222);
        canvas.addPoint(2,152,22);
        canvas.addPoint(2,100,522);
        canvas.addReceiver(222,222);
        frame.show();

    }


}
