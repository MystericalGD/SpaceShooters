package asteroidshooter.main;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import asteroidshooter.controller.AbstractController;
import asteroidshooter.controller.KeyController;
import asteroidshooter.main.panel.GameInfoPanel;
import asteroidshooter.main.panel.GamePanel;
import asteroidshooter.main.panel.InGameMenuPanel;
import asteroidshooter.main.panel.InfoPanel;
import asteroidshooter.objects.Asteroid;
import asteroidshooter.objects.Border;
import asteroidshooter.objects.Bullet;
import asteroidshooter.objects.Player;
import asteroidshooter.objects.Point;
import asteroidshooter.utils.MathUtils;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ConcurrentModificationException;

import javax.swing.JRadioButton;
public class Game implements ActionListener, ItemListener, ChangeListener {
    public AbstractController controller;
    public boolean ALLOW_SHOOT = true;
    
    private Player player;
    private long score;
    private long highestScore = 0;
    private int MAX_TOTAL_ASTEROIDS = 30;
    private double regenAsteroidTime = 0.2; //seconds
    private int regenAsteroidStatus; //seconds
    private boolean isPaused = false;
    private boolean isEndChecked = false;
    public static enum Status {
        PLAY,
        PAUSE,
        END
    }
    
    private Timer UpdateTimer;
    private Timer RenderTimer;
    private Timer WatchTimer;
    public final int MAX_TIME_SEC = 30;
    private int timeSec = 0;
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private InGameMenuPanel menuPanel;
    private GameInfoPanel gameInfoPanel;
    private static int UPS = 60;
    private int updateCount;
    private int FPS = 60;
    private Border border;
    public ArrayList<Bullet> BulletsList;
    public ArrayList<Point> DeadBulletsList;
    public ArrayList<Asteroid> AsteroidsList;


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
        updateCount = 0;
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
        updateTime();
    }

    public void render() {
        gamePanel.repaint();
    }
    
    private void checkEnd() {
        if (!isEndChecked) {
            if (updateCount >= MAX_TIME_SEC * UPS) {
                score = score + Math.round(player.getHP()) * 4;
                highestScore = Math.max(score,highestScore);
                menuPanel.changeMenu(Status.END);
                isEndChecked = true;
            }
            else if (player.isDead()) {
                menuPanel.changeMenu(Status.END);
                isEndChecked = true;
            }
        }
    }
    public void restart() {
        init();
        menuPanel.changeMenu(Status.PLAY);
    }

    private void updateTime() {
        if (updateCount < MAX_TIME_SEC * UPS){
            updateCount++;
            timeSec = updateCount / UPS;
        }
    }
    
    public int getTimeLeft() {
        return MAX_TIME_SEC - timeSec;
    }
    public int getUpdateLeft(){
        return MAX_TIME_SEC*UPS - updateCount;
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

    public long getScore() {
        return score;
    }
    public long getHighestScore() {
        return highestScore;
    }

    public Dimension getGamePanelSize() {
        return gamePanel.getSize();
    }
    
    // BULLETS

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

    // ASTEROID

    private void updateAsteroids() {
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
                if (getTimeLeft() > 0) score += asteroid.getPoint();
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
        else if (e.getSource() == WatchTimer && !isPaused) {
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
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == 1) 
        {
            if (e.getItem() == menuPanel.FPS30) {
                menuPanel.FPS30.setSelected(true);
                menuPanel.FPS60.setSelected(false);
                FPS = 30;
                RenderTimer.stop();
                RenderTimer = new Timer(1000/FPS,this);
                RenderTimer.start();
            }
            else if (e.getItem()== menuPanel.FPS60) {
                menuPanel.FPS30.setSelected(false);
                menuPanel.FPS60.setSelected(true);
                FPS = 60;
                RenderTimer.stop();
                RenderTimer = new Timer(1000/FPS,this);
                RenderTimer.start();
            }
            else if ((e.getSource() == menuPanel.controllerModeSelectionBox) && controller instanceof KeyController) {
                System.out.println(e.getItem().toString());
                ((KeyController)controller).switchMode(e.getItem().toString());
            }
        }
        else if (e.getStateChange() == 2) 
        {
            if (!menuPanel.FPS30.isSelected() && !menuPanel.FPS60.isSelected()) {
                ((JRadioButton)e.getItem()).setSelected(true);
            }
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == menuPanel.sensitivitySlider && controller instanceof KeyController) {
            ((KeyController)controller).setRotateSpeed(((JSlider)e.getSource()).getValue());
        }
    }
}