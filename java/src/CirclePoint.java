/**
 * Created by Johnni on 03-11-2017.
 */

public class CirclePoint {
    private double mX;
    private double mY;
    private double mR;

    public CirclePoint(double x, double y, double r) {
        mX = x;
        mY = y;
        mR = r;
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

    public double getR() {
        return mR;
    }

    public void setR(double mR) {
        this.mR = mR;
    }
}
