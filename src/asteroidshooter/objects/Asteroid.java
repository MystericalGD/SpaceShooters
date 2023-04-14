package asteroidshooter.objects;
import java.awt.Graphics2D;
import java.util.Random;

import asteroidshooter.main.Game;
import asteroidshooter.utils.MathUtils;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.AffineTransform;
public class Asteroid extends GameObject  {
    private final int MAX_VELOCITY = 150;
    private final int MIN_VELOCITY = 50;
    private final double MAX_ANGULAR_SPEED = Math.toRadians(360);
    private final double MIN_ANGULAR_SPEED = Math.toRadians(100);
    private final String[] SIZES = new String[] {"small","big"};
    private int lifetime;
    private int current_lifetime = 0;
    private int velocity;
    private double theta;
    private double rotation;
    private double rotationSpeed;
    private double HP;
    private String size;
    private int radius;
    private int XP;

    public Asteroid(Game game) {
        super(game);
        Random random = new Random();
        size = SIZES[random.nextInt(2)];
        switch (size) {
            case "small":
            radius=13;
            HP = 5;
            XP = 10;
            break;
            case "big":
            radius=20;
            HP = 10;
            XP = 20;
        }
        rotation = random.nextDouble(Math.PI);
        rotationSpeed = random.nextDouble(MIN_ANGULAR_SPEED/Game.getUPS(),MAX_ANGULAR_SPEED/Game.getUPS());
        int startSide = random.nextInt(4);
        int endSide = random.nextInt(4);
        velocity = random.nextInt(MIN_VELOCITY, MAX_VELOCITY);
        if (endSide == startSide) {
            endSide++;
            endSide %=4;
        }
        Point startPoint = pickCoordinate(startSide);
        x = startPoint.x;
        y = startPoint.y;
        Point endPoint = pickCoordinate(endSide);
        calculateLifetime(startPoint, endPoint);
        theta = MathUtils.getAngle(startPoint, endPoint);
    }

    private Point pickCoordinate(int side) {
        Random random = new Random();
        Point p = new Point();
        switch (side) {
            case 0: // up
                p.x = random.nextInt(0,(int)game.getGamePanelSize().getWidth());
                p.y = 0;
                break;
            case 1: // left
                p.x = 0;
                p.y = random.nextInt(0,(int)game.getGamePanelSize().getHeight());
                break;
            case 2: // down
                p.x = random.nextInt(0,(int)game.getGamePanelSize().getWidth());
                p.y = (int)game.getGamePanelSize().getHeight();
                break;
            case 3: // right
                p.x = (int)game.getGamePanelSize().getWidth();
                p.y = random.nextInt(0,(int)game.getGamePanelSize().getHeight());
                break;
            default:
                break;
        }
        return p;
    }
    protected void updatePos() {
        x += velocity*Math.cos(theta)/Game.getUPS();
        y += velocity*Math.sin(theta)/Game.getUPS();
        current_lifetime++;
    }
    public void update() {
        updatePos();
        rotation += rotationSpeed;
        rotation %= 2*Math.PI;
    }
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();
        g2d.translate(x,y);
        g2d.rotate(rotation);
        int[] xShapes;
        int[] yShapes;
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(2));
        switch (radius) {
            case 13:
                xShapes = new int[] {-4,10,14,1,-12,-11};
                yShapes = new int[] {-13,-11,3,13,6,-7};
                // g2d.drawOval(-13,-13,26,26);
                g2d.drawPolygon(xShapes,yShapes,6);
                break;
                case 20:
                xShapes = new int[] {-1,13,21,9,-20,-22,-5};
                yShapes = new int[] {-24,-18,2,23,14,-3,-12};
                // g2d.drawOval(-20,-20,40,40);
                g2d.drawPolygon(xShapes,yShapes,7);
                break;
            default:
                g2d.fillOval((int)x, (int)y, 20, 20);
        }
        g2d.setTransform(old);
    }

    private void calculateLifetime(Point startPoint, Point endPoint) {
        double distance = MathUtils.getDistance(startPoint,endPoint);
        lifetime = (int)(distance * Game.getUPS() / velocity);
        // System.out.println(lifetime);
    }

    public boolean isDead() {
        return (current_lifetime == lifetime);
    }
    public boolean isDestroyed() {
        return HP <= 0;
    }
    public int getRadius() {
        return radius;
    }
    public void deductHP(GameObject o) {
        if (o instanceof Player) {
            HP--;
        }
        else if (o instanceof Bullet) {
            HP-=5;
        }
    }
    public double getVelocity() {
        return velocity;
    }
    public int getXP() {
        return XP;
    }
    public double getTheta() {
        return theta;
    }
}
