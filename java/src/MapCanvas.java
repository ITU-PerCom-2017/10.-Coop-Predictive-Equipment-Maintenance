import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class MapCanvas extends JPanel {
    private static final Color RECEIVER_COLOR = Color.white;
    private static final int RECEIVER_SIZE = 40;

    private Map<String, GeneralPath> mPaths;
    private List<GeneralPath> mReceivers;
    private Vector<GeneralPath> mVector;
    private List<Color> mColors;

    public MapCanvas(String title, int width, int height, Color bgColor) {

        mPaths = new HashMap<>();
        mVector = new Vector<>();
        mReceivers = new ArrayList<>();
        mColors = new ArrayList<>();

        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setBackground(bgColor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.setVisible(true);
    }

    /**
     * Method for creating paths / or adding points to existing paths.
     * @param beaconId the id of a beacon
     * @param x coordinate as int
     * @param y coordinate as int
     */
    public void addPoint(String beaconId, int x, int y) {

        // Test factor DELETE AFTER USE
        double xx = x * 0.1;
        double yy = y * 0.1;
        x = (int)xx;
        y = (int)yy;
        // Test factor end

        if(mPaths.containsKey(beaconId)) {
            // If the path already exist, then append a new path to the old one
            GeneralPath newPath = new GeneralPath();
            newPath.moveTo(x, y);
            newPath.lineTo(x, y);
            mPaths.get(beaconId).append(newPath, true);

        } else {
            // If the path does not exist, then create a new path
            GeneralPath newPath = new GeneralPath();
            newPath.moveTo(x, y);
            newPath.lineTo(x, y);
            mVector.addElement(newPath);
            mColors.add(getRandomColor());
            mPaths.put(beaconId, newPath);
        }
        repaint();
    }


    // Add a receiver on a coordinate
    public void addReceiver(String id, int x, int y) {
        GeneralPath receiver = new GeneralPath();
        receiver.moveTo(x, y);
        receiver.lineTo(x, y);
        mReceivers.add(receiver);
    }


    private Color getRandomColor() {
        int r = (int) (Math.random() * 255 + 1);
        int g = (int) (Math.random() * 255 + 1);
        int b = (int) (Math.random() * 255 + 1);
        return new Color(r, g, b);
    }


    public void paint(Graphics g) {
        if (!mVector.isEmpty()) {
            for (int i = 0; i < mVector.size(); i++){
                Graphics2D path = (Graphics2D) g;
                path.setPaint(mColors.get(i));
                path.setStroke(new BasicStroke(3.0f));
                path.draw(mVector.elementAt(i));
            }
        }

        if (!mReceivers.isEmpty()) {
            for (GeneralPath receiver : mReceivers) {
                Graphics2D graphics = (Graphics2D) g;
                graphics.setPaint(RECEIVER_COLOR);

                int x = (int) receiver.getCurrentPoint().getX();
                int y = (int) receiver.getCurrentPoint().getY();
                graphics.drawOval(x-RECEIVER_SIZE/2, y-RECEIVER_SIZE/2, RECEIVER_SIZE, RECEIVER_SIZE);
            }
        }
    }
}
