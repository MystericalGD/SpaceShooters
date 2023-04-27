package asteroidshooter.controller;

import asteroidshooter.main.Game;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class AbstractController implements KeyListener, MouseListener {
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
