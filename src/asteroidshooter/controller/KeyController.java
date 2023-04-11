package asteroidshooter.controller;
import java.awt.event.*;

import asteroidshooter.main.Game;
import asteroidshooter.objects.Player;

public class KeyController extends AbstractController {
    private int upBT;
    private int downBT;
    private int leftBT;
    private int rightBT;
    private boolean leftBTOnce = true;
    private boolean rightBTOnce = true;
    private boolean upBTHeld = false;
    private double rotateSpeed = 360.0;

    public KeyController(String mode) {
        if (mode == "Arrow") {
            upBT = KeyEvent.VK_UP;
            downBT = KeyEvent.VK_DOWN;
            leftBT = KeyEvent.VK_LEFT;
            rightBT = KeyEvent.VK_RIGHT;
        }
        else if (mode == "WASD") {
            upBT = KeyEvent.VK_W;
            downBT = KeyEvent.VK_S;
            leftBT = KeyEvent.VK_A;
            rightBT = KeyEvent.VK_D;
        }
        
    }
    public void switchMode(String mode) {
        if (mode == "Arrow") {
            upBT = KeyEvent.VK_UP;
            downBT = KeyEvent.VK_DOWN;
            leftBT = KeyEvent.VK_LEFT;
            rightBT = KeyEvent.VK_RIGHT;
        }
        else if (mode == "WASD") {
            upBT = KeyEvent.VK_W;
            downBT = KeyEvent.VK_S;
            leftBT = KeyEvent.VK_A;
            rightBT = KeyEvent.VK_D;
        }
        game.getPlayer().setThetaUpdateZero();
        game.getPlayer().setThetaUpdateZero();
        game.getPlayer().setAccelerateDirection(Player.Direction.DEFAULT);
        leftBTOnce = true;
        rightBTOnce = true;
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
                upBTHeld = true;
                game.getPlayer().setAccelerateDirection(Player.Direction.FORWARD);
            }
            if (e.getKeyCode() == downBT && !game.getPlayer().isBoosted) {
                game.getPlayer().setAccelerateDirection(Player.Direction.BACKWARD);
            }
            if (e.getKeyCode() == leftBT && leftBTOnce) {
                game.getPlayer().setThetaUpdate(-rotateSpeed/Game.getUPS());
                leftBTOnce = false;
            }
            if (e.getKeyCode() == rightBT && rightBTOnce) {
                game.getPlayer().setThetaUpdate(rotateSpeed/Game.getUPS());
                rightBTOnce = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                game.getPlayer().setTriggerShoot(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                game.getPlayer().setTriggerBoost(true);
                game.getPlayer().setAccelerateDirection(Player.Direction.FORWARD);
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                game.setPaused(!game.isPaused());
            }
        }
    }
    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
        
        if (e.getKeyCode() == upBT) {
            upBTHeld = false;
            if (!game.getPlayer().isBoosted) game.getPlayer().setAccelerateDirection(Player.Direction.DEFAULT);
        }
        if  (e.getKeyCode() == downBT) {   
            if (!game.getPlayer().isBoosted) game.getPlayer().setAccelerateDirection(Player.Direction.DEFAULT);
        }
        if (e.getKeyCode() == leftBT)
        {
            game.getPlayer().setThetaUpdate(rotateSpeed/Game.getUPS());
            leftBTOnce = true;
        }
        if (e.getKeyCode() == rightBT)
        {
            game.getPlayer().setThetaUpdate(-rotateSpeed/Game.getUPS());
            rightBTOnce = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            game.getPlayer().setTriggerShoot(false);
            game.getPlayer().allow_shoot = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            game.getPlayer().setTriggerBoost(false);
            if (!upBTHeld) game.getPlayer().setAccelerateDirection(Player.Direction.DEFAULT);
        }
    }
    public void setRotateSpeed(int value) {
        rotateSpeed = 180 + 60*value;
    }
}