import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class MapCanvas extends Canvas{
    private Canvas mCanvas;
    // todo Burde det være et concurrentHashMap? er der ikke flere thread som tilgaar den samtidigt?
    private Map<Integer, GeneralPath> mPaths;
    private Vector vector = new Vector();
    private static RssiDatabase mRssiDatabase;
    private int mWidth;
    private int mHeight;
    private final Color BACKGROUND_COLOR = Color.gray;
    private final Color RECIEVER_COLOR = Color.white;
    private int RECIEVER_INDENTION_TO_MAP = 200;

    //I Think we need to make a Graphic2D pr. path, in order to color each. path differently

    //Setting color of each path
    private final List<Color> mPathColors = new ArrayList();

    public MapCanvas(RssiDatabase database,int width, int height) {
        mCanvas = new Canvas();
        mPaths = new HashMap<>();
        mRssiDatabase = database;
        setBackground(BACKGROUND_COLOR);
        mWidth = width;
        mHeight = height;
        setSize(mWidth, height);
        mPathColors.add(Color.red);
        mPathColors.add(Color.yellow);
        mPathColors.add(Color.green);
        mPathColors.add(Color.pink);
    }

    /***
     * Method for creating paths / or adding points to existing paths.
     * @param beaconId
     * @param x choordinate as int
     * @param y choordinate as int
     * @param g Graphics2D -
     */
    public void addPoint(Integer beaconId, int x, int y, Graphics2D g) {
        g.setStroke(new BasicStroke(1.5f));

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
                g.draw((GeneralPath) vector.elementAt(beaconId-1));
                g.setPaint(mPathColors.get(beaconId-1));
            }
        }
    }


    public void paint(Graphics g) {
        Graphics2D mReceiverGraphics = (Graphics2D) g;
        addReceivers(mReceiverGraphics);
        //todo fix mPathColors - der er et issue med at farverne paa baggrund og path overskriver?
        // setPaint skulle sætte farven p ' hver path.
        // I Think we need a graphic per path color
        Graphics2D mBeaconPath1 = (Graphics2D) g;
        Graphics2D mBeaconPath2 = (Graphics2D) g;
        Graphics2D mBeaconPath3 = (Graphics2D) g;
        Graphics2D mBeaconPath4 = (Graphics2D) g;
        test(1,mBeaconPath1);
        test(2,mBeaconPath2);
        test(3,mBeaconPath3);
        test(4,mBeaconPath4);

    }

    private void addReceivers(Graphics2D g){
        addReceiverPoint(RECIEVER_INDENTION_TO_MAP,RECIEVER_INDENTION_TO_MAP, g);
        addReceiverPoint(mWidth-RECIEVER_INDENTION_TO_MAP,mHeight-RECIEVER_INDENTION_TO_MAP, g);
        addReceiverPoint(RECIEVER_INDENTION_TO_MAP,mHeight-RECIEVER_INDENTION_TO_MAP, g);
        addReceiverPoint(mWidth-RECIEVER_INDENTION_TO_MAP,RECIEVER_INDENTION_TO_MAP, g);
    }
    private void addReceiverPoint(int x, int y, Graphics2D g) {
        g.setStroke(new BasicStroke(1.5f));
        g.setPaint(RECIEVER_COLOR);
        int size = 20;
        g.drawOval(x, y, size, size);

    }
    private void insertFromDb(){
    }
    /***
     * Test for drawing paths.
     * @param beaconId - which beacon to draw
     * @param g -Graphics2D
     */
    private void test(int beaconId, Graphics2D g){
        int min = 50;
        int max = 950;
        for(int i=5; i<100;i+=2){
            try {
                Thread.sleep(20);
                //int beacon =(int)(Math.random() * ((4 - 1) + 1)) + 1;
                int beacon = beaconId;
                addPoint(beacon,(int)(Math.random() * ((max - min) + 1)) + min,(int)(Math.random() * ((max - min) + 1)) + min , g );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
