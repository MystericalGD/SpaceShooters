package entities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Player extends AbstractEntity implements Movable {

    private float velocityX = 0;
    private float velocityY = 0;
    private double theta = 0;
    private double thetaUpdate = 0;
    private Direction accelerateDirection = Direction.DEFAULT;

    
    private final double ACCELERATION;
    private final int MAX_VELOCITY;

    public Player() {
        this(5,0.5);
    }
    public Player(int MAX_VELOCITY, double ACCELERATION) {
        this.MAX_VELOCITY = MAX_VELOCITY;
        this.ACCELERATION = ACCELERATION;
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.black);
        g2d.translate(posX,posY);
        g2d.rotate(theta);
        int[] playerShapeX = setOffset(new int[] {0,20,30,20,0},10);
        int[] playerShapeY = setOffset(new int[] {0,0,10,20,20},10);

        g2d.fillPolygon(playerShapeX, playerShapeY, playerShapeX.length);
        // g2d.fillRect(-10, -10, 20, 20);

    }
    private int[] setOffset(int[] shape, int offset) {
        for (int i=0; i<shape.length;i++) {
            shape[i] -= offset;
        }
        return shape;
    }
    public void update() {
        updateTheta();
        updatePos();
    }

    public void updatePos() {
        accelerate();
        // posX += Math.cos(theta) * velocity;
        // posY += Math.sin(theta) * velocity;
        posX += velocityX;
        posY += velocityY;
        // System.out.println(posX + " " +  posY);
        // posX += Math.cos(velocity);
    }
    public void updateTheta() {
        theta += thetaUpdate;
        theta %= (2*Math.PI);
        // System.out.println(theta);
    }
    public void setThetaUpdate(int degree) {
        thetaUpdate = Math.toRadians(degree);
    }

    public void setAccelerateDirection(Direction d) {
        accelerateDirection = d;
    }

    // public void accelerate() {
    //     if (velocity < MAX_VELOCITY && accelerateDirection == Direction.FORWARD) 
    //     {
    //         velocity += ACCELERATION;
    //     }
    //     else if (velocity > -MAX_VELOCITY && accelerateDirection == Direction.BACKWARD) 
    //     {
    //         velocity -= ACCELERATION;
    //     }
    //     else if (accelerateDirection == Direction.DEFAULT) {
    //         if (velocity > 0) velocity -= 0.5*ACCELERATION;
    //         else if (velocity < 0) velocity += 0.5*ACCELERATION;
    //         else velocity = 0;
    //     }
    //     // System.out.println(velocity);
    // }
    public void accelerate() {
        if (accelerateDirection == Direction.FORWARD) 
        {
            // double velocity = Math.sqrt(velocityX*velocityX + velocityY*velocityY);
            velocityX += ACCELERATION*Math.cos(theta);
            velocityY += ACCELERATION*Math.sin(theta);
        }
        else if (accelerateDirection == Direction.BACKWARD) 
        {
            velocityX -= ACCELERATION*Math.cos(theta);
            velocityY -= ACCELERATION*Math.sin(theta);
        }
        else if (accelerateDirection == Direction.DEFAULT) {
            // if (velocity > 0) {
                // velocity -= 0.5*ACCELERATION;
                velocityX /= 1.03;
                velocityY /= 1.03;
            // }
            // else if (velocity < 0) {
            //     // velocity += 0.5*ACCELERATION;
            //     velocityX += 0.5*velocityX;
            //     velocityY += 0.5*velocityY;
            // }
            // else velocity = 0;
        }
        System.out.println(velocityX + " " + velocityY);
    }

    public void updateRotation(float theta) {
        this.theta = theta;
    }

    public double round(double number, int dec) {
        double multiplier = Math.pow(10,dec);
        System.out.println(Math.round(number*multiplier)/((double)multiplier));
        return Math.round(number*multiplier)/multiplier;
    }

    public void isCollided(Object o) {

    }

}
