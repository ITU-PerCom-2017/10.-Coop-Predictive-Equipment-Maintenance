import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadPoolExecutor;

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
        //TCPServer server = new TCPServer(database, START_PORT, RECEIVERS);
        MapCanvas canvas = new MapCanvas(FRAME_WIDTH, FRAME_HEIGHT);

        JFrame frame = new JFrame("COOP Indoor Location Lap");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setBackground(Color.gray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        container.add(canvas);
        frame.show();


        for (int i = 0; i < 500; i++) {
            int id = (int)(Math.random() * 4 + 1);
            int x = (int)(Math.random() * 900 + 1);
            int y = (int)(Math.random() * 900 + 1);
            if(i<8){
                canvas.addReceiver(x,y);
            }
            try {
                canvas.addPoint(id, x, y);
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


}
