import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johnni on 17-10-2017.
 */
public class RssiDatabase {

    // Test register to get a distance from an RSSI
    private int[] testValues = {4,4,4,5,5,6,6,6,7,8,8,9,10,10,11,12,13,14,15,16,18,19,21,22,24,26,28,30,33,35,38,41,44,48,52,56,60,65,70,76,82,89,96,103,112,121,130,141,152,164,177,191,207,223,241,260,281,304,328,354,383,413,446,482,521,562,607,656,708,765,826,892,964,1041,1124,1214,1311,1416,1529,1652,1784,1927,2081,2247,2427,2621,2831,3057,3302,3566,3851,4160,4492,4852,5240,5659,6112,6601,7129,7699};


    // First  map: Beacon ID map
    // Second map: Time map
    // Third  map: Receiver ID map with RSSI values
    private Map<String,Map<Integer,Map<String, Integer>>> mPathMap;


    // Singleton constructor
    public RssiDatabase() {
            mPathMap  = new HashMap<>();
    }


    // Primary method to put beacon rssi data into the database
    public void putBeaconRssi(String receiverId, String beaconId, Integer rssi) {

        // Reads the time from the system. This is used for keys later.
        Double doubleTime = System.currentTimeMillis() * 0.001;
        Integer time = doubleTime.intValue();

        // If the beacon already exist, get the map and store the data.
        if (mPathMap.containsKey(beaconId)) {

            Map<Integer,Map<String, Integer>> timeMap = mPathMap.get(beaconId);

            // If the beacon already has data from that timestamp, get the map and store the rssi data.
            if (timeMap.containsKey(time)) {

                Map<String, Integer> receiverMap = timeMap.get(time);

                receiverMap.put(receiverId, rssi);

                // Else create a new map and store the rssi data.
            } else {

                Map<String, Integer> receiverMap = new HashMap<>();

                receiverMap.put(receiverId, rssi);

                timeMap.put(time, receiverMap);

            }

            // If the beacon does not exist, create a map for each data type and store the data.
        } else {

            Map<Integer,Map<String, Integer>> timeMap = new HashMap<>(180); // 180 correspond to 3 minutes of data collection.

            Map<String, Integer> receiverMap = new HashMap<>();

            receiverMap.put(receiverId, rssi);

            timeMap.put(time, receiverMap);

            mPathMap.put(beaconId, timeMap);
        }
    }


    // Method to get the entire database
    public Map<String,Map<Integer,Map<String, Integer>>> getDatabase() {
        return mPathMap;
    }

    // Method to get the data from a single beacon
    public Map<Integer,Map<String, Integer>> getBeaconData(String beaconId) {
        return mPathMap.get(beaconId);
    }

    public static CirclePoint calculateCoordinates(CirclePoint receiver1, CirclePoint receiver2, CirclePoint receiver3) {
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





    public static void main(String[] args) {



        CirclePoint cp1 = new CirclePoint(1, 1, 6);
        CirclePoint cp2 = new CirclePoint(11, 1, 6);
        CirclePoint cp3 = new CirclePoint(1, 11, 6);
        CirclePoint cp4 = new CirclePoint(11, 11, 12);

        List<CirclePoint> list = new ArrayList<>();

        list.add(cp1);
        list.add(cp2);
        list.add(cp3);
        list.add(cp4);


        for (int i = 0; i < list.size(); i++) {

        }

        CirclePoint circlePoint1 = calculateCoordinates(cp1, cp2, cp3);
        System.out.println("Coordinate x =  " + circlePoint1.getX());
        System.out.println("Coordinate y =  " + circlePoint1.getY());
        System.out.println("---------------------");

        CirclePoint circlePoint2 = calculateCoordinates(cp1, cp2, cp4);
        System.out.println("Coordinate x =  " + circlePoint2.getX());
        System.out.println("Coordinate y =  " + circlePoint2.getY());
        System.out.println("---------------------");

        CirclePoint circlePoint3 = calculateCoordinates(cp1, cp3, cp4);
        System.out.println("Coordinate x =  " + circlePoint3.getX());
        System.out.println("Coordinate y =  " + circlePoint3.getY());
        System.out.println("---------------------");

        CirclePoint circlePoint4 = calculateCoordinates(cp2, cp3, cp4);
        System.out.println("Coordinate x =  " + circlePoint4.getX());
        System.out.println("Coordinate y =  " + circlePoint4.getY());
        System.out.println("---------------------");

    }


}



