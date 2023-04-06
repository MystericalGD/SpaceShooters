package controller;
import javax.swing.event.MouseInputListener;
import java.awt.event.KeyListener;
import main.Game;

public abstract class AbstractController implements KeyListener, MouseInputListener {
    Game game;
    AbstractController(Game game) {
        this.game = game;
    }
    // abstract void rotate();
    // void 
}
