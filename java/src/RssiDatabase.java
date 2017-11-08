import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    // Second map: Time map
    // Third  map: Receiver ID map with RSSI values
    //private static volatile Map<Integer, Map<Integer, Map<Integer, Integer>>> sPathMap;

    // First  map: Beacon ID map
    // Second map: Receiver ID map with RSSI values
    private static volatile Vector<Map<String, Map<String, Integer>>> sDatabase;

    // Latest timestamp in seconds
    private static Integer sTime;

    // Constructor
    public RssiDatabase() {
        //sPathMap = new ConcurrentHashMap<>();
        //sDatabase = Collections.synchronizedList(new LinkedList<>());
        sDatabase = new Vector<>();
        sTime = 0;
    }



    // Primary method to put beacon rssi data into the database.
    public void putBeaconRssi(String beaconId, String receiverId, Integer rssi) {

        // Reads the time from the system. This is used for keys.
        Double doubleTime = System.currentTimeMillis() * 0.001; // Milliseconds to seconds.
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



    /*
    // Primary method to put beacon rssi data into the database
    public void putBeaconRssi(Integer beaconId, Integer receiverId, Integer rssi) {

        // Reads the time from the system. This is used for keys later.
        Double doubleTime = System.currentTimeMillis() * 0.001; // Milliseconds to seconds
        Integer time = doubleTime.intValue();
        sTime = doubleTime.intValue();

        // If the beacon already exist, get the map and store the data.
        if (sPathMap.containsKey(beaconId)) {
            Map<Integer, Map<Integer, Integer>> timeMap = sPathMap.get(beaconId);

            // If the beacon already has data from that timestamp, get the map and store the rssi data.
            if (timeMap.containsKey(time)) {
                Map<Integer, Integer> receiverMap = timeMap.get(time);
                receiverMap.put(receiverId, rssi);

                // Else create a new map and store the rssi data.
            } else {
                Map<Integer, Integer> receiverMap = new HashMap<>();
                receiverMap.put(receiverId, rssi);
                timeMap.put(time, receiverMap);
            }

            // If the beacon does not exist, create a map for each data type and store the data.
        } else {
            Map<Integer, Map<Integer, Integer>> timeMap = new HashMap<>(600); // 600 correspond to 10 minutes of data collection.
            Map<Integer, Integer> receiverMap = new HashMap<>();
            receiverMap.put(receiverId, rssi);
            timeMap.put(time, receiverMap);
            sPathMap.put(beaconId, timeMap);
        }
    }

    // Returns the number of beacons
    public int size() {
        return sPathMap.size();
    }

    // Returns the entire database
    public Map<Integer, Map<Integer, Map<Integer, Integer>>> getDatabase() {
        return sPathMap;
    }

    // Copies and returns the data from a single beacon
    public Map<Integer, Map<Integer, Integer>> getBeaconData(Integer beaconId) {
        return new HashMap<>(sPathMap.get(beaconId));
    }

    // Copies and returns the latest receiver data for a specific beacon
    public Map<Integer, Map<Integer, Integer>> getLatestBeaconData() {
        Map<Integer, Map<Integer, Integer>> data = new HashMap<>();

        return data;
    }

    */
}



