import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class MapCanvas extends Canvas{
    private Canvas mCanvas;
    //private Vector vector;
    // todo Burde det være et concurrentHashMap? er der ikke flere thread som tilgår den samtidigt?
    private Map<Integer, GeneralPath> mPaths;
    private Vector vector = new Vector();
    private Graphics2D mBeaconPath1;
    private Graphics2D mReciaverGraphics;
    private int mWidth;
    private int mHeight;
    private int RECIEVER_INDENTION_TO_MAP = 200;
    private final Color BACKGROUND_COLOR = Color.gray;

    private final Color RECIEVER_COLOR = Color.white;
    private final Color BEACON_STROKE_COLOR_1 = Color.red;
    private final Color BEACON_STROKE_COLOR_2 = Color.yellow;
    private final Color BEACON_STROKE_COLOR_3 = Color.green;
    private final Color BEACON_STROKE_COLOR_4 = Color.magenta;

    public MapCanvas(int width, int height) {
        mCanvas = new Canvas();
        mPaths = new HashMap<>();
        setBackground(BACKGROUND_COLOR);
        mWidth = width;
        mHeight = height;
        setSize(mWidth, height);
    }

    public void addReceiverPoint(int x, int y, Graphics2D g) {
        int size = 20;
        g.drawOval(x, y, size, size);

    }


    // Add point
    public void addPoint(Integer beaconId, int x, int y) {
        if(mPaths.containsKey(beaconId)){
            System.out.println("Append to beacon " + beaconId + " path");
            //If path  exist in Map, then append new path to the old
            GeneralPath oldPath = mPaths.get(beaconId);
            GeneralPath newPath = new GeneralPath();
            newPath.moveTo(x,y);
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
                mBeaconPath1.draw((GeneralPath) vector.elementAt(i));
            }
        }
    }


    public void paint(Graphics g) {
        // I Thing we need a graphic per path color
        mBeaconPath1 = (Graphics2D) g;
        mBeaconPath1.setStroke(new BasicStroke(1.5f));

        mReciaverGraphics = (Graphics2D) g;
        mReciaverGraphics.setStroke(new BasicStroke(1.5f));
        mReciaverGraphics.setPaint(RECIEVER_COLOR);
        addRecievers();

        //todo fix colors - der er et issue med at farverne på baggrund og path overskriver?
        // setPaint skulle sætte farven på hver path.
        //mBeaconPath1.setPaint(BEACON_STROKE_COLOR_1);
        for (int i = 0; i < vector.size(); i++){
            mBeaconPath1.draw((GeneralPath) vector.elementAt(i));
        }
        test();
    }
    private void addRecievers(){
        addReceiverPoint(RECIEVER_INDENTION_TO_MAP,RECIEVER_INDENTION_TO_MAP, mReciaverGraphics);
        addReceiverPoint(mWidth-RECIEVER_INDENTION_TO_MAP,mHeight-RECIEVER_INDENTION_TO_MAP,mReciaverGraphics);

        addReceiverPoint(RECIEVER_INDENTION_TO_MAP,mHeight-RECIEVER_INDENTION_TO_MAP,mReciaverGraphics);
        addReceiverPoint(mWidth-RECIEVER_INDENTION_TO_MAP,RECIEVER_INDENTION_TO_MAP,mReciaverGraphics);
    }


    private void test(){
        int min = 5;
        int max = 1000;
        for(int i=5; i<1000;i+=20){
            try {
                Thread.sleep(200);
                mBeaconPath1.setPaint(BEACON_STROKE_COLOR_1);
                addPoint(1,(int)(Math.random() * ((max - min) + 1)) + min,(int)(Math.random() * ((max - min) + 1)) + min);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        }


    }
}
