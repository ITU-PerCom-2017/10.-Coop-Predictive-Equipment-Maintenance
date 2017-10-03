import java.net.InetAddress;
import java.sql.Date;
import java.sql.Timestamp;

public class Beacon {
    private static double beaconId;
    private static double distanceToReciever;
    private static InetAddress recieverAdress;

    private long timeNow;
    public Beacon (double ssid, double distToR, InetAddress reciever) {
        beaconId = ssid;
        recieverAdress = reciever;
        distanceToReciever = distToR;
        timeNow = System.currentTimeMillis();

    }
}
