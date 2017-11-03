import java.util.HashMap;
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

        if (mPathMap != null) {
            mPathMap  = new HashMap<>();
        }
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

    public void calculateCoordinates() {
        // Website formula https://math.stackexchange.com/questions/884807/find-x-location-using-3-known-x-y-location-using-trilateration

        // First coordinate and rssi
        int x1 = 1;
        int y1 = 6;
        int r1 = 3;

        // Second coordinate and rssi
        int x2 = 1;
        int y2 = 1;
        int r2 = 4;

        // Third coordinate and rssi
        int x3 = 6;
        int y3 = 1;
        int r3 = 3;

        // Intermediate calculations
        double A = (-2)*x1 + 2*x2;
        double B = (-2)*y1 + 2*y2;
        double C = (r1^2) - (r2^2) - (x1^2) + (x2^2) - (y1^2) + (y2^2);
        double D = (-2)*x2 + 2*x3;
        double E = (-2)*y2 + 2*y3;
        double F = (r2^2) - (r3^2) - (x2^2) + (x3^2) - (y2^2) + (y3^2);

        // Formula: x = (CE-FB)/(EA-BD)
        double x = (C*E - F*B) / (E*A - B*D);

        // Formula: y = (CD-AF)/(BD-AE)
        double y = (C*D - A*F) / (B*D - A*E);



    }
}



