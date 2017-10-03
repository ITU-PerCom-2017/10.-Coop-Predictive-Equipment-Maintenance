import java.net.InetAddress;

public class Beacon {
    public static InetAddress recieverAdress;
    public static double beaconId;
    public static double distanceToReciever;

    public Beacon (double ssid) {
        beaconId = ssid;
    }

    public static double getBeaconId() {
        return beaconId;
    }

    public static void setBeaconId(double beaconId) {
        Beacon.beaconId = beaconId;
    }

    public static double getDistanceToReciever() {
        return distanceToReciever;
    }

    public static void setDistanceToReciever(double distanceToReciever) {
        Beacon.distanceToReciever = distanceToReciever;
    }
}
