import java.awt.*;

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

    // Test method
    private static void testCoopMap(MapCanvas canvas) {

        canvas.addReceiver(222,222);
        canvas.addReceiver(111,111);
        canvas.addReceiver(111,222);
        canvas.addReceiver(444,222);

        for (int i = 0; i < 500; i++) {

            int id = (int)(Math.random() * 4 + 1);
            int x = (int)(Math.random() * 900 + 1);
            int y = (int)(Math.random() * 900 + 1);

            try {
                canvas.addPoint(id, x, y);
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        RssiDatabase database = new RssiDatabase();
        //TCPServer server = new TCPServer(database, START_PORT, RECEIVERS);
        MapCanvas canvas = new MapCanvas(TITLE, FRAME_WIDTH, FRAME_HEIGHT, BG_COLOR);

        testCoopMap(canvas);
    }
}
