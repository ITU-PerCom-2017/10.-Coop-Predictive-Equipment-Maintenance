import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class MapCanvas extends Canvas{
    private Canvas mCanvas;
    // todo Burde det vaere et concurrentHashMap? er der ikke flere thread som tilgaar den samtidigt?
    private Map<Integer, GeneralPath> mPaths;
    private Vector vector = new Vector();
    private static RssiDatabase mRssiDatabase;
    private int mWidth;
    private int mHeight;
    private final Color BACKGROUND_COLOR = Color.gray;
    private final Color RECIEVER_COLOR = Color.white;
    private int RECIEVER_INDENTION_TO_MAP = 200;
    private final List<Color> PATH_COLORS = new ArrayList();

    public MapCanvas(RssiDatabase database,int width, int height) {
        mCanvas = new Canvas();
        mPaths = new HashMap<>();
        mRssiDatabase = database;
        mWidth = width;
        mHeight = height;
        setSize(mWidth, mHeight);
        setBackground(BACKGROUND_COLOR);
        PATH_COLORS.add(Color.red);
        PATH_COLORS.add(Color.yellow);
        PATH_COLORS.add(Color.green);
        PATH_COLORS.add(Color.pink);

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
            //If path  exist in Map, then append new path to the old
            GeneralPath oldPath = mPaths.get(beaconId);
            GeneralPath newPath = new GeneralPath();

            newPath.moveTo(x,y);
            newPath.lineTo(x,y);

            oldPath.append(newPath,true);

        } else {
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
                g.setPaint(PATH_COLORS.get(beaconId-1));
            }
        }
    }


    public void paint(Graphics g) {
        Graphics2D mReceiverGraphics = (Graphics2D) g;
        addReceivers(mReceiverGraphics);
        //todo fix PATH_COLORS - der er et issue med at farverne paa baggrund og path overskriver?
        // setPaint skulle saette farven p ' hver path.
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

    /***
     * Add demo receivers
     * @param g
     */
    private void addReceivers(Graphics2D g){
        addReceiverPoint(RECIEVER_INDENTION_TO_MAP,RECIEVER_INDENTION_TO_MAP, g);
        addReceiverPoint(mWidth-RECIEVER_INDENTION_TO_MAP,mHeight-RECIEVER_INDENTION_TO_MAP, g);
        addReceiverPoint(RECIEVER_INDENTION_TO_MAP,mHeight-RECIEVER_INDENTION_TO_MAP, g);
        addReceiverPoint(mWidth-RECIEVER_INDENTION_TO_MAP,RECIEVER_INDENTION_TO_MAP, g);
    }

    /***
     * Add single receiver on coordinate
     * @param x
     * @param y
     * @param g
     */
    private void addReceiverPoint(int x, int y, Graphics2D g) {
        g.setStroke(new BasicStroke(1.5f));
        g.setPaint(RECIEVER_COLOR);
        int size = 20;
        g.drawOval(x, y, size, size);

    }

    /***
     * Test for drawing paths.
     * @param beaconId - which beacon to draw
     * @param g -Graphics2D
     */
    private void test(int beaconId, Graphics2D g){
        int min = 50;
        int maxWidth = CoopMap.FRAME_WIDTH-50;
        int maxHeight = CoopMap.FRAME_HEIGHT-50;

        for(int i=5; i<100;i+=2){
            try {
                Thread.sleep(20);
                //int beacon =(int)(Math.random() * ((4 - 1) + 1)) + 1;
                int beacon = beaconId;
                addPoint(beacon,(int)(Math.random() * ((maxWidth - min) + 1)) + min,(int)(Math.random() * ((maxHeight - min) + 1)) + min , g );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
