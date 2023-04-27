package asteroidshooter.main;

import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import asteroidshooter.controller.AbstractController;
import asteroidshooter.controller.KeyController;
import asteroidshooter.main.panel.GameInfoPanel;
import asteroidshooter.main.panel.DisplayPanel;
import asteroidshooter.main.panel.GameMenuPanel;
import asteroidshooter.main.panel.StatusPanel;
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
    public final int MAX_TIME_SEC = 60;
    public static String status = "play";
    public ArrayList<Bullet> BulletsList;
    public ArrayList<Point> DeadBulletsList;
    public ArrayList<Asteroid> AsteroidsList;
    private AbstractController controller;
    private Player player;
    private long score;
    private long highestScore = 0;
    private final int MAX_TOTAL_ASTEROIDS = 30;
    private final double REGEN_ASTEROID_TIME = 0.2; // seconds
    private int regenAsteroidStatus; // seconds
    private boolean isPaused;
    private boolean isEndChecked;
    private Timer updateTimer;
    private Timer renderTimer;
    private int timeSec = 0;
    private DisplayPanel displayPanel;
    private StatusPanel statusPanel;
    private GameMenuPanel menuPanel;
    private GameInfoPanel gameInfoPanel;
    private static int UPS = 60;
    private int updateCount;
    private int FPS = 60;
    private Border border;

    Game(DisplayPanel displayPanel, StatusPanel statusPanel, GameMenuPanel menuPanel, GameInfoPanel gameInfoPanel) {

        this.displayPanel = displayPanel;
        this.statusPanel = statusPanel;
        this.menuPanel = menuPanel;
        this.gameInfoPanel = gameInfoPanel;

        border = Border.fromCenter(displayPanel.getSize(), 700, 500);
        controller = new KeyController("WASD");

        controller.addGame(this);
        menuPanel.addGame(this);
        gameInfoPanel.addGame(this);
        displayPanel.addGame(this);
        init();
    }

    public void init() {
        player = new Player(this);
        score = 0;
        updateCount = 0;
        isPaused = false;
        isEndChecked = false;

        BulletsList = new ArrayList<Bullet>();
        DeadBulletsList = new ArrayList<Point>();
        AsteroidsList = new ArrayList<Asteroid>();
        regenAsteroidStatus = (int) (REGEN_ASTEROID_TIME * UPS);
        for (int i = 0; i < 8; i++) {
            AsteroidsList.add(new Asteroid(this));
        }
        updateTimer = new Timer(1000 / UPS, this);
        renderTimer = new Timer(1000 / FPS, this);
        updateTimer.start();
        renderTimer.start();
    }

    public void update() {
        updateAsteroids();
        player.update();
        updateBullets();
        statusPanel.addText(player.getInfo());
        gameInfoPanel.update();
        checkEnd();
        updateTime();
    }

    public void render(Graphics g) {
        renderBullets(g);
        renderAsteroids(g);
        player.render(g);
        border.render(g);
    }

    private void checkEnd() {
        if (!isEndChecked) {
            if (updateCount >= MAX_TIME_SEC * UPS) {
                score = score + Math.round(player.getHP()) * 4;
                highestScore = Math.max(score, highestScore);
                menuPanel.changeMenu("end");
                isEndChecked = true;
            } else if (player.getIsDead()) {
                menuPanel.changeMenu("end");
                isEndChecked = true;
            }
        }
    }

    public void restart() {
        init();
        menuPanel.changeMenu("play");
    }

    private void updateTime() {
        if (updateCount < MAX_TIME_SEC * UPS) {
            updateCount++;
            timeSec = updateCount / UPS;
        }
    }

    public int getTimeLeft() {
        return MAX_TIME_SEC - timeSec;
    }

    public int getUpdateLeft() {
        return MAX_TIME_SEC * UPS - updateCount;
    }

    public void setPaused(boolean b) {
        isPaused = b;
        if (isPaused)
            menuPanel.changeMenu("pause");
        else
            menuPanel.changeMenu("play");
    }

    public boolean getPaused() {
        return isPaused;
    }

    public AbstractController getController() {
        return controller;
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

    public long getScore() {
        return score;
    }

    public long getHighestScore() {
        return highestScore;
    }

    public Dimension getGamePanelSize() {
        return displayPanel.getSize();
    }

    // BULLETS

    private void updateBullets() {
        for (Iterator<Bullet> iterator = BulletsList.iterator(); iterator.hasNext();) {
            Bullet bullet = iterator.next();
            bullet.update();
            if (bullet.outsideBorder()) {
                iterator.remove();
            }
        }
    }

    public void renderBullets(Graphics g) {
        try {
            for (Iterator<Bullet> iterator = BulletsList.iterator(); iterator.hasNext();) {
                iterator.next().render(g);
            }
        } catch (Exception e) {
            System.out.println("Error rendering");
        }
    }

    // ASTEROID

    private void updateAsteroids() {
        if (AsteroidsList.size() < MAX_TOTAL_ASTEROIDS && (regenAsteroidStatus == REGEN_ASTEROID_TIME * UPS)) {
            AsteroidsList.add(new Asteroid(this));
            regenAsteroidStatus = 0;
        } else if (regenAsteroidStatus < REGEN_ASTEROID_TIME * UPS) {
            regenAsteroidStatus++;
        }
        player.isHit = false;
        for (Iterator<Asteroid> iterator = AsteroidsList.iterator(); iterator.hasNext();) {
            Asteroid asteroid = iterator.next();
            asteroid.update();
            checkCollision(asteroid);
            if (asteroid.isDead()) {
                iterator.remove();
            } else if (asteroid.isDestroyed()) {
                if (getTimeLeft() > 0)
                    score += asteroid.getXP();
                iterator.remove();
            }
        }
    }

    public void renderAsteroids(Graphics g) {
        try {
            for (Iterator<Asteroid> iterator = AsteroidsList.iterator(); iterator.hasNext();) {
                iterator.next().render(g);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    private void checkCollision(Asteroid asteroid) {
        for (Iterator<Bullet> iterator = BulletsList.iterator(); iterator.hasNext();) {
            Bullet bullet = iterator.next();
            Point bulletPoint = bullet; // implicit casting
            Point asteroidPoint = asteroid;
            if (MathUtils.getDistance(bulletPoint, asteroidPoint) < asteroid.getRadius()) {
                iterator.remove();
                asteroid.deductHP(bullet);
            }
        }
        if (MathUtils.getDistance(player, asteroid) < (asteroid.getRadius() + 10)) {
            player.bounceAsteroid(asteroid);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == renderTimer) {
            displayPanel.repaint();
        } else if (e.getSource() == updateTimer && !isPaused) {
            update();
        } else if (e.getActionCommand() == "Resume") {
            setPaused(false);
        } else if (e.getActionCommand() == "Pause") {
            setPaused(true);
        } else if (e.getActionCommand() == "Restart") {
            restart();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == 1) {
            if (e.getItem() == menuPanel.FPS30BT) {
                menuPanel.FPS30BT.setSelected(true);
                menuPanel.FPS60BT.setSelected(false);
                FPS = 30;
                renderTimer.stop();
                renderTimer = new Timer(1000 / FPS, this);
                renderTimer.start();
            } else if (e.getItem() == menuPanel.FPS60BT) {
                menuPanel.FPS30BT.setSelected(false);
                menuPanel.FPS60BT.setSelected(true);
                FPS = 60;
                renderTimer.stop();
                renderTimer = new Timer(1000 / FPS, this);
                renderTimer.start();
            } else if ((e.getSource() == menuPanel.controllerModeSelectionBox) && controller instanceof KeyController) {
                ((KeyController) controller).setMode(e.getItem().toString());
            }
        } else if (e.getStateChange() == 2) {
            if (!menuPanel.FPS30BT.isSelected() && !menuPanel.FPS60BT.isSelected()) {
                ((JRadioButton) e.getItem()).setSelected(true);
            }
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == menuPanel.sensitivitySlider && controller instanceof KeyController) {
            ((KeyController) controller).setRotateSpeed(((JSlider) e.getSource()).getValue());
        }
    }
}