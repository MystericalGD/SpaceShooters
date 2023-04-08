package entities;
import java.awt.Graphics2D;
import java.awt.Graphics;
import main.Game;
public abstract class EntityObject extends Point {

    protected abstract void update();
    protected abstract void render(Graphics g);
    // public abstract void isCollided(Object o);

    EntityObject() {
        super(0,0);
    }

    EntityObject(double x, double y) {
        super(x,y);
    }

}

