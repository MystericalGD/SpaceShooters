package main;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import controller.AbstractController;
// import controller.MouseKeyController;
import controller.KeyController;
import entities.Player;
import entities.Border;
import entities.Bullet;

public class Game {
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    public AbstractController controller;
    private static int UPS = 120;
    private static int FPS = 60;
    public static boolean ALLOW_SHOOT = true;
    private static Player player = new Player();
    private static Border border;
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Thread FPSThread;
    Thread UPSThread;

    Game(GamePanel gamePanel, InfoPanel infoPanel) {

        this.gamePanel = gamePanel;
        this.infoPanel = infoPanel;
        border = Border.fromCenter(gamePanel.getSize(), 700,500);

        FPSThread = new Thread(() -> (new Timer())
            .scheduleAtFixedRate(new TimerTask() {
            public void run() {
                render();
                // System.out.println("render");
            }
        }, 0, 1000/FPS));
        UPSThread = new Thread(() -> (new Timer())
            .scheduleAtFixedRate(new TimerTask() {
            public void run() {
                update();
            }
        }, 0, 1000/UPS));
        controller = new KeyController(1);
        gamePanel.addMouseMotionListener(controller);
        gamePanel.addMouseListener(controller);
        gamePanel.addKeyListener(controller);
        System.out.println("DONE");
        
        FPSThread.start();
        UPSThread.start();
    }

    public void update() {
        player.update();
        Bullet.updateBullets();
        infoPanel.addText(player.getInfo());
    }

    public void render() {
        Graphics g = gamePanel.getGraphics();
        gamePanel.resetGraphics(g);
        Bullet.renderBullets(g);
        player.render(g);
        border.render(g);
        toolkit.sync();
    }

    public static Player getPlayer() {
        return player;
    }

    public static Border getBorder() {
        return border;
    }

    public static int getUPS() {
        return UPS;
    }

    public static int getFPS() {
        return FPS;
    }
    public static void setUPS(int value) {
        UPS = value;
    }
    public static void setFPS(int value) {
        FPS = value;
    }

}