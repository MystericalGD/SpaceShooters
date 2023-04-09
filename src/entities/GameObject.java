package entities;
import java.awt.Graphics;
import main.Game;
public abstract class GameObject extends Point {
    protected Game game;
    protected abstract void update();
    protected abstract void render(Graphics g);
    // public abstract void isCollided(Object o);

    GameObject() {
        this(0,0);
    }

    GameObject(double x, double y) {
        super(x,y);
    }

    public void registerGame(Game game) {
        this.game = game;
    }
    GameObject(Game game) {
        this();
        this.game=game;
    }

}

