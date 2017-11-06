import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadPoolExecutor;

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
        //TCPServer server = new TCPServer(database, START_PORT, RECEIVERS);
        MapCanvas canvas = new MapCanvas(FRAME_WIDTH, FRAME_HEIGHT);

        JFrame frame = new JFrame("COOP Indoor Location Lap");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setBackground(Color.gray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        container.add(canvas);
        frame.show();

        canvas.addReceiver(222,222);
        canvas.addReceiver(111,111);
        canvas.addReceiver(111,222);
        canvas.addReceiver(444,222);
        canvas.addReceiver(412,555);
        for (int i = 0; i < 500; i++) {

            int id = (int)(Math.random() * 4 + 1);
            int x = (int)(Math.random() * 900 + 1);
            int y = (int)(Math.random() * 900 + 1);
            try {
                canvas.addPoint(id,x,y, canvas.getGraphics() );
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


}
