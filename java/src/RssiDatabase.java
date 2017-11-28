import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Johnni on 17-10-2017.
 */

/**
 * This database uses a Vector list to keep it thread safe.
 * New elements is added to the end for each whole second.
 * Its putBeaconRssi method runs in XXX time.
 * Its getLatestBeaconData method runs in XXX time.
 */
public class RssiDatabase {

    // First  map: Beacon ID map
    // Second map: Receiver ID map with RSSI values
    private static volatile Vector<Map<String, Map<String, Integer>>> sDatabase;

    // Latest timestamp in seconds
    private static AtomicInteger sTime;
    private static double sTimeResolution;

    // Constructor
    public RssiDatabase(int resolution) {
        sDatabase = new Vector<>();
        sTime = new AtomicInteger(0);
        sTimeResolution = 1.0 / (resolution * 1000); // Resolution of the database in seconds. 5 means there is a data point every fifth second.

        System.out.println("DATABASE CREATED");
    }

    // Primary method to put beacon rssi data into the database.
    public void putBeaconRssi(String receiverId, String beaconId, Integer rssi) {

        rssi = getDistance(receiverId, beaconId, rssi);

        // Reads the time from the system. This is used for keys.
        Double doubleTime = System.currentTimeMillis() * sTimeResolution;
        Integer time = doubleTime.intValue();

        // Checks if the current time is equal to the last timestamp, then gets the last element.
        if (time.intValue() == sTime.intValue()) {

            Map<String, Map<String, Integer>> beacons = sDatabase.lastElement();

            // Checks if there are data from the beacon for that specific timestamp, then gets the map and store the rssi data.
            if (beacons.containsKey(beaconId)) {

                Map<String, Integer> receivers = beacons.get(beaconId);

                // Checks if the receiver is already added. If it is, then it average out the two rssi values.
                if (receivers.containsKey(receiverId)) {
                    Integer oldRssi = receivers.get(receiverId);
                    rssi = (int) Math.round((rssi + oldRssi) * 0.5);
                }

                // Puts the data into the database
                receivers.put(receiverId, rssi);


                // Else create a new map and store the rssi data.
            } else {
                Map<String, Integer> receivers = new HashMap<>();
                receivers.put(receiverId, rssi);
                beacons.put(beaconId, receivers);

            }

            // If there is no data for on the beacon on the specific timestamp, create a new map and store the data.
        } else {
            Map<String, Map<String, Integer>> beacons = new HashMap<>(600); // 600 correspond to 10 minutes of data collection.
            Map<String, Integer> receivers = new HashMap<>();

            receivers.put(receiverId, rssi);
            beacons.put(beaconId, receivers);
            sDatabase.add(beacons);

            // Updates the timestamp
            sTime.set(doubleTime.intValue());
        }
    }


    // Copies and returns the latest receiver data for all beacons. Can be null. Size of database must be 2 or higher.
    public Map<String, Map<String, Integer>> getLatestBeaconData() {
        if (sDatabase.size() > 1) {
            return new HashMap<>(sDatabase.get(sDatabase.size() - 2));
        }

        return null;
    }


    // Returns the latest timestamp.
    public int time() {
        return sTime.get();
    }


    // Returns the number of beacons.
    public int size() {
        return sDatabase.size();
    }

    public Vector<Map<String, Map<String, Integer>>> getDatabaseCopy() {
        return new Vector<>(sDatabase);
    }

    // Converts rssi to distance in centimeter
    private static int getDistance(String receiverId, String beaconId, int rssi) {

        String id = receiverId + beaconId;

        int val = -1;

        if (rssi >= 0 && rssi < CoopMap.R1B1_VALUES.length) {

            switch (id) {
                // Receiver 1
                case "R1B1":
                    val = CoopMap.R1B1_VALUES[rssi];
                    break;
                case "R1B2":
                    val = CoopMap.R1B2_VALUES[rssi];
                    break;
                case "R1B3":
                    val = CoopMap.R1B3_VALUES[rssi];
                    break;
                case "R1B4":
                    val = CoopMap.R1B4_VALUES[rssi];
                    break;

                // Receiver 2
                case "R2B1":
                    val = CoopMap.R2B1_VALUES[rssi];
                    break;
                case "R2B2":
                    val = CoopMap.R2B2_VALUES[rssi];
                    break;
                case "R2B3":
                    val = CoopMap.R2B3_VALUES[rssi];
                    break;
                case "R2B4":
                    val = CoopMap.R2B4_VALUES[rssi];
                    break;

                // Receiver 3
                case "R3B1":
                    val = CoopMap.R3B1_VALUES[rssi];
                    break;
                case "R3B2":
                    val = CoopMap.R3B2_VALUES[rssi];
                    break;
                case "R3B3":
                    val = CoopMap.R3B3_VALUES[rssi];
                    break;
                case "R3B4":
                    val = CoopMap.R3B4_VALUES[rssi];
                    break;

                // Receiver 4
                case "R4B1":
                    val = CoopMap.R4B1_VALUES[rssi];
                    break;
                case "R4B2":
                    val = CoopMap.R4B2_VALUES[rssi];
                    break;
                case "R4B3":
                    val = CoopMap.R4B3_VALUES[rssi];
                    break;
                case "R4B4":
                    val = CoopMap.R4B4_VALUES[rssi];
                    break;

                default:
                    break;
            }
        }
        return val;
    }
}
