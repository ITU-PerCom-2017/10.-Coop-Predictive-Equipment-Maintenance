import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Johnni on 03-11-2017.
 */

public class CoopMap {
    private static final int TIME_RESOLUTION = 1; // Resolution of the database in seconds. 5 means there is a data point every fifth second.
    private static final int START_PORT = 6789; // Port to start from
    private static final int RECEIVERS = 4; // Number of receivers
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 500;
    private static final String TITLE = "COOP Indoor Location Map";
    private static final Color BG_COLOR = Color.gray; // Background color of the map window

    // Test register to get a distance in cm from a rssi value
    private static final int[] TEST_VALUES = {4,4,4,5,5,6,6,6,7,8,8,9,10,10,11,12,13,14,15,16,18,19,21,22,24,26,28,30,33,35,38,41,44,48,52,56,60,65,70,76,82,89,96,103,112,121,130,141,152,164,177,191,207,223,241,260,281,304,328,354,383,413,446,482,521,562,607,656,708,765,826,892,964,1041,1124,1214,1311,1416,1529,1652,1784,1927,2081,2247,2427,2621,2831,3057,3302,3566,3851,4160,4492,4852,5240,5659,6112,6601,7129,7699};


    private static List<BeaconReceiver> sReceiverCoordinates;
    private static RssiDatabase sDatabase;
    private static TCPServer sServer;
    private static MapCanvas sCanvas;


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
    private static CirclePoint[] getCirclePointsFromReceivers(Map<String, Integer> receivers, int amount) {

        // Checks if the conditions are met
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

            String key = entry.getKey();
            Integer value = entry.getValue();

            System.out.println("count - KEY =  " + key);
            System.out.println("count - VAL =  " + value);


            // Finds the distance that corresponds to the given rssi
            double distance = getDistance(value);
            double x = 0.0;
            double y = 0.0;

            System.out.println("-----------------------");

            // Uses a receiver's id to find its coordinates
            for (BeaconReceiver receiver : sReceiverCoordinates) {

                if (key.equals(receiver.getId())) {

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

        // First coordinate and rssi
        double x1 = receiver1.getX();
        double y1 = receiver1.getY();
        double r1 = receiver1.getR();

        // Second coordinate and rssi
        double x2 = receiver2.getX();
        double y2 = receiver2.getY();
        double r2 = receiver2.getR();

        // Third coordinate and rssi
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


    // Converts rssi to distance in centimeter
    private static int getDistance(int rssi) {

        if (rssi >= 0 && rssi < TEST_VALUES.length) {
            return TEST_VALUES[rssi];
        }

        return -1;
    }


    // Draws a new point to the canvas for each beacon
    private static void drawBeacons(Map<String, Map<String, Integer>> beacons) {

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
                CirclePoint[] circlePoints = getCirclePointsFromReceivers(receivers, 3);

                if (circlePoints != null && circlePoints.length > 2) {

                    // Calculate the beacon's coordinate
                    CirclePoint bCoordinate = calculateCoordinates(circlePoints[0], circlePoints[1], circlePoints[2]);
                    System.out.println("Beacon Coordinate: X=" + bCoordinate.getX() + "-Y=" + bCoordinate.getY());
                    System.out.println("---------------------");

                    // Draws the point to canvas
                    sCanvas.addPoint(beaconId, (int)bCoordinate.getX(), (int)bCoordinate.getY());
                }
            }
        }
    }



    // Starts drawing points to the canvas on a new thread. If there is no new data to draw then it waits 0.8 seconds.
    private static void startCoopMap() {
        Thread t = new Thread(() -> {
            int time = 0;

            while (true) {

                // Checks if there is new data since last update
                if (!(time == sDatabase.time())) {
                    time = sDatabase.time();
                    drawBeacons(sDatabase.getLatestBeaconData());
                }

                // Waits 0.8 seconds before checking for new data again
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }



    public static void main(String[] args) throws IOException {
        sReceiverCoordinates = new ArrayList<>();
        sDatabase = new RssiDatabase(TIME_RESOLUTION);
        boolean started = false;

        System.out.println("Welcome to " + TITLE);
        System.out.println("_____________________________________");
        System.out.println("Type in a receiver: >ID XX YY<. Insert minimum 3 receivers.");
        System.out.println("ID = Find this on the receiver hardware.");
        System.out.println("XX = Coordinate between 0 and " + FRAME_WIDTH);
        System.out.println("YY = Coordinate between 0 and " + FRAME_HEIGHT);


        // While loop that listens for user input
        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = reader.readLine();
            String[] splitInput = input.split("\\s+"); // Splits the string for each 'space'

            // Checks for exactly 3 inputs
            if (splitInput.length == 3) {

                String id = splitInput[0];
                int x = Integer.parseInt(splitInput[1]);
                int y = Integer.parseInt(splitInput[2]);

                // Checks if the X and Y values match the canvas size.
                if (x >= 0 && x <= FRAME_WIDTH && y >= 0 && y <= FRAME_HEIGHT) {

                    sReceiverCoordinates.add(new BeaconReceiver(id, x, y));
                    System.out.println("Receiver added. Total receivers: " + sReceiverCoordinates.size());

                    // Checks if there is at least 3 receivers and that the service is not yet started.
                    if (sReceiverCoordinates.size() > 2 && !started) {

                        sServer = new TCPServer(sDatabase, START_PORT, RECEIVERS);
                        sCanvas = new MapCanvas(TITLE, FRAME_WIDTH, FRAME_HEIGHT, BG_COLOR);

                        startCoopMap();

                        started = true;
                        System.out.println("SYSTEM STARTED");
                    }

                    // Draws the receivers to the canvas if the application is running
                    if (started && sReceiverCoordinates.size() > 0) {
                        for (BeaconReceiver receiver : sReceiverCoordinates) {
                            String rId = receiver.getId();
                            int rX = (int)receiver.getX();
                            int rY = (int)receiver.getY();
                            sCanvas.addReceiver(rId, rX, rY);
                        }
                    }

                } else {
                    System.out.println("Coordinates does not match. Try again.");
                }

            } else {
                System.out.println("Wrong input. Try again.");
                System.out.println("_____________________________________");
                System.out.println("Type in a receiver: >ID XX YY<. Insert minimum 3 receivers.");
                System.out.println("ID = Find this on the receiver hardware.");
                System.out.println("XX = Coordinate between 0 and " + FRAME_WIDTH);
                System.out.println("YY = Coordinate between 0 and " + FRAME_HEIGHT);
            }
        }
    }
}
