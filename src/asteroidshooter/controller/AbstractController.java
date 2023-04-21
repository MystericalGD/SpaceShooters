package asteroidshooter.controller;

import javax.swing.event.MouseInputListener;

import asteroidshooter.main.Game;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

public abstract class AbstractController implements KeyListener, MouseInputListener {
    protected Game game;
    public boolean isFocused = false;

    AbstractController() {
    }

    public void mouseExited(MouseEvent e) {
        isFocused = false;
    }

    public void mouseEntered(MouseEvent e) {
        isFocused = true;
    }

    public void addGame(Game game) {
        this.game = game;
    }
}
