package objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;

public class Border {

    public int x;
    public int y;
    public int w;
    public int h;

    private Color strokeCol = Color.black;

    public Border(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        // System.out.println(x + " " + y + " " + w + " " + h);
    }
    public Border(Point upperLeft, int w, int h) {
        this(upperLeft.x, upperLeft.y, w, h);
    }

    public static Border fromCenter(Point center, int w, int h) {
        return new Border(center.x-w/2, center.y-h/2, w,h);
    }
    public static Border fromCenter(Dimension d, int w, int h) {
        return new Border((int)((d.getWidth()-w)/2), (int)((d.getHeight()-h)/2), w,h);
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 2*x+w, y);
        g2d.fillRect(0, y+h, 2*x+w, y);
        g2d.fillRect(0, y, x, h);
        g2d.fillRect(x+w, y, x, h);
        g2d.setColor(strokeCol);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(x, y, w, h);
    }
    // public int getUpperY() {
    //     return ()
    // }
    // public int getLowerY() {

    // }
    // public int getLeftX() {

    // }
    // public int getRightX() {

    // }
}
