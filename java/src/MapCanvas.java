import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class MapCanvas extends Canvas{
    private Canvas mCanvas;
    //private Vector vector;
    private Map<Integer, GeneralPath> mPaths;
    private Vector vector = new Vector();

    private Graphics2D mG2d;

    public MapCanvas() {
        mCanvas = new Canvas();
        mPaths = new HashMap<>();


    }

    public void addReceiverPoint(int x, int y) {
        int size = 20;
        mG2d.drawOval(x, y, size, size);
    }


    public void addPoint(Integer beaconId, int x, int y) {
        if(mPaths.containsKey(beaconId)){
            GeneralPath newPath = new GeneralPath();
            newPath.moveTo(x,y);
            newPath.lineTo(x,y);
            vector.addElement(newPath);
        }
    }


    public void addPoint(int startX, int startY, GeneralPath appendToPath) {
        GeneralPath tempPath = new GeneralPath();
        tempPath.moveTo(startX,startY);
        tempPath.lineTo(startX,startY);
        appendToPath.append(tempPath,true);
    }

    public void paint(Graphics g) {
        mG2d = (Graphics2D) g;
        mG2d.setPaint(Color.white);

        vector = new Vector();
        for (int i = 0; i < vector.size(); i++){
            mG2d.draw((GeneralPath) vector.elementAt(i));
        }
    }
}
