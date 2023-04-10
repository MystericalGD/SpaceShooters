package main;
import utils.MathUtils;

import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
import objects.Bullet;
import objects.Border;
import objects.Player;
import objects.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import javax.swing.JComponent;
public class Game implements ActionListener, ItemListener, ChangeListener {
    public AbstractController controller;
    public boolean ALLOW_SHOOT = true;
    
    private Player player;
    private long score;
    private int MAX_TOTAL_ASTEROIDS = 30;
    private double regenAsteroidTime = 0.2; //seconds
    private int regenAsteroidStatus; //seconds
    private boolean isPaused = false;
    public static enum Status {
        PLAY,
        PAUSE,
        END
    }
    
    private Timer UpdateTimer;
    private Timer RenderTimer;
    private Timer WatchTimer;
    // Graphics g; //GamePanel graphics
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private InGameMenuPanel menuPanel;
    private GameInfoPanel gameInfoPanel;
    private static int UPS = 60;
    private int FPS = 60;
    private Border border;
    public ArrayList<Bullet> BulletsList;
    public ArrayList<Point> DeadBulletsList;
    public ArrayList<Asteroid> AsteroidsList;

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
        gamePanel.addGameListener(this);

        border = Border.fromCenter(gamePanel.getSize(), 700,500);

        controller = new KeyController("WASD");
        controller.addGameListener(this);
        gamePanel.addController(controller);
        
        init(); 
    }

    public void init() {
        player = new Player(this);
        score = 0;
        isPaused = false;
        BulletsList = new ArrayList<Bullet>() ;
        DeadBulletsList = new ArrayList<Point>();
        AsteroidsList = new ArrayList<Asteroid>();
        regenAsteroidStatus = (int)(regenAsteroidTime * UPS);
        for (int i=0; i < 15; i++) {
            AsteroidsList.add(new Asteroid(this));
        }
        UpdateTimer = new Timer(1000/UPS,this);
        RenderTimer = new Timer(1000/FPS,this);
        WatchTimer = new Timer(1000,this);
        UpdateTimer.start();
        RenderTimer.start();
        WatchTimer.start();
    }

    public void update() {
        updateAsteroids();
        player.update();
        updateBullets();
        infoPanel.addText(player.getInfo());
        gameInfoPanel.update();
        checkEnd();
    }

    public void render() {
        gamePanel.repaint();
    }
    

    public void setPaused(boolean b) {
        isPaused = b;
        if (isPaused) menuPanel.changeMenu(Status.PAUSE);
        else menuPanel.changeMenu(Status.PLAY);
    }
    public boolean isPaused() {
        return isPaused;
    }


    public Player getPlayer() {
        return player;
    }
    // @Override
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
    public void renderBullets(Graphics g) {
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
    public void renderAsteroids(Graphics g) {
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
        }
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == RenderTimer) {
            render();
        }
        else if (e.getSource() == UpdateTimer && !isPaused) {
            update();
        }
        else if (e.getActionCommand() == "Resume") {
            isPaused = false;
            menuPanel.changeMenu(Status.PLAY);
        }
        else if (e.getActionCommand() == "Pause") {
            isPaused = true;
            menuPanel.changeMenu(Status.PAUSE);
        }
        else if (e.getActionCommand() == "Restart") {
            restart();
        }
        else if (e.getActionCommand() == "WASD") {
            isPaused = true;
            menuPanel.changeMenu(Status.PAUSE);
        }
        else if (e.getActionCommand() == "Arrow") {
            restart();
        }
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getItem() == menuPanel.FPS30 && e.getStateChange() == 1) {
            menuPanel.FPS30.setSelected(true);
            menuPanel.FPS60.setSelected(false);
            FPS = 30;
            RenderTimer.stop();
            RenderTimer = new Timer(1000/FPS,this);
            RenderTimer.start();
        }
        else if (e.getItem()== menuPanel.FPS60 && e.getStateChange() == 1) {
            menuPanel.FPS30.setSelected(false);
            menuPanel.FPS60.setSelected(true);
            FPS = 60;
            RenderTimer.stop();
            RenderTimer = new Timer(1000/FPS,this);
            RenderTimer.start();
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == menuPanel.sensitivitySlider && controller instanceof KeyController) {
            ((KeyController)controller).setRotateSpeed(((JSlider)e.getSource()).getValue());
        }
    }
    public long getScore() {
        return score;
    }

    public void restart() {
        init();
        menuPanel.changeMenu(Status.PLAY);

    }

    private void checkEnd() {
        if (player.isDead()) {
            menuPanel.changeMenu(Status.END);
        }
    }

    
}