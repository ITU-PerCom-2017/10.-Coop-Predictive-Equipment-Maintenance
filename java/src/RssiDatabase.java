import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnni on 17-10-2017.
 */
public class RssiDatabase {


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

        if (mPathMap.containsKey(beaconId)) {

            Map<Integer,Map<String, Integer>> tempTimeMap = mPathMap.get(beaconId);

            if (tempTimeMap.containsKey(time)) {

                Map<String, Integer> tempReceiverMap = tempTimeMap.get(time);

                tempReceiverMap.put(receiverId, rssi);

            } else {

                Map<String, Integer> tempReceiverMap = new HashMap<>();

                tempReceiverMap.put(receiverId, rssi);

                tempTimeMap.put(time, tempReceiverMap);

            }

        } else {

            Map<Integer,Map<String, Integer>> tempTimeMap = new HashMap<>();

            Map<String, Integer> tempReceiverMap = new HashMap<>();

            tempReceiverMap.put(receiverId, rssi);

            tempTimeMap.put(time, tempReceiverMap);

            mPathMap.put(beaconId, tempTimeMap);


        }
    }


    public Map<String,Map<Integer,Map<String, Integer>>> getDatabase() {
        return mPathMap;
    }

    public Map<Integer,Map<String, Integer>> getBeaconData(String beaconId) {
        return mPathMap.get(beaconId);
    }
}



