package objects;
import java.awt.Graphics;
import main.Game;
public abstract class GameObject extends Point {
    protected Game game;
    protected double theta = 0;
    protected abstract void update();
    public abstract void updatePos();
    protected abstract void render(Graphics g);
    // public abstract void isCollided(Object o);

    GameObject() {
        super(0,0);
    }

    GameObject(double x, double y) {
        super(x,y);
    }
    GameObject(double x, double y, double theta) {
        super(x,y);
        this.theta = theta;
    }

    public void registerGame(Game game) {
        this.game = game;
    }

    GameObject(Game game) {
        this();
        this.game=game;
    }

}

