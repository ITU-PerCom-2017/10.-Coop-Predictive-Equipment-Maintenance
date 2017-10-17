import java.net.InetAddress;
import java.sql.Date;
import java.sql.Timestamp;

public class Beacon {
    private static double beaconMajor;
    private static double beaconMinor;
    private static double distanceToReciever;
    private static InetAddress recieverAdress;
    private long timeNow;

    public Beacon (double major, double minor, double distToR, InetAddress reciever) {
        beaconMajor = major;
        beaconMinor = minor;
        recieverAdress = reciever;
        distanceToReciever = distToR;
        timeNow = System.currentTimeMillis();
    }
}
