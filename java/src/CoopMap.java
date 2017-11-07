import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnni on 03-11-2017.
 */

public class CoopMap {
    private static final int START_PORT = 6789; // Port to start from
    private static final int RECEIVERS = 4; // Number of receivers
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 600;
    private static final String TITLE = "COOP Indoor Location Map";
    private static final Color BG_COLOR = Color.gray;


    private static List<CirclePoint> mReceiverCoordinates;

    // Test method
    private static void testCoopMap(MapCanvas canvas) {

        for (int i = 0; i < 50; i++) {

            int id = (int)(Math.random() * 4 + 1);
            int x = (int)(Math.random() * FRAME_WIDTH + 1);
            int y = (int)(Math.random() * FRAME_HEIGHT + 1);


            if (i < RECEIVERS) {
                mReceiverCoordinates.add(new CirclePoint(x, y, -1));
            }

            try {
                canvas.addPoint(id, x, y);
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {

        mReceiverCoordinates = new ArrayList<>();

        RssiDatabase database = new RssiDatabase();
        //TCPServer server = new TCPServer(database, START_PORT, RECEIVERS);
        MapCanvas canvas = new MapCanvas(TITLE, FRAME_WIDTH, FRAME_HEIGHT, BG_COLOR);


        CirclePoint cp = RssiDatabase.calculateCoordinates(mReceiverCoordinates.get(0), mReceiverCoordinates.get(1), mReceiverCoordinates.get(2));

        canvas.addPoint(1, (int)cp.getX(), (int)cp.getY());


        testCoopMap(canvas);
    }
}
