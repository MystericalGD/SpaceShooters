package entities;
import java.awt.Graphics;
public abstract class GameObject extends Point {

    protected abstract void update();
    protected abstract void render(Graphics g);
    // public abstract void isCollided(Object o);

    GameObject() {
        this(0,0);
    }

    GameObject(double x, double y) {
        super(x,y);
    }

}

