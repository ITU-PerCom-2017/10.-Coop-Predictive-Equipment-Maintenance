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
    private List<Color> mPathColors;

    public MapCanvas(int width, int height) {
        mPaths = new HashMap<>();
        mVector = new Vector();
        mReceivers = new ArrayList<>();
        mWidth = width;
        mHeight = height;
        setSize(mWidth, mHeight);
        setBackground(BACKGROUND_COLOR);
        mPathColors = new ArrayList<>();
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
     */
    public void addPoint(Integer beaconId, int x, int y) {
        //g.setStroke(new BasicStroke(1.5f));
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
        GeneralPath receiver = new GeneralPath();
        receiver.moveTo(x,y);
        receiver.lineTo(x,y);
        mReceivers.add(receiver);
    }

    private Color getRandomColor(){
        int randomR = (int)(Math.random() * 255 + 1);
        int randomG = (int)(Math.random() * 255 + 1);
        int randomB = (int)(Math.random() * 255 + 1);
        return new Color(randomR,randomG,randomB);
    }
    public void paint(Graphics g) {

        //todo fix mPathColors - der er et issue med at farverne paa baggrund og path overskriver?

        if(!mVector.isEmpty()){
            for (int i = 0; i < mVector.size(); i++){
                Graphics2D path = (Graphics2D) g;
                path.draw((GeneralPath) mVector.elementAt(i));
                path.setPaint(getRandomColor());
            }
        }

        if(!mReceivers.isEmpty()){
            for (int i = 0; i < mReceivers.size(); i++){
                Graphics2D receiver = (Graphics2D) g;
                receiver.draw((GeneralPath) mReceivers.get(i));
                receiver.setPaint(RECIEVER_COLOR);
                receiver.setStroke(new BasicStroke(1.5f));
            }
        }
    }

}
