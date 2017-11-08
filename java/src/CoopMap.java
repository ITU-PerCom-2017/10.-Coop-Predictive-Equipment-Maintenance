import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Johnni on 03-11-2017.
 */

public class CoopMap {
    private static final int START_PORT = 6789; // Port to start from
    private static final int RECEIVERS = 4; // Number of receivers
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 600;
    private static final String TITLE = "COOP Indoor Location Map";
    private static final Color BG_COLOR = Color.gray;

    // Test register to get a distance from an RSSI
    private static final int[] TEST_VALUES = {4,4,4,5,5,6,6,6,7,8,8,9,10,10,11,12,13,14,15,16,18,19,21,22,24,26,28,30,33,35,38,41,44,48,52,56,60,65,70,76,82,89,96,103,112,121,130,141,152,164,177,191,207,223,241,260,281,304,328,354,383,413,446,482,521,562,607,656,708,765,826,892,964,1041,1124,1214,1311,1416,1529,1652,1784,1927,2081,2247,2427,2621,2831,3057,3302,3566,3851,4160,4492,4852,5240,5659,6112,6601,7129,7699};


    private static List<BeaconReceiver> mReceiverCoordinates;
    private static int mTempDbSize = 0;


    private static void testCreateReceivers(int amount) {

        for (int i = 0; i < amount; i++) {

            int x = (int) (Math.random() * FRAME_WIDTH + 1);
            int y = (int) (Math.random() * FRAME_HEIGHT + 1);

            mReceiverCoordinates.add(new BeaconReceiver(i, x, y));

        }

    }



    private static void testNewDatabase(RssiDatabase database, MapCanvas canvas) {

        for (int i = 0; i < 60; i++) {

            Map<Integer, Map<Integer, Integer>> beacons = database.getLatestBeaconData();


            if (beacons != null && beacons.size() > 0) {

                for (Integer beaconId : beacons.keySet()) {
                    Map<Integer, Integer> receivers = beacons.get(beaconId);

                    if (receivers.size() > 2) {




                        // Sorting the map by value
                        receivers = receivers.entrySet()
                                .stream()
                                .sorted(Map.Entry.comparingByValue(/*Collections.reverseOrder()*/ ))
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (e1, e2) -> e1,
                                        LinkedHashMap::new
                                ));





                        // Get the first three receivers
                        Integer[][] idAndRssi = new Integer[3][2];

                        int count = 0;
                        for(Map.Entry<Integer, Integer> entry : receivers.entrySet()) {
                            Integer key = entry.getKey();
                            Integer value = entry.getValue();

                            if (count < 3) {
                                idAndRssi[count] = new Integer[]{key,value};
                            }
                            count++;
                        }


                        CirclePoint[] circlePoints = new CirclePoint[3];

                        for (int j = 0; j < 3; j++) {

                            int receiverId = idAndRssi[j][0];
                            int rssi = idAndRssi[j][1];
                            int distance = rssiToDistance(rssi);
                            double x = 0;
                            double y = 0;

                            for (BeaconReceiver receiver : mReceiverCoordinates) {
                                if (receiver.getId().intValue() == receiverId) {
                                    x = receiver.getX();
                                    y = receiver.getY();
                                }
                            }

                            CirclePoint cp = new CirclePoint(x, y, distance);
                            circlePoints[j] = cp;

                        }

                        // Beacon coordinate
                        CirclePoint bCoordinate = calculateCoordinates(circlePoints[0], circlePoints[1], circlePoints[2]);
                        System.out.println("Coordinate x =  " + bCoordinate.getX());
                        System.out.println("Coordinate y =  " + bCoordinate.getY());
                        System.out.println("---------------------");


                        // Draws the point to canvas
                        canvas.addPoint(beaconId, (int)bCoordinate.getX(), (int)bCoordinate.getY());

                    }
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
    private static int rssiToDistance(int rssi) {

        if (rssi >= 0 && rssi < TEST_VALUES.length) {
            return TEST_VALUES[rssi];
        }

        return -1;
    }



    public static void main(String[] args) {

        mReceiverCoordinates = new ArrayList<>();

        RssiDatabase database = new RssiDatabase();
        //TCPServer server = new TCPServer(database, START_PORT, RECEIVERS);
        MapCanvas canvas = new MapCanvas(TITLE, FRAME_WIDTH, FRAME_HEIGHT, BG_COLOR);



        testCreateReceivers(RECEIVERS);

        testNewDatabase(database, canvas);





        /*
        canvas.addPoint(1, 10, 10);



        //CirclePoint coordinate = database.calculateCoordinates(mReceiverCoordinates.get(0), mReceiverCoordinates.get(1), mReceiverCoordinates.get(2));

        int dbSize = database.getDatabase().size();
        if(dbSize >= mTempDbSize){
            mTempDbSize =dbSize;
            System.out.println(mTempDbSize);
            //canvas.addPoint(1, (int)coordinate.getX(), (int)coordinate.getY());
            dbSize = database.getDatabase().size();
        }

        */



    }
}
