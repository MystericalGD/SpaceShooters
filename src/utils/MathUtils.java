package utils;
import entities.Point;

public class MathUtils {
    public static double getDistance(Point startPoint, Point endPoint) {
        return Math.sqrt(Math.pow((endPoint.getX() - startPoint.getX()), 2) + 
                            Math.pow((endPoint.getY() - startPoint.getY()), 2));
    }
    public static double getAngle(Point startPoint, Point endPoint) {
        return Math.atan((endPoint.getY() - startPoint.getY())/(endPoint.getX() - startPoint.getX()));
    }
}
