package main;
import entities.Point;
import utils.MathUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Toolkit;
import controller.AbstractController;
// import controller.MouseKeyController;
import controller.KeyController;
import entities.Player;
import entities.Asteroid;
import entities.Border;
import entities.Bullet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
public class Game {
    public AbstractController controller;
    public static boolean ALLOW_SHOOT = true;
    
    private static Player player;
    private static int score = 0;
    private static int MAX_TOTAL_ASTEROIDS = 30;
    private static double regenAsteroidTime = 0.2; //seconds
    private static int regenAsteroidStatus; //seconds
    // private static boolean allowRegenAsteroid = true; //seconds

    
    private static GamePanel gamePanel;
    private static InfoPanel infoPanel;
    private static int UPS = 60;
    private static int FPS = 30;
    private static Border border;
    public static ArrayList<Bullet> BulletsList = new ArrayList<Bullet>() ;
    public static ArrayList<Point> DeadBulletsList = new ArrayList<Point>() ;
    public static ArrayList<Asteroid> AsteroidsList = new ArrayList<Asteroid>() ;

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Thread FPSThread;
    Thread UPSThread;

    Game(GamePanel gamePanel, InfoPanel infoPanel) {

        Game.gamePanel = gamePanel;
        Game.infoPanel = infoPanel;
        border = Border.fromCenter(gamePanel.getSize(), 700,500);
        player = new Player();
        regenAsteroidStatus = (int)(regenAsteroidTime * UPS);
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
        updateBullets();
        updateAsteroids();
        infoPanel.addText(player.getInfo());
    }

    public void render() {
        Graphics g = gamePanel.getGraphics();
        gamePanel.resetGraphics(g);
        renderBullets(g);
        renderAsteroids(g);
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
    public static void checkCollision() {

    }
    public static Dimension getGamePanelSize() {
        return gamePanel.getSize();
    }

    private static void updateBullets() {
        for (Iterator<Bullet> iterator = BulletsList.iterator(); iterator.hasNext(); ) {
            Bullet bullet = iterator.next();
            bullet.update();
            // boolean isHit = bullet.checkHit(AsteroidsList);
            if (bullet.outsideBorder()) {
                iterator.remove();
            }
        }
    }
    private static void renderBullets(Graphics g) {
        try {
            for (Iterator<Bullet> iterator = BulletsList.iterator(); iterator.hasNext(); ) {
                iterator.next().render(g);
            }
        } 
        catch (ConcurrentModificationException e) {} 
    }

    private static void updateAsteroids() {
        // System.out.println(MAX_TOTAL_ASTEROIDS + " " + AsteroidsList.size() + " " + regenAsteroidStatus + " " + regenAsteroidTime);
        if (AsteroidsList.size() < MAX_TOTAL_ASTEROIDS && (regenAsteroidStatus == regenAsteroidTime * UPS)) {
            AsteroidsList.add(new Asteroid());
            regenAsteroidStatus = 0;
        }
        else if (regenAsteroidStatus < regenAsteroidTime * UPS) {
            regenAsteroidStatus++;
        }
        for (Iterator<Asteroid> iterator = AsteroidsList.iterator(); iterator.hasNext(); ) {
            Asteroid asteroid = iterator.next();
            asteroid.update();
            checkCollision(asteroid);
            if (asteroid.isDead()) {
                iterator.remove();
            }
            else if (asteroid.isDestroyed()) {
                score += asteroid.getPoint();
                iterator.remove();
            }
        }
        // System.out.println(AsteroidsList.size());
    }
    private static void renderAsteroids(Graphics g) {
        try {
            for (Iterator<Asteroid> iterator = AsteroidsList.iterator(); iterator.hasNext(); ) {
                iterator.next().render(g);
            }
        } 
        catch (Exception e) {
            // System.out.println("ERROR");
        } 
    }
    private static void checkCollision(Asteroid asteroid) {
        for (Iterator<Bullet> iterator = BulletsList.iterator(); iterator.hasNext(); ) {
            Bullet bullet = iterator.next();
            if (MathUtils.getDistance(bullet, asteroid) < asteroid.getRadius()) {
                iterator.remove();
                asteroid.deductHP(bullet);
            }
        }
        if (MathUtils.getDistance(player, asteroid) < asteroid.getRadius()) {
            // asteroid.deductHP(player);
            player.deductHP(asteroid);
        }

    }

}