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
            generalPath = new Vector();
            GeneralPath beaconPath1, beaconPath2,beaconPath3;

            beaconPath1 = new GeneralPath();
            beaconPath1.moveTo(25, 35);
            beaconPath1.lineTo(35, 50);
            beaconPath1.lineTo(50, 20);
            beaconPath1.lineTo(80, 30);
            beaconPath1.curveTo(20, 100, 110, 60, 40, 100);
            generalPath.addElement(beaconPath1);

            beaconPath2 = new GeneralPath();
            beaconPath2.moveTo(120, 120);
            beaconPath2.lineTo(120, 180);
            beaconPath2.lineTo(180, 120);
            beaconPath2.lineTo(180, 180);
            beaconPath2.closePath();
            generalPath.addElement(beaconPath2);

            beaconPath3 = new GeneralPath();
            beaconPath3.moveTo(250, 120);
            beaconPath3.lineTo(70, 80);
            beaconPath3.lineTo(220, 40);
            beaconPath3.lineTo(280, 40);
            beaconPath3.lineTo(230, 80);
            beaconPath3.closePath();
            generalPath.addElement(beaconPath3);
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

            for (int i = 0; i < generalPath.size(); i++) {
                g2d.setPaint(Color.white);
                g2d.draw((GeneralPath) generalPath.elementAt(i));
            }
        }
    }
}
