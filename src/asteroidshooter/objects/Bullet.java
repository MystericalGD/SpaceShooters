package asteroidshooter.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import asteroidshooter.main.Game;

public class Bullet extends GameObject {
    private static final int MAX_VELOCITY = 1500;
    private double velocityX;
    private double velocityY;
    private double length = 7;
    private Color bulletColor = Color.BLACK;

    Bullet(double x, double y, double theta, Game game) {
        this(x, y, theta);
        this.game = game;
    }

    Bullet(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        velocityX = MAX_VELOCITY * Math.cos(theta);
        velocityY = MAX_VELOCITY * Math.sin(theta);
    }

    protected void updatePos() {
        x += (velocityX / Game.getUPS());
        y += (velocityY / Game.getUPS());
    }

    public void update() {
        updatePos();
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bulletColor);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine((int) x, (int) y, (int) (x - length * Math.cos(theta)), (int) (y - length * Math.sin(theta)));
    }

    public boolean outsideBorder() {
        Border border = game.getBorder();
        return (x < border.getX() || x > border.getX() + border.getWidth() ||
                y < border.getY() || y > border.getY() + border.getHeight());
    }
}
