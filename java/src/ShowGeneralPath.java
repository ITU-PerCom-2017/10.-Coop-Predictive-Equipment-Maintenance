import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Vector;

public class ShowGeneralPath extends JApplet {
    Canvas1 canvas;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Show General Paths");
        ShowGeneralPath path = new ShowGeneralPath();

        path.init();
        frame.getContentPane().add(path);
        frame.setSize(580, 500);
        frame.show();
    }
    public void init() {
        Container container = getContentPane();
        JPanel panel = new JPanel();
        canvas = new Canvas1();

        container.add(canvas);
    }
    class Canvas1 extends Canvas {
        Vector generalPath;

        public Canvas1() {
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
            g.drawOval(canvas.getWidth()/10,canvas.getHeight()/(beaconSize/2),beaconSize,beaconSize);
            ((Graphics2D) g).translate(-(beaconSize/2),-(beaconSize/2));
            g.drawOval(canvas.getWidth()/10*9,canvas.getHeight()/(beaconSize/2)*9,beaconSize,beaconSize);
            g.drawOval(canvas.getWidth()/10,canvas.getHeight()/(beaconSize/2)*9,beaconSize,beaconSize);
            g.drawOval(canvas.getWidth()/10*9,canvas.getHeight()/(beaconSize/2),beaconSize,beaconSize);
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
                    g2d.translate(canvas.getWidth()/2,canvas.getHeight()/2);
                    g2d.draw((GeneralPath) generalPath.elementAt(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for(int i =0; i-canvas.getWidth()/2< canvas.getWidth()/2; i++){
                for(int j =0-canvas.getHeight()/2; j<canvas.getHeight()/2; j++){
                    try {
                        Thread.sleep(10);
                        if(i<canvas.getWidth()/2) {
                            addPoint(i, j, beaconPath1);
                        }if(i==canvas.getWidth()/2){
                            i= -canvas.getWidth()*2;
                            j= -canvas.getWidth()*2;

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
