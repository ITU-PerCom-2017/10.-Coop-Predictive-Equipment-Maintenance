/**
 * Created by Johnni on 08-11-2017.
 */

public class BeaconReceiver {
    private String mId;
    private double mX;
    private double mY;

    public BeaconReceiver(String id, double x, double y) {
        mId = id;
        mX = x;
        mY = y;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public double getX() {
        return mX;
    }

    public void setX(double mX) {
        this.mX = mX;
    }

    public double getY() {
        return mY;
    }

    public void setY(double mY) {
        this.mY = mY;
    }
}
