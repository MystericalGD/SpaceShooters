package entities;
import java.awt.Graphics;
// import java.awt.Graphics2D;

public abstract class AbstractEntity {
    protected double posX;
    protected double posY;

    public abstract void update();
    public abstract void render(Graphics g);
    public abstract void isCollided(Object o);

}

