package main;
import utils.MathUtils;

import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import controller.AbstractController;
// import controller.MouseKeyController;
import controller.KeyController;
import main.panel.GameInfoPanel;
import main.panel.GamePanel;
import main.panel.InGameMenuPanel;
import main.panel.InfoPanel;
import objects.Asteroid;
import objects.Border;
import objects.Bullet;
import objects.Player;
import objects.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ConcurrentModificationException;

public class Game implements ActionListener, ItemListener {
    public AbstractController controller;
    public boolean ALLOW_SHOOT = true;
    
    private Player player;
    private long score = 0;
    private int MAX_TOTAL_ASTEROIDS = 30;
    private double regenAsteroidTime = 0.2; //seconds
    private int regenAsteroidStatus; //seconds
    // private static boolean allowRegenAsteroid = true; //seconds
    private boolean isPaused = false;
    public static enum Status {
        PLAY,
        PAUSE,
        END
    }
    private Timer UPSTimer;
    private Timer FPSTimer;
    Graphics g; //GamePanel graphics
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private InGameMenuPanel menuPanel;
    private GameInfoPanel gameInfoPanel;
    private static int UPS = 60;
    private int FPS = 60;
    private Border border;
    public ArrayList<Bullet> BulletsList = new ArrayList<Bullet>() ;
    public ArrayList<Point> DeadBulletsList = new ArrayList<Point>() ;
    public ArrayList<Asteroid> AsteroidsList = new ArrayList<Asteroid>() ;

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    // Thread FPSThread;
    // Thread UPSThread;

    Game(GamePanel gamePanel, InfoPanel infoPanel, InGameMenuPanel menuPanel, GameInfoPanel gameInfoPanel) {

        this.gamePanel = gamePanel;
        this.infoPanel = infoPanel;
        this.menuPanel = menuPanel;
        this.gameInfoPanel = gameInfoPanel;

        menuPanel.addGameListener(this);
        gameInfoPanel.addGameListener(this);

        g = gamePanel.getGraphics();
        border = Border.fromCenter(gamePanel.getSize(), 700,500);
        player = new Player(this);

        controller = new KeyController(1);
        controller.addGameListener(this);

        regenAsteroidStatus = (int)(regenAsteroidTime * UPS);

        gamePanel.addController(controller);
        System.out.println("DONE");
        
        UPSTimer = new Timer(1000/UPS,this);
        FPSTimer = new Timer(1000/FPS,this);
        UPSTimer.start();
        FPSTimer.start();
    }

    public void update() {
        updateAsteroids();
        player.update();
        updateBullets();
        infoPanel.addText(player.getInfo());
        gameInfoPanel.update();
    }

    public void render() {
        gamePanel.resetGraphics(g);
        renderBullets(g);
        renderAsteroids(g);
        player.render(g);
        border.render(g);
        toolkit.sync();
    }

    public Player getPlayer() {
        return player;
    }

    public Border getBorder() {
        return border;
    }

    public static int getUPS() {
        return UPS;
    }

    public int getFPS() {
        return FPS;
    }
    public static void setUPS(int value) {
        UPS = value;
    }
    public void setFPS(int value) {
        FPS = value;
    }

    public Dimension getGamePanelSize() {
        return gamePanel.getSize();
    }

    private void updateBullets() {
        for (Iterator<Bullet> iterator = BulletsList.iterator(); iterator.hasNext(); ) {
            Bullet bullet = iterator.next();
            bullet.update();
            if (bullet.outsideBorder()) {
                iterator.remove();
            }
        }
    }
    private void renderBullets(Graphics g) {
        try {
            for (Iterator<Bullet> iterator = BulletsList.iterator(); iterator.hasNext(); ) {
                iterator.next().render(g);
            }
        } 
        catch (ConcurrentModificationException e) {} 
    }

    private void updateAsteroids() {
        // System.out.println(MAX_TOTAL_ASTEROIDS + " " + AsteroidsList.size() + " " + regenAsteroidStatus + " " + regenAsteroidTime);
        if (AsteroidsList.size() < MAX_TOTAL_ASTEROIDS && (regenAsteroidStatus == regenAsteroidTime * UPS)) {
            AsteroidsList.add(new Asteroid(this));
            regenAsteroidStatus = 0;
        }
        else if (regenAsteroidStatus < regenAsteroidTime * UPS) {
            regenAsteroidStatus++;
        }
        player.isHit = false;
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
    }
    private void renderAsteroids(Graphics g) {
        try {
            for (Iterator<Asteroid> iterator = AsteroidsList.iterator(); iterator.hasNext(); ) {
                iterator.next().render(g);
            }
        } 
        catch (Exception e) {
            System.out.println("ERROR");
        }
    }
    private void checkCollision(Asteroid asteroid) {
        for (Iterator<Bullet> iterator = BulletsList.iterator(); iterator.hasNext(); ) {
            Bullet bullet = iterator.next();
            if (MathUtils.getDistance(bullet, asteroid) < asteroid.getRadius()) {
                iterator.remove();
                asteroid.deductHP(bullet);
            }
        }
        if (MathUtils.getDistance(player, asteroid) < (asteroid.getRadius() + 10)) {
            player.bounceAsteroid(asteroid);
            player.deductHP(asteroid);
        }
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == FPSTimer && !isPaused) {
            render();
        }
        else if (e.getSource() == UPSTimer && !isPaused) {
            update();
        }
        else if (e.getActionCommand() == "Resume") {
            isPaused = false;
            FPSTimer.start();
            UPSTimer.start();
            menuPanel.changeMenu(Status.PLAY);
        }
        else if (e.getActionCommand() == "Pause") {
            isPaused = true;
            FPSTimer.stop();
            UPSTimer.stop(); 
            menuPanel.changeMenu(Status.PAUSE);
        }
        else if (e.getActionCommand() == "Restart") {
            restart();
        }
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getItem() == menuPanel.FPS30 && e.getStateChange() == 1) {
            menuPanel.FPS30.setSelected(true);
            menuPanel.FPS60.setSelected(false);
            FPS = 30;
            FPSTimer.stop();
            FPSTimer = new Timer(1000/FPS,this);
            FPSTimer.start();
        }
        else if (e.getItem()== menuPanel.FPS60 && e.getStateChange() == 1) {
            menuPanel.FPS30.setSelected(false);
            menuPanel.FPS60.setSelected(true);
            FPS = 60;
            FPSTimer.stop();
            FPSTimer = new Timer(1000/FPS,this);
            FPSTimer.start();
        }
    }
    public long getScore() {
        return score;
    }

    public void restart() {}
}