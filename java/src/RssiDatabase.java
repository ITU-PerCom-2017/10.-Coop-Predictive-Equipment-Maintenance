import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Johnni on 17-10-2017.
 */

/**
 * This database uses a Vector list to keep it thread safe.
 * New elements is added to the end for each whole 'time resolution'. Could be each second or each 5 seconds.
 */
public class RssiDatabase {

    // First  map: Beacon ID map
    // Second map: Receiver ID map with RSSI values
    private static volatile Vector<Map<String, Map<String, Integer>>> sDatabase;

    // Latest timestamp in seconds
    private static AtomicInteger sTime;

    // Time resolution of the database
    private static double sTimeResolution;

    // Distance from rssi value array
    private static int[] r1b1, r1b2, r1b3, r1b4;
    private static int[] r2b1, r2b2, r2b3, r2b4;
    private static int[] r3b1, r3b2, r3b3, r3b4;
    private static int[] r4b1, r4b2, r4b3, r4b4;

    // Constructor
    public RssiDatabase(int resolution) {
        sDatabase = new Vector<>();
        sTime = new AtomicInteger(0);
        sTimeResolution = 1.0 / (resolution * 1000); // Resolution of the database in seconds. 5 means there is a data point every fifth second.

        // Generating distance values for each receiver and beacon.
        // a and b Values has been calculated from data measured in DesignLab.
        r1b1 = generateDistValues(3.39,10.7);
        r1b2 = generateDistValues(27.5, 6.48);
        r1b3 = generateDistValues(-18.5, 13.8);
        r1b4 = generateDistValues(8.34,9.45);

        r2b1 = generateDistValues(29.9, 6.48);
        r2b2 = generateDistValues(-13.7, 14.2);
        r2b3 = generateDistValues(-29, 16.6);
        r2b4 = generateDistValues(1.79, 11.8);

        r3b1 = generateDistValues(8.82, 9.56);
        r3b2 = generateDistValues(31.8, 6.12);
        r3b3 = generateDistValues(8.61, 10.2);
        r3b4 = generateDistValues(-9.89, 13.5);

        r4b1 = generateDistValues(7.84, 10.3);
        r4b2 = generateDistValues(46.3, 3.25);
        r4b3 = generateDistValues(28.7, 6.98);
        r4b4 = generateDistValues(1.79, 11.8);

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

        if (rssi >= 0 && rssi < r1b1.length) {

            switch (id) {
                // Receiver 1
                case "R1B1":
                    val = r1b1[rssi];
                    break;
                case "R1B2":
                    val = r1b2[rssi];
                    break;
                case "R1B3":
                    val = r1b3[rssi];
                    break;
                case "R1B4":
                    val = r1b4[rssi];
                    break;

                // Receiver 2
                case "R2B1":
                    val = r2b1[rssi];
                    break;
                case "R2B2":
                    val = r2b2[rssi];
                    break;
                case "R2B3":
                    val = r2b3[rssi];
                    break;
                case "R2B4":
                    val = r2b4[rssi];
                    break;

                // Receiver 3
                case "R3B1":
                    val = r3b1[rssi];
                    break;
                case "R3B2":
                    val = r3b2[rssi];
                    break;
                case "R3B3":
                    val = r3b3[rssi];
                    break;
                case "R3B4":
                    val = r3b4[rssi];
                    break;

                // Receiver 4
                case "R4B1":
                    val = r4b1[rssi];
                    break;
                case "R4B2":
                    val = r4b2[rssi];
                    break;
                case "R4B3":
                    val = r4b3[rssi];
                    break;
                case "R4B4":
                    val = r4b4[rssi];
                    break;

                default:
                    break;
            }
        }
        return val;
    }


    // Calculating the correspondent distance for each rssi value
    // Equation: x = exp( (y-a)/b )
    // where x is distance in cm and y is the rssi value.
    // a is a constant and b is the coefficient.
    private int[] generateDistValues(double a, double b) {
        int[] array = new int[150];

        for (int y = 0; y < array.length; y++) {
            double distance = Math.exp((y-a)/b);
            array[y] = (int)distance;
        }

        return array;
    }
}
