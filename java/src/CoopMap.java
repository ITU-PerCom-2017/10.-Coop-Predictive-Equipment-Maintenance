import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Johnni on 03-11-2017.
 */

public class CoopMap {
    public static final int TIME_RESOLUTION = 1; // Resolution of the database in seconds. 5 means there is a data point every fifth second.
    public static final int PORT = 7007; // Port to start from
    public static final int FRAME_WIDTH = 1315; // Measured width of DesignLab
    public static final int FRAME_HEIGHT = 1140; // Measured height of DesignLab
    public static final String TITLE = "COOP Indoor Location Map";
    public static final Color BG_COLOR = Color.gray; // Background color of the map window

    private static PrintStream file = null;
    private static PrintStream console = null;


    // Sorts a map of receivers by rssi value in ascending order
    private static Map<String, Integer> sortMapByValue(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }


    // Creates an array of receiver CirclePoints from a map of receiver ids
    private static CirclePoint[] getCirclePointsFromReceivers(List<BeaconReceiver> receiverCoordinates, Map<String, Integer> receivers, int amount) {

        // Checks if there are data in the list and that there are enough data (not less than 3) to create CirclePoints
        if (amount < 1 || receivers.size() < amount) {
            return null;
        }

        int count = 0;
        CirclePoint[] circlePoints = new CirclePoint[amount];

        // Get the first receivers
        for (Map.Entry<String, Integer> entry : receivers.entrySet()) {

            if (count >= amount) {
                break;
            }

            String receiverId = entry.getKey();
            Integer distance = entry.getValue();

            System.out.println("count - KEY =  " + receiverId);
            System.out.println("count - VAL =  " + distance);


            double x = 0.0;
            double y = 0.0;

            System.out.println("-----------------------");

            // Uses a receiver's id to find its coordinates
            for (BeaconReceiver receiver : receiverCoordinates) {

                if (receiverId.equals(receiver.getId())) {

                    x = receiver.getX();
                    y = receiver.getY();
                    System.out.println("       distance =  " + distance);
                    System.out.println("receiver.getX() =  " + x);
                    System.out.println("receiver.getY() =  " + y);

                    break;
                }
            }

            // Creates a CirclePoint and adds it to the array
            CirclePoint cp = new CirclePoint(x, y, distance);
            circlePoints[count] = cp;

            count++;
        }

        return circlePoints;
    }

    private static CirclePoint calculateCoordinates(CirclePoint receiver1, CirclePoint receiver2, CirclePoint receiver3) {
        // Website formula https://math.stackexchange.com/questions/884807/find-x-location-using-3-known-x-y-location-using-trilateration

        // First coordinate and distance
        double x1 = receiver1.getX();
        double y1 = receiver1.getY();
        double r1 = receiver1.getR();

        // Second coordinate and distance
        double x2 = receiver2.getX();
        double y2 = receiver2.getY();
        double r2 = receiver2.getR();

        // Third coordinate and distance
        double x3 = receiver3.getX();
        double y3 = receiver3.getY();
        double r3 = receiver3.getR();

        // Intermediate calculations
        double A = (-2)*x1 + 2*x2;
        double B = (-2)*y1 + 2*y2;
        double C = (r1*r1) - (r2*r2) - (x1*x1) + (x2*x2) - (y1*y1) + (y2*y2);
        double D = (-2)*x2 + 2*x3;
        double E = (-2)*y2 + 2*y3;
        double F = (r2*r2) - (r3*r3) - (x2*x2) + (x3*x3) - (y2*y2) + (y3*y3);

        // Formula: x = (CE-FB)/(EA-BD)
        double x = (C*E - F*B) / (E*A - B*D);

        // Formula: y = (CD-AF)/(BD-AE)
        double y = (C*D - A*F) / (B*D - A*E);

        return new CirclePoint(x, y, -1);
    }


    // Draws a new point to the canvas for each beacon
    private static void drawBeacons(List<BeaconReceiver> receiverCoordinates, Map<String, Map<String, Integer>> beacons, MapCanvas canvas) {

        // Checks if the conditions are met
        if (beacons == null || beacons.size() < 1) {
            return;
        }

        // Goes through each beacon
        for (String beaconId : beacons.keySet()) {

            // Gets the receiver data associated with the beacon
            Map<String, Integer> receivers = beacons.get(beaconId);

            if (receivers != null && receivers.size() > 2) {

                // Sorting the map by value
                receivers = sortMapByValue(receivers);

                // Gets the first 3 receivers
                CirclePoint[] circlePoints = getCirclePointsFromReceivers(receiverCoordinates, receivers, 3);

                if (circlePoints != null && circlePoints.length > 2) {
                    System.setOut(console);
                    // Calculate the beacon's coordinate
                    CirclePoint bCoordinate = calculateCoordinates(circlePoints[0], circlePoints[1], circlePoints[2]);
                    System.out.println("Beacon Coordinate: X=" + bCoordinate.getX() + "-Y=" + bCoordinate.getY());
                    System.out.println("---------------------");

                    // Draws the point to canvas
                    canvas.addPoint(beaconId, (int)bCoordinate.getX(), (int)bCoordinate.getY());
                    System.setOut(file);
                    System.out.println(beaconId + "," + (int)bCoordinate.getX() + "," + (int)bCoordinate.getY());
                    System.setOut(console);
                }
            }
        }
    }


    // Starts drawing points to the canvas on a new thread. If there is no new data to draw then it waits 0.8 seconds.
    private static void startCoopMap(List<BeaconReceiver> receiverCoordinates, RssiDatabase database, MapCanvas canvas) {
        Thread t = new Thread(() -> {
            int time = 0;
            while (true) {

                // Checks if there is new data since last update
                if (!(time == database.time())) {
                    time = database.time();
                    drawBeacons(receiverCoordinates, database.getLatestBeaconData(), canvas);

                }

                // Waits 0.8 seconds before checking for new data again
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


    private static List<BeaconReceiver> readReceiversFromFile() {
        List<BeaconReceiver> receiverCoordinates = new ArrayList<>();

        String fileName = "receiver_coordinates.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] splitInput = line.split("\\s+"); // Splits the string for each 'space'

                // Checks for exactly 3 inputs
                if (splitInput.length == 3) {

                    String id = splitInput[0];
                    int x = Integer.parseInt(splitInput[1]);
                    int y = Integer.parseInt(splitInput[2]);

                    // Checks if the X and Y values match the canvas size.
                    if (x >= 0 && x < FRAME_WIDTH && y >= 0 && y < FRAME_HEIGHT) {

                        receiverCoordinates.add(new BeaconReceiver(id, x, y));
                        System.out.println("Receiver added. Total receivers: " + receiverCoordinates.size());

                    } else {
                        System.out.println("Coordinates does not match. Try again.");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiverCoordinates;
    }


    // Testing method for calculating coordinates
    private static void testCoordinates() {
        CirclePoint cp1 = new CirclePoint(1,1,6);
        CirclePoint cp2 = new CirclePoint(1,5,4);
        CirclePoint cp3 = new CirclePoint(5,1,4);

        CirclePoint cp4 = calculateCoordinates(cp1, cp2, cp3);

        System.out.println("X:" + cp4.getX() + " Y:" + cp4.getY());
    }


    // Method to get real time printed out
    private static void testTime() {
        Thread t = new Thread(() -> {
            while (true) {

                String time = LocalDateTime.now().toLocalTime().toString();

                System.out.println(time);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }



    public static void main(String[] args) {
        System.out.println("Welcome to " + TITLE);
        System.out.println("_____________________________________");

        RssiDatabase database = null;
        MapCanvas canvas = null;


        //testTime();


        // Reads the coordinates and id for each receiver from a file
        List<BeaconReceiver> receiverCoordinates = readReceiversFromFile();


        // Checks if there is at least 4 receivers. Then sets up the system.
        if (receiverCoordinates.size() > 3) {
            database = new RssiDatabase(TIME_RESOLUTION);
            canvas = new MapCanvas(TITLE, FRAME_WIDTH, FRAME_HEIGHT, BG_COLOR);

            // Draws the receivers to the canvas
            for (BeaconReceiver receiver : receiverCoordinates) {
                String rId = receiver.getId();
                int rX = (int)receiver.getX();
                int rY = (int)receiver.getY();
                canvas.addReceiver(rId, rX, rY);
            }

            new UDPServer(database);
        }

        startCoopMap(receiverCoordinates, database, canvas);


        // Store current System.out before assigning a new value
        console = System.out;
        //Create logfile and set output stream
        try {
            file = new PrintStream(new File("log/coordinates.txt"));
            System.out.println( "log/coordinates.txt PrintStream ready");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
