import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Vector;

public class MapGenerator extends JApplet {
    private MyCanvas mCanvas;


    public MapGenerator() {

    }

    public void init() {
        Container container = getContentPane();
        JPanel panel = new JPanel();
        mCanvas = new MyCanvas();

        container.add(mCanvas);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("COOP Indoor Location Lap");
        MapGenerator path = new MapGenerator();

        path.init();
        frame.getContentPane().add(path);
        frame.setSize(580, 500);
        frame.show();
    }





    class MyCanvas extends Canvas {
        private Vector generalPath;

        public MyCanvas() {
            setBackground(Color.gray);
            setSize(400, 200);
        }

        public void addPoint(int startX, int startY,GeneralPath appendToPath){
            GeneralPath tempPath;
            tempPath = new GeneralPath();
            tempPath.moveTo(startX,startY);
            tempPath.lineTo(startX,startY);
            appendToPath.append(tempPath,true);
        }

        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int beaconSize = 20;
            ((Graphics2D) g).translate(-(beaconSize/2),-(beaconSize/2));
            g.drawOval(mCanvas.getWidth()/10, mCanvas.getHeight()/(beaconSize/2),beaconSize,beaconSize);
            ((Graphics2D) g).translate(-(beaconSize/2),-(beaconSize/2));
            g.drawOval(mCanvas.getWidth()/10*9, mCanvas.getHeight()/(beaconSize/2)*9,beaconSize,beaconSize);
            g.drawOval(mCanvas.getWidth()/10, mCanvas.getHeight()/(beaconSize/2)*9,beaconSize,beaconSize);
            g.drawOval(mCanvas.getWidth()/10*9, mCanvas.getHeight()/(beaconSize/2),beaconSize,beaconSize);
            generalPath = new Vector();
            GeneralPath beaconPath1;

            beaconPath1 = new GeneralPath();
            beaconPath1.moveTo(25, 35);
            beaconPath1.lineTo(35, 50);
            beaconPath1.lineTo(50, 20);
            beaconPath1.lineTo(80, 30);
            beaconPath1.curveTo(20, 100, 110, 60, 40, 100);
            generalPath.addElement(beaconPath1);
            for (int i = 0; i < generalPath.size(); i++){
                try {
                    Thread.sleep(1000);
                    g2d.setPaint(Color.white);
                    //Centers coordinates on screen. (0,0)
                    g2d.translate(mCanvas.getWidth()/2, mCanvas.getHeight()/2);
                    g2d.draw((GeneralPath) generalPath.elementAt(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for(int i = 0; i- mCanvas.getWidth()/2< mCanvas.getWidth()/2; i++){
                for(int j = 0- mCanvas.getHeight()/2; j< mCanvas.getHeight()/2; j++){
                    try {
                        Thread.sleep(10);
                        if(i< mCanvas.getWidth()/2) {
                            addPoint(i, j, beaconPath1);
                        }if(i== mCanvas.getWidth()/2){
                            i= -mCanvas.getWidth()*2;
                            j= -mCanvas.getWidth()*2;

                        }
                        g2d.draw((GeneralPath) generalPath.elementAt(0));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
