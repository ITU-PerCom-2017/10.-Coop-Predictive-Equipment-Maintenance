import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MapCanvas extends JPanel {
    private static final Color RECEIVER_COLOR = Color.black;
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
        mColors.add(Color.green);
        mColors.add(Color.red);
        mColors.add(Color.blue);
        mColors.add(Color.black);

        JFrame frame = new JFrame(title);
        //frame.setSize(width, height);

        //frame.setSize(new Dimension(width, height));
        frame.setSize(new Dimension(400, 400));

        frame.setBackground(bgColor);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("dl_clean.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Adds scroll panels. Does not work right now.
        BufferedImage finalImage = image;
        JScrollPane jsp = new JScrollPane(this, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalImage, 0, 0, null);
            }
        };



        //jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.getContentPane().add(jsp);
    }


    /**
     * Method for creating paths / or adding points to existing paths.
     * @param beaconId the id of a beacon
     * @param x coordinate as int
     * @param y coordinate as int
     */
    public void addPoint(String beaconId, int x, int y) {

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
           // mColors.add(getRandomColor());
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
