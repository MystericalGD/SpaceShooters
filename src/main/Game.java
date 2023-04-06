package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Timer;
import java.util.TimerTask;

import controller.AbstractController;
// import controller.MouseKeyController;
import controller.KeyController;
import entities.Player;

public class Game {
    private GamePanel gamePanel;
    public AbstractController controller;
    private int UPS;
    private int FPS;
    private Player player = new Player();
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Thread FPSThread = new Thread() {};
    Timer FPSTimer = new Timer();
    Timer UPSTimer = new Timer();
    Game(GamePanel gamePanel) {
        this(gamePanel, 60,60);
        
        
    }

    Game(GamePanel gamePanel, int UPS, int FPS) {
        this.gamePanel = gamePanel;
        new Thread(() -> FPSTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                render();
                // System.out.println("render");
            }
        }, 0, 1000/FPS)).start();;
        new Thread(() -> UPSTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                update();
                // System.out.println("update");
            }
        }, 0, 1000/UPS)).start();

        controller = new KeyController(this);
        gamePanel.addMouseMotionListener(controller);
        gamePanel.addMouseListener(controller);
        gamePanel.addKeyListener(controller);

        // FPSTimer.start();
        // UPSTimer.start();
    }

    public void update() {
        player.update();
    }

    public void render() {
        Graphics g = gamePanel.getGraphics();
        gamePanel.resetGraphics(g);
        player.render(g);
        toolkit.sync();
    }

    public Player getPlayer() {
        return player;
    }
}