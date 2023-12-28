package asteroidshooter.utils;

import asteroidshooter.objects.Point;

public class MathUtils {
    public static double getDistance(Point startPoint, Point endPoint) {
        return Math.sqrt(Math.pow((endPoint.getX() - startPoint.getX()), 2) +
                Math.pow((endPoint.getY() - startPoint.getY()), 2));
    }

    public static double getAngle(Point centerPoint, Point endPoint) {
        double angle = Math.atan((endPoint.getY() - centerPoint.getY()) / (endPoint.getX() - centerPoint.getX()));
        if (angle < 0)
            angle += 2 * Math.PI;
        if (endPoint.getX() - centerPoint.getX() < 0)
            angle -= Math.PI;
        return angle;
    }
}
