package entities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import main.Game;
public class Player extends GameObject implements Movable {
    private final double RELOAD_TIME = 0.25;
    private long MAX_RELOAD_STATUS;
    private long reload_status;
    public boolean allow_shoot = true;
    public boolean isBoosted = false;
    private double velocityX = 0;
    private double velocityY = 0;
    private double thetaTrue = 0;
    private double theta = 0;
    private double thetaUpdate = 0;
    private double HP = 100;
    private boolean triggerShoot = false;
    private Direction accelerateDirection = Direction.DEFAULT;
    private final double ACCELERATION;
    private int MAX_VELOCITY;
    private Color fireColor = Color.GRAY;
    private Color playerColorPrimary = Color.BLACK;
    private Color playerColorSecondary = Color.GRAY;

    public Player() {
        this(400,300);
    }
    public Player(int x, int y) {
        this(x,y, 150,1);
    }
    public Player(int x, int y, int MAX_VELOCITY, double ACCELERATION) {
        super(x,y);
        this.MAX_VELOCITY = MAX_VELOCITY;
        this.ACCELERATION = ACCELERATION;
        MAX_RELOAD_STATUS = Math.round(RELOAD_TIME*Game.getUPS());
        System.out.println(Game.getUPS());
        // System.out.println(MAX_RELOAD_STATUS + " " + Math.round(RELOAD_TIME*Game.getUPS()));
        reload_status = MAX_RELOAD_STATUS;
    }

    public enum Direction {
        FORWARD,
        BACKWARD,
        DEFAULT
    }
    
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();
        
        g2d.translate(x,y);
        g2d.rotate(theta);
        int[] playerShapeX = new int[] {-10,10,20,10,-10};
        int[] playerShapeY = new int[] {-10,-10,0,10,10};
        
        g2d.setColor(playerColorPrimary);
        g2d.fillPolygon(playerShapeX, playerShapeY, playerShapeX.length);
        g2d.setColor(playerColorSecondary);
        g2d.fillOval(2,-5,10,10);


        if (accelerateDirection == Direction.FORWARD) {
            g2d.setColor(fireColor);
            g2d.fillPolygon(
                new int[]{-10, -15, -13, -17, -13, -15, -10}, 
                new int[]{10, 10, 5, 0, -5, -10, -10}, 7
            );
        }
        g2d.setTransform(old);
    }

    public void update() {
        updateTheta();
        updatePos();
        updateShoot();
    }

    public void updatePos() {
        accelerate();
        isHitBorder(Game.getBorder());
        x += (velocityX / Game.getUPS());
        y += (velocityY / Game.getUPS());
    }

    public void updateTheta() {
        thetaTrue += thetaUpdate;
        thetaTrue %= (2*Math.PI);
        if (thetaTrue < 0) thetaTrue += (2*Math.PI);
        theta = Math.toRadians(Math.round(Math.toDegrees(thetaTrue)/5) * 5);
        // System.out.println(theta);
    }
    public void setThetaUpdate(double degree) {
        thetaUpdate += Math.toRadians(degree);
    }

    public void setAccelerateDirection(Direction d) {
        accelerateDirection = d;
    }

    public void accelerate() {
        double targetVelocityX = MAX_VELOCITY*Math.cos(theta);
        double targetVelocityY = MAX_VELOCITY*Math.sin(theta);
        if (accelerateDirection == Direction.FORWARD) 
        {
            velocityX += (targetVelocityX - velocityX) * ACCELERATION / Game.getUPS();
            velocityY += (targetVelocityY - velocityY) * ACCELERATION / Game.getUPS();
        }
        else if (accelerateDirection == Direction.BACKWARD) 
        {
            velocityX += (-targetVelocityX - velocityX) * ACCELERATION / Game.getUPS();
            velocityY += (-targetVelocityY - velocityY) * ACCELERATION / Game.getUPS();
        }
        else if (accelerateDirection == Direction.DEFAULT) {
            velocityX += (0 - velocityX) * ACCELERATION / (Game.getUPS() * 1.3);
            velocityY += (0 - velocityY) * ACCELERATION / (Game.getUPS() * 1.3);
        }
        // System.out.println(velocityX + " " + velocityY);
    }

    public void setTriggerShoot(Boolean bool) {
        triggerShoot = bool;
    }
    public void updateShoot() { 
        if (triggerShoot && allow_shoot && Game.BulletsList.size() < Bullet.MAX_BULLETS) {
            reload_status = 0;
            allow_shoot = false;
            Game.BulletsList.add(new Bullet(x, y, theta));
        }
        if (reload_status < MAX_RELOAD_STATUS) {
            reload_status++;
        } else allow_shoot = true;
    }

    public void isHitBorder(Border border) {
        if (x+velocityX/Game.getUPS() <= border.x + 10|| x+velocityX/Game.getUPS() >= border.x + border.w - 10) {
            velocityX *= -0.6;
        }
        else if (y+velocityY/Game.getUPS() <= border.y + 10|| y+velocityY/Game.getUPS() >= border.y + border.h - 10) {
            velocityY *= -0.6;
        }
        // else System.out.println("No hit");
    }

    public String getInfo() {
        String directionStr = (accelerateDirection == Direction.FORWARD) ? "Forward" : 
                              (accelerateDirection == Direction.BACKWARD) ? "Backward" : 
                              "Default";
        String info = "Direction: " + directionStr + '\n' + 
                      String.format("Theta (degrees): %d\n",Math.round(Math.toDegrees(theta))) +
                      String.format("Position (x,y): (%.2f, %.2f)\n", x, y) +
                      String.format("Velocity (x,y): (%.2f, %.2f)", velocityX, velocityY);
        return info;
    }

    public void boostSpeed(boolean bool) {
        isBoosted = bool;
        if (bool) MAX_VELOCITY *=3;
        else MAX_VELOCITY /=3;
    }

    public void deductHP(GameObject o) {
        if (o instanceof Asteroid) {
            HP--;
        }
        // else if (o instanceof Bullet) {
        //     HP-=3;
        // }
    }

}
