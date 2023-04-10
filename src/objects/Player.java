package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.text.html.StyleSheet;

import main.Game;
import utils.MathUtils;

public class Player extends GameObject {
    private final double RELOAD_TIME = 0.25;
    private long MAX_RELOAD_STATUS;
    private long reload_status;
    private final double REGEN_BOOST_TIME = 1.5;
    private long MAX_REGEN_BOOST_STATUS;
    private long regen_boost_status;
    public boolean allow_shoot = true;
    public boolean isBoosted = false;
    public boolean isHit = false;
    private boolean wasHit = false;
    private double velocityX = 0;
    private double velocityY = 0;
    private double thetaTrue = 0;
    private double theta = 0;
    private double thetaUpdate = 0;
    private double HP = 100;
    private boolean triggerShoot = false;
    private boolean triggerBoost = false;
    private Direction accelerateDirection = Direction.DEFAULT;
    private final double ACCELERATION;
    private final int MAX_VELOCITY;
    private int max_velocity;
    private Color fireColor = Color.GRAY;
    private Color playerColorPrimary = Color.BLACK;
    private Color playerColorSecondary = Color.GRAY;

    public Player() {
        this(400,300);
    }
    public Player(Game game) {
        this((int)game.getGamePanelSize().getWidth()/2, 
             (int)game.getGamePanelSize().getHeight()/2);
        this.game = game;
    }
    public Player(int x, int y) {
        this(x,y, 150,1);
    }
    public Player(int x, int y, int MAX_VELOCITY, double ACCELERATION) {
        super(x,y);
        this.MAX_VELOCITY = MAX_VELOCITY;
        max_velocity = MAX_VELOCITY;
        this.ACCELERATION = ACCELERATION;
        MAX_RELOAD_STATUS = Math.round(RELOAD_TIME*Game.getUPS());
        MAX_REGEN_BOOST_STATUS = Math.round(REGEN_BOOST_TIME*Game.getUPS());
        reload_status = MAX_RELOAD_STATUS;
        regen_boost_status = MAX_REGEN_BOOST_STATUS;
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
        updateBoost();
        wasHit = isHit;
    }

    public void updatePos() {
        accelerate();
        isHitBorder(game.getBorder());
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
        double targetVelocityX = max_velocity*Math.cos(theta);
        double targetVelocityY = max_velocity*Math.sin(theta);
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
    public void setTriggerBoost(Boolean bool) {
        triggerBoost = bool;
    }
    public void updateShoot() { 
        if (triggerShoot && allow_shoot && game.BulletsList.size() < Bullet.MAX_BULLETS) {
            reload_status = 0;
            allow_shoot = false;
            game.BulletsList.add(new Bullet(x, y, theta, game));
        }
        if (reload_status < MAX_RELOAD_STATUS) {
            reload_status++;
        } else allow_shoot = true;
    }
    public void updateBoost() {
        if (triggerBoost && (regen_boost_status == MAX_REGEN_BOOST_STATUS)) {
            boostSpeed(true);
        }
        else boostSpeed(false);
        if (regen_boost_status < MAX_REGEN_BOOST_STATUS) {
            regen_boost_status++;
        }
    }

    public int isHitBorder(Border border) {
        if (x+velocityX/Game.getUPS() <= border.x + 10)  {
            velocityX *= -0.6;
            return 1;
        }
        else if (x+velocityX/Game.getUPS() >= border.x + border.w - 10) {
            velocityX *= -0.6;
            return 3;
        }
        else if (y+velocityY/Game.getUPS() <= border.y + 10) {
            velocityY *= -0.6;
            return 0;
        }
        else if (y+velocityY/Game.getUPS() >= border.y + border.h - 10) {
            velocityY *= -0.6;
            return 2;
        }
        else return -1;
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
        if (bool) max_velocity = MAX_VELOCITY*3;
        else max_velocity = MAX_VELOCITY/3;
    }

    public void deductHP(GameObject o) {
        if (o instanceof Asteroid) {
            Asteroid a = (Asteroid)o;
            if (isHit && !wasHit) {        
                HP-= Math.log10(a.getRadius() * a.getVelocity() / 10) * getVelocity()/ Game.getUPS() ;
            }
        }
    }
    public double getHP() {
        return HP;
    }
    public double getVelocity() {
        return Math.sqrt(velocityX*velocityX + velocityY*velocityY);
    }
    public void bounceAsteroid(Asteroid asteroid) {
        isHit=true;
        double thetaHit = MathUtils.getAngle(asteroid, this);
        double v_fromAsteroid = asteroid.getVelocity() * Math.cos(thetaHit - asteroid.getTheta());
        double oldVelocity = getVelocity();
        // velocityX = oldVelocity * Math.cos(thetaTrue - Math.PI - 2*thetaHit) + v_fromAsteroid * Math.cos(thetaHit);
        // velocityY = oldVelocity * Math.sin(thetaTrue - Math.PI - 2*thetaHit) + v_fromAsteroid * Math.sin(thetaHit);
        velocityX = oldVelocity * Math.cos(thetaTrue - Math.PI - 2*thetaHit) + v_fromAsteroid * Math.cos(thetaHit);
        velocityY = oldVelocity * Math.sin(thetaTrue - Math.PI - 2*thetaHit) + v_fromAsteroid * Math.sin(thetaHit);
        // x += (Math.cos(thetaHit) * (asteroid.getRadius() + 15))/Game.getUPS();
        // y += (Math.sin(thetaHit) * (asteroid.getRadius() + 15))/Game.getUPS();
        double newVelocity = getVelocity();
        double deltaVelocity = newVelocity - oldVelocity;
        regen_boost_status = 0;
        // System.out.println(Math.round(Math.toDegrees(thetaHit)));
        Border border = game.getBorder();
        switch (isHitBorder(game.getBorder())) {
            case 0:
            y = border.y+10;
            break;

            case 1:
            x = border.x+10;
            break;

            case 2:
            y = border.y+border.h-10;
            break;

            case 3:
            x = border.x+border.w-10;
            break;
            default:
            break;
        }
        // System.out.println(isHit + " " + wasHit);
    }
}
