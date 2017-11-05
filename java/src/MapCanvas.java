import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class MapCanvas extends Canvas{
    // todo Burde det vaere et concurrentHashMap? er der ikke flere thread som tilgaar den samtidigt?
    private Map<Integer, GeneralPath> mPaths;
    private List<GeneralPath> mReceivers;
    private Vector mVector;
    private int mWidth;
    private int mHeight;
    private final Color BACKGROUND_COLOR = Color.gray;
    private final Color RECIEVER_COLOR = Color.white;

    public MapCanvas(int width, int height) {
        mPaths = new HashMap<>();
        mVector = new Vector();
        mReceivers = new ArrayList<>();
        mWidth = width;
        mHeight = height;
        setSize(mWidth, mHeight);
        setBackground(BACKGROUND_COLOR);
    }

    /***
     * Method for creating paths / or adding points to existing paths.
     * @param beaconId
     * @param x choordinate as int
     * @param y choordinate as int
     */
    public void addPoint(Integer beaconId, int x, int y) {
        if(mPaths.containsKey(beaconId)){
            //If path  exist in Map, then append new path to the old
            GeneralPath newPath = new GeneralPath();
            newPath.moveTo(x,y);
            newPath.lineTo(x,y);
            mPaths.get(beaconId).append(newPath,true);
        } else {
            //If path does not exist in Map, then create new path
            GeneralPath newPath = new GeneralPath();
            newPath.moveTo(x,y);
            newPath.lineTo(x,y);
            mVector.addElement(newPath);
            mPaths.put(beaconId,newPath);
        }

    }

    /***
     * Add single receiver on coordinate
     * @param x
     * @param y
     */
    public void addReceiver(int x, int y) {
        mReceivers.add(ne
    }

    private Color getRandomColor(){
        int randomR = (int)(Math.random() * 255 + 1);
        int randomG = (int)(Math.random() * 255 + 1);
        int randomB = (int)(Math.random() * 255 + 1);
        return new Color(randomR,randomG,randomB);
    }
    public void paint(Graphics g) {

        if(!mVector.isEmpty()){
            for (int i = 0; i < mVector.size(); i++){
                Graphics2D path = (Graphics2D) g;
                path.draw((GeneralPath) mVector.elementAt(i));
                path.setPaint(getRandomColor());
            }
        }

        System.out.println(mReceivers.size());
        if(!mReceivers.isEmpty()){
            System.out.println(mReceivers.size());
            for (int i = 0; i < mReceivers.size(); i++){
                Graphics2D receiver = (Graphics2D) g;
                receiver.drawOval(19,99,12,12);
                receiver.setPaint(RECIEVER_COLOR);
             // receiver.setStroke(new BasicStroke(1.5f));
            }
        }
    }

}
