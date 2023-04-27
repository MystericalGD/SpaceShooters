package asteroidshooter.controller;

import java.awt.event.*;

import asteroidshooter.main.Game;

public class KeyController extends AbstractController {
    private int upBT;
    private int downBT;
    private int leftBT;
    private int rightBT;
    private boolean leftBTHeld = false;
    private boolean rightBTHeld = false;
    private boolean upBTHeld = false;
    private double rotateSpeed = 360.0;

    public KeyController(String mode) {
        super();
        if (mode == "Arrow") {
            upBT = KeyEvent.VK_UP;
            downBT = KeyEvent.VK_DOWN;
            leftBT = KeyEvent.VK_LEFT;
            rightBT = KeyEvent.VK_RIGHT;
        } else if (mode == "WASD") {
            upBT = KeyEvent.VK_W;
            downBT = KeyEvent.VK_S;
            leftBT = KeyEvent.VK_A;
            rightBT = KeyEvent.VK_D;
        }
    }

    public void setMode(String mode) {
        if (mode == "Arrow") {
            upBT = KeyEvent.VK_UP;
            downBT = KeyEvent.VK_DOWN;
            leftBT = KeyEvent.VK_LEFT;
            rightBT = KeyEvent.VK_RIGHT;
        } else if (mode == "WASD") {
            upBT = KeyEvent.VK_W;
            downBT = KeyEvent.VK_S;
            leftBT = KeyEvent.VK_A;
            rightBT = KeyEvent.VK_D;
        }
        game.getPlayer().setAccelerateDirection('0');
        leftBTHeld = false;
        rightBTHeld = false;
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (isFocused)
        {
            if (e.getKeyCode() == upBT) {
                upBTHeld = true;
                game.getPlayer().setAccelerateDirection('F');
            }
            if (e.getKeyCode() == downBT && !game.getPlayer().isBoosted) {
                game.getPlayer().setAccelerateDirection('B');
            }
            if (e.getKeyCode() == leftBT) {
                leftBTHeld = true;
                if (rightBTHeld == true) game.getPlayer().setThetaUpdate(0);
                else game.getPlayer().setThetaUpdate(-Math.toDegrees(Math.toRadians(rotateSpeed)) / Game.getUPS());
            }
            if (e.getKeyCode() == rightBT) {
                rightBTHeld = true;
                if (leftBTHeld == true) game.getPlayer().setThetaUpdate(0);
                else game.getPlayer().setThetaUpdate(Math.toDegrees(Math.toRadians(rotateSpeed)) / Game.getUPS());
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                game.getPlayer().setTriggerShoot(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                game.getPlayer().setTriggerBoost(true);
                game.getPlayer().setAccelerateDirection('F');
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            game.setPaused(!game.getPaused());
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == upBT) {
            upBTHeld = false;
            if (!game.getPlayer().isBoosted)
                game.getPlayer().setAccelerateDirection('0');
        }
        if (e.getKeyCode() == downBT) {
            if (!game.getPlayer().isBoosted)
                game.getPlayer().setAccelerateDirection('0');
        }
        if (e.getKeyCode() == leftBT) {
            leftBTHeld = false;
            if (rightBTHeld == false) game.getPlayer().setThetaUpdate(0);
            else game.getPlayer().setThetaUpdate(Math.toDegrees(Math.toRadians(rotateSpeed)) / Game.getUPS());
        }
        if (e.getKeyCode() == rightBT) {
            rightBTHeld = false;
            if (leftBTHeld == false) game.getPlayer().setThetaUpdate(0);
            else game.getPlayer().setThetaUpdate(-Math.toDegrees(Math.toRadians(rotateSpeed)) / Game.getUPS());
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            game.getPlayer().setTriggerShoot(false);
            game.getPlayer().isReloadDone = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            game.getPlayer().setTriggerBoost(false);
            if (!upBTHeld)
                game.getPlayer().setAccelerateDirection('0');
        }
    }

    public void setRotateSpeed(int value) {
        rotateSpeed = 300 + 60 * value;
    }
}