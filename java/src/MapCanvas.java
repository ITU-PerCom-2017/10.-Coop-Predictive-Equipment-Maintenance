import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class MapCanvas extends Canvas{
    private static final Color BACKGROUND_COLOR = Color.gray;
    private static final Color RECEIVER_COLOR = Color.white;
    private static final int RECEIVER_SIZE = 40;
    public static  Graphics2D mPath;

    //TODO Burde det vaere et concurrentHashMap? er der ikke flere thread som tilgaar den samtidigt?
    private Map<Integer, GeneralPath> mPaths;
    private static  List<GeneralPath> mReceivers;
    private Vector<GeneralPath> mVector;
    private List<Color> mColors;
    public MapCanvas(int width, int height) {
        mPaths = new HashMap<>();
        mVector = new Vector<>();
        mReceivers = new ArrayList<>();
        mColors = new ArrayList<>();
        setSize(width, height);
        setBackground(BACKGROUND_COLOR);
    }

    /**
     * Method for creating paths / or adding points to existing paths.
     * @param beaconId the id of a beacon
     * @param x choordinate as int
     * @param y choordinate as int
     */

    public void addPoint(Integer beaconId, int x, int y,Graphics g) {
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
            getRandomColor();
            mPaths.put(beaconId,newPath);

        }
        update(g);

    }

    /**
     * Add single receiver on coordinate
     * @param x
     * @param y
     */
    public static void addReceiver(int x, int y) {
        GeneralPath receiver = new GeneralPath();
        receiver.moveTo(x,y);
        receiver.lineTo(x,y);
        mReceivers.add(receiver);
    }

    private void getRandomColor() {
        int randomR = (int)(Math.random() * 255 + 1);
        int randomG = (int)(Math.random() * 255 + 1);
        int randomB = (int)(Math.random() * 255 + 1);
        mColors.add(new Color(randomR,randomG,randomB));
    }


    public void paint(Graphics g) {
        if (!mVector.isEmpty()) {
            for (int i = 0; i < mVector.size(); i++){
               mPath = (Graphics2D) g;
                mPath.setPaint(mColors.get(i));
                mPath.draw(mVector.elementAt(i));
            }
        }

        if (!mReceivers.isEmpty()) {
            for (GeneralPath mReceiver : mReceivers) {
                Graphics2D receiver = (Graphics2D) g;
                receiver.setPaint(RECEIVER_COLOR);

                int x = (int) mReceiver.getCurrentPoint().getX();
                int y = (int) mReceiver.getCurrentPoint().getY();
                receiver.drawOval(x, y, RECEIVER_SIZE, RECEIVER_SIZE);
            }
        }
    }
}
