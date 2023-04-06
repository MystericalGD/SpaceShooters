package controller;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import entities.Direction;
import main.Game;
import java.util.Timer;


public class KeyController extends AbstractController {
    
    public KeyController(Game game) {
        super(game);
    }


    public boolean isFocused = false;
    

    public void mouseExited(MouseEvent e) {
        isFocused = false;
    }
    public void mouseEntered(MouseEvent e) {
        isFocused = true;
    }

    public void mouseReleased(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {
        // if (isFocused) {
        //     int mouseX = e.getX();
        //     int mouseY = e.getY();
        // }
    }
    public void keyPressed(KeyEvent e) {
        // System.out.println(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            game.getPlayer().setAccelerateDirection(Direction.FORWARD);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            game.getPlayer().setAccelerateDirection(Direction.BACKWARD);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            game.getPlayer().setThetaUpdate(-5);
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            game.getPlayer().setThetaUpdate(5);
        }

    }


    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            game.getPlayer().setAccelerateDirection(Direction.DEFAULT);
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            game.getPlayer().setThetaUpdate(0);

        }
    }
}