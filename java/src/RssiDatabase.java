import java.util.*;

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
    private static Integer sTime;

    // Constructor
    public RssiDatabase() {
        sDatabase = new Vector<>();
        sTime = 0;
    }


    // Primary method to put beacon rssi data into the database.
    public void putBeaconRssi(String beaconId, String receiverId, Integer rssi) {

        // Reads the time from the system. This is used for keys.
        Double doubleTime = System.currentTimeMillis() * 0.0001; // Milliseconds to 10 seconds.
        Integer time = doubleTime.intValue();

        // Checks if the current time is equal to the last timestamp, then gets the last element.
        if (time.intValue() == sTime.intValue()) {

            Map<String, Map<String, Integer>> beacons = sDatabase.lastElement();

            // Checks if there are data from the beacon for that specific timestamp, then gets the map and store the rssi data.
            if (beacons.containsKey(beaconId)) {

                Map<String, Integer> receivers = beacons.get(beaconId);
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
            sTime = doubleTime.intValue();
        }
    }


    // Copies and returns the latest receiver data for all beacons. Can be null.
    public Map<String, Map<String, Integer>> getLatestBeaconData() {
        if (sDatabase.size() > 1) {
            return new HashMap<>(sDatabase.get(sDatabase.size() - 2));
        }

        return null;
    }


    // Returns the latest timestamp.
    public int time() {
        return sTime;
    }


    // Returns the number of beacons.
    public int size() {
        return sDatabase.size();
    }

}



