package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import main.Game;

public class Bullet extends GameObject {
    double theta;
    private double velocityX;
    private double velocityY;
    private double length = 7;
    private Color bulletColor = Color.BLACK;
    public static final double MAX_VELOCITY = 1000;
    public static final int MAX_BULLETS = 20;
    Bullet(double x, double y, double theta, Game game) {
        this(x,y,theta);
        this.game=game;
    }
    Bullet(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        velocityX = MAX_VELOCITY*Math.cos(theta);
        velocityY = MAX_VELOCITY*Math.sin(theta);
    }

    public void updatePos() {
        x += (velocityX / Game.getUPS());
        y += (velocityY / Game.getUPS());
    }
    public void update() {
        updatePos();
    }
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(bulletColor);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine((int)x, (int)y, (int)(x - length*Math.cos(theta)), (int)(y - length*Math.sin(theta)));
    }

    public boolean outsideBorder() {
        Border border = game.getBorder();
        return (x < border.x || x > border.x + border.w ||
                y < border.y || y > border.y + border.h);
    }
}
