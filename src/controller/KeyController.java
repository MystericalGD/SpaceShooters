package controller;
import java.awt.event.*;

import entities.Bullet;
import entities.Player;
import main.Game;


public class KeyController extends AbstractController {
    
    private int upBT;
    private int downBT;
    private int leftBT;
    private int rightBT;
    private boolean leftBTOnce = true;
    private boolean rightBTOnce = true;
    private double rotateSpeed = 300.0;
    public KeyController(int mode) {
        if (mode == 0) {
            upBT = KeyEvent.VK_UP;
            downBT = KeyEvent.VK_DOWN;
            leftBT = KeyEvent.VK_LEFT;
            rightBT = KeyEvent.VK_RIGHT;
        }
        else if (mode == 1) {
            upBT = KeyEvent.VK_W;
            downBT = KeyEvent.VK_S;
            leftBT = KeyEvent.VK_A;
            rightBT = KeyEvent.VK_D;
        }
    }

    public void mouseReleased(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

    public void keyPressed(KeyEvent e) {
        // if (isFocused)
        {    
            if (e.getKeyCode() == upBT) {
                Game.getPlayer().setAccelerateDirection(Player.Direction.FORWARD);
            }
            if (e.getKeyCode() == downBT) {
                Game.getPlayer().setAccelerateDirection(Player.Direction.BACKWARD);
            }
            if (e.getKeyCode() == leftBT && leftBTOnce) {
                Game.getPlayer().setThetaUpdate(-rotateSpeed/Game.getUPS());
                leftBTOnce = false;
            }
            if (e.getKeyCode() == rightBT && rightBTOnce) {
                Game.getPlayer().setThetaUpdate(rotateSpeed/Game.getUPS());
                rightBTOnce = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                Game.getPlayer().setTriggerShoot(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                Game.getPlayer().boostSpeed(true);
                Game.getPlayer().setAccelerateDirection(Player.Direction.FORWARD);
            }
        }
    }
    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == upBT || e.getKeyCode() == downBT) 
        {   
            if (!Game.getPlayer().isBoosted) Game.getPlayer().setAccelerateDirection(Player.Direction.DEFAULT);
        }
        if (e.getKeyCode() == leftBT)
        {
            Game.getPlayer().setThetaUpdate(rotateSpeed/Game.getUPS());
            leftBTOnce = true;
        }
        if (e.getKeyCode() == rightBT)
        {
            Game.getPlayer().setThetaUpdate(-rotateSpeed/Game.getUPS());
            rightBTOnce = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Game.getPlayer().setTriggerShoot(false);
            Game.getPlayer().allow_shoot = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            Game.getPlayer().boostSpeed(false);
            Game.getPlayer().setAccelerateDirection(Player.Direction.DEFAULT);
        }
    }
}
