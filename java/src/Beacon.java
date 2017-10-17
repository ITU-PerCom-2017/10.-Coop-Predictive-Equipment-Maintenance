import java.net.InetAddress;

public class Beacon {
    private double mMajor;
    private double mMinor;
    private double mDistance;
    private InetAddress mAddress;
    private long mTime;

    public Beacon (double major, double minor, double distToR, InetAddress receiver) {
        mMajor = major;
        mMinor = minor;
        mAddress = receiver;
        mDistance = distToR;
        mTime = System.currentTimeMillis();
    }
}
