import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class MapCanvas extends Canvas{
    private Canvas mCanvas;
    //private Vector vector;
    // todo Burde det være et concurrentHashMap? er der ikke flere thread som tilgår den samtidigt?
    private Map<Integer, GeneralPath> mPaths;
    private Vector vector = new Vector();
    private Graphics2D mG2d;
    private Graphics2D mBackground;

    public MapCanvas() {
        mCanvas = new Canvas();
        mPaths = new HashMap<>();
        setBackground(Color.black);
        setSize(1000, 1000);
    }

    public void addReceiverPoint(int x, int y) {
        int size = 20;
        mG2d.drawOval(x, y, size, size);
    }


    // Add point
    public void addPoint(Integer beaconId, int x, int y) {
        float tempX,tempY;
        if(mPaths.containsKey(beaconId)){
            System.out.println("Append to beacon " + beaconId + " path");
            //If path  exist in Map, then append new path to the old
            GeneralPath oldPath = mPaths.get(beaconId);
            GeneralPath newPath = new GeneralPath();
            newPath.moveTo(x,y);
            //newPath.lineTo(x,y);
            newPath.lineTo(x,y);

            oldPath.append(newPath,true);

        } else {
            System.out.println("Add beaconPath" + beaconId );
            //If path does not exist in Map, then create new path
            GeneralPath newPath = new GeneralPath();
            newPath.moveTo(x,y);
            newPath.lineTo(x,y);
            vector.addElement(newPath);
            mPaths.put(beaconId,newPath);
        }
        if(!vector.isEmpty()){
            for (int i = 0; i < vector.size(); i++){
                mG2d.draw((GeneralPath) vector.elementAt(i));
            }
        }
    }


    public void paint(Graphics g) {
        // I Thing we need a graphic per path color
        mG2d = (Graphics2D) g;
        mG2d.setStroke(new BasicStroke(1.5f));
        //todo fix colors - der er et issue med at farverne på baggrund og path overskriver?
        // setPaint skulle sætte farven på hver path.
        mG2d.setPaint(Color.white);
        for (int i = 0; i < vector.size(); i++){
            mG2d.draw((GeneralPath) vector.elementAt(i));
        }
        test();

    }
    private void test(){
        int min = 5;
        int max = 1000;
        for(int i=5; i<1000;i+=20){
                try {
                    Thread.sleep(10);
                    addPoint(1,(int)(Math.random() * ((max - min) + 1)) + min,(int)(Math.random() * ((max - min) + 1)) + min);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

    }
}
