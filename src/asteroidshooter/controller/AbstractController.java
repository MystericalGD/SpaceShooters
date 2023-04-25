package asteroidshooter.controller;

import javax.swing.event.MouseInputListener;

import asteroidshooter.main.Game;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

public abstract class AbstractController implements KeyListener, MouseInputListener {
    protected Game game;
    public boolean isFocused = false;
    public boolean mouseInside = true;

    AbstractController() {
    }

    public void mouseExited(MouseEvent e) {
        mouseInside = false;
    }

    public void mouseEntered(MouseEvent e) {
        mouseInside = true;
    }
    public void mousePressed(MouseEvent e) {
        if (mouseInside) isFocused = true;
        else isFocused = false;
    }
    public void addGame(Game game) {
        this.game = game;
    }
}
