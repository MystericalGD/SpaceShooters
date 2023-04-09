package entities;

public class Point {
    protected double x;
    protected double y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Point() {
        x = 0;
        y = 0;
    }
    public double getX() 
    {
        return x;
    }
    public double getY() 
    {
        return y;
    }
}
