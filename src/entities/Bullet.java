package entities;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import main.Game;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
public class Bullet extends EntityObject {
    double theta;
    private double velocityX;
    private double velocityY;
    private double length = 7;
    private Color bulletColor = Color.BLACK;
    public static final double MAX_VELOCITY = 1000;
    public static final int MAX_BULLETS = 20;
    public static ArrayList<Bullet> BulletsList = new ArrayList<Bullet>() ;
    Bullet(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        velocityX = MAX_VELOCITY*Math.cos(theta);
        velocityY = MAX_VELOCITY*Math.sin(theta);
    }

    protected void update() {
        x += (velocityX / Game.getUPS());
        y += (velocityY / Game.getUPS());
    }
    protected void render(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(bulletColor);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine((int)x, (int)y, (int)(x - length*Math.cos(theta)), (int)(y - length*Math.sin(theta)));
    }

    public static void updateBullets() {
        for (Iterator<Bullet> iterator = BulletsList.iterator(); iterator.hasNext(); ) {
            Bullet bullet = iterator.next();
            bullet.update();
            if (bullet.outsideBorder()) {
                iterator.remove();
            }
        }
    }
    public static void renderBullets(Graphics g) {
        try {
            for (Iterator<Bullet> iterator = BulletsList.iterator(); iterator.hasNext(); ) {
                iterator.next().render(g);
            }
        } 
        catch (ConcurrentModificationException e) {}
            
    }
    private boolean outsideBorder() {
        Border border = Game.getBorder();
        return (x < border.x || x > border.x + border.w ||
                y < border.y || y > border.y + border.h);
    }
}
