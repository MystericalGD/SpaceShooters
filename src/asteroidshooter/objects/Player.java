package asteroidshooter.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

import asteroidshooter.main.Game;
import asteroidshooter.utils.MathUtils;

public class Player extends GameObject {
    public boolean isReloadDone = true;
    public boolean isBoosted = false;
    public boolean isHit = false;
    private final int MAX_VELOCITY = 300;
    private final double RELOAD_TIME = 0.25;
    private final double REGEN_BOOST_TIME = 0.75;
    private final int MAX_DEATH_FRAME = 60;
    private final double ACCELERATION = 1;
    private long MAX_RELOAD_STATUS;
    private final long MAX_REGEN_BOOST_STATUS;
    private final long MAX_BULLETS_SHOT = 20;
    private long reloadStatus;
    private long regenBoostStatus;
    private boolean wasHit = false;
    private double velocityX = 0;
    private double velocityY = 0;
    private double thetaTrue = 0;
    private double thetaUpdate = 0;
    private double HP = 100;
    private boolean triggerShoot = false;
    private boolean triggerBoost = false;
    private char accelerateDirection = '0';
    private int max_velocity;
    private Color fireColor = Color.GRAY;
    private Color playerColorPrimary = Color.BLACK;
    private Color playerColorSecondary = Color.GRAY;
    private int deathFrame = 0;
    private GameObject[] deathPoints = new GameObject[10];

    public Player() {
        this(400, 300);
    }

    public Player(Game game) {
        this((int) game.getGamePanelSize().getWidth() / 2,
                (int) game.getGamePanelSize().getHeight() / 2);
        this.game = game;
    }

    public Player(int x, int y) {
        super(x, y);
        max_velocity = MAX_VELOCITY;
        MAX_RELOAD_STATUS = Math.round(RELOAD_TIME * Game.getUPS());
        MAX_REGEN_BOOST_STATUS = Math.round(REGEN_BOOST_TIME * Game.getUPS());
        reloadStatus = MAX_RELOAD_STATUS;
        regenBoostStatus = MAX_REGEN_BOOST_STATUS;
    }

    public void render(Graphics g) {
        if (!getIsDead()) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform old = g2d.getTransform();

            g2d.translate(x, y);
            g2d.rotate(theta);
            int[] playerShapeX = new int[] { -10, 5, 15, 5, -10 };
            int[] playerShapeY = new int[] { -10, -10, 0, 10, 10 };

            g2d.setColor(playerColorPrimary);
            g2d.fillPolygon(playerShapeX, playerShapeY, playerShapeX.length);
            g2d.setColor(playerColorSecondary);
            g2d.fillOval(-5, -5, 10, 10);

            if (accelerateDirection == 'F' && !game.getPaused()) {
                g2d.setColor(fireColor);
                g2d.fillPolygon(
                        new int[] { -10, -15, -13, -17, -13, -15, -10 },
                        new int[] { 10, 10, 5, 0, -5, -10, -10 }, 7);
            }
            // g2d.drawOval(-10,-10,20,20);
            g2d.setTransform(old);
        } else {
            deathAnimation(g);
        }
    }

    public void update() {
        updateTheta();
        updatePos();
        updateShoot();
        updateBoost();
        wasHit = isHit;
    }

    protected void updatePos() {
        accelerate();
        getBorderHit(game.getBorder());
        x += (velocityX / Game.getUPS());
        y += (velocityY / Game.getUPS());
    }

    private void updateTheta() {
        thetaTrue += thetaUpdate;
        thetaTrue %= (2 * Math.PI);
        if (thetaTrue < 0)
            thetaTrue += (2 * Math.PI);
        theta = Math.toRadians(Math.round(Math.toDegrees(thetaTrue) / 5) * 5);
        // System.out.println(theta);
    }

    public void setThetaUpdate(double degree) {
        thetaUpdate = Math.toRadians(degree);
    }

    // public void setThetaUpdateZero() {
    //     thetaUpdate = 0;
    // }

    public void setAccelerateDirection(char d) {
        accelerateDirection = d;
    }

    public void accelerate() {
        double targetVelocityX = max_velocity * Math.cos(theta);
        double targetVelocityY = max_velocity * Math.sin(theta);
        if (accelerateDirection == 'F') {
            velocityX += (targetVelocityX - velocityX) * ACCELERATION / Game.getUPS();
            velocityY += (targetVelocityY - velocityY) * ACCELERATION / Game.getUPS();
        } else if (accelerateDirection == 'B') {
            velocityX += (-targetVelocityX - velocityX) * ACCELERATION / Game.getUPS();
            velocityY += (-targetVelocityY - velocityY) * ACCELERATION / Game.getUPS();
        } else if (accelerateDirection == '0') {
            velocityX += (0 - velocityX) * ACCELERATION / (Game.getUPS() * 1.3);
            velocityY += (0 - velocityY) * ACCELERATION / (Game.getUPS() * 1.3);
        }
    }

    public void setTriggerShoot(boolean bool) {
        triggerShoot = bool;
    }

    public void setTriggerBoost(boolean bool) {
        triggerBoost = bool;
    }

    private void updateShoot() {
        if (!getIsDead()) {
            if (triggerShoot && isReloadDone && game.BulletsList.size() < MAX_BULLETS_SHOT) {
                reloadStatus = 0;
                isReloadDone = false;
                game.BulletsList.add(new Bullet(x, y, theta, game));
            }
            if (reloadStatus < MAX_RELOAD_STATUS) {
                reloadStatus++;
            } else
                isReloadDone = true;
        }
    }

    private void updateBoost() {
        if (triggerBoost && (regenBoostStatus == MAX_REGEN_BOOST_STATUS)) {
            boostSpeed(true);
        } else
            boostSpeed(false);
        if (regenBoostStatus < MAX_REGEN_BOOST_STATUS) {
            regenBoostStatus++;
        }
    }

    public int getBorderHit(Border border) {
        if (x + velocityX / Game.getUPS() <= border.getX() + 10) {
            velocityX *= -0.6;
            return 1;
        } else if (x + velocityX / Game.getUPS() >= border.getX() + border.getWidth() - 10) {
            velocityX *= -0.6;
            return 3;
        } else if (y + velocityY / Game.getUPS() <= border.getY() + 10) {
            velocityY *= -0.6;
            return 0;
        } else if (y + velocityY / Game.getUPS() >= border.getY() + border.getHeight() - 10) {
            velocityY *= -0.6;
            return 2;
        } else
            return -1;
    }

    public String getInfo() {
        String directionStr = (accelerateDirection == 'F') ? "Forward"
                : (accelerateDirection == 'B') ? "Backward" : "Default";
        String info = "Direction: " + directionStr + '\n' +
                String.format("Theta (degrees): %d\n", Math.round(Math.toDegrees(theta))) +
                String.format("Position (x,y): (%.2f, %.2f)\n", x, y) +
                String.format("Velocity (x,y): (%.2f, %.2f)", velocityX, velocityY);
        return info;
    }

    public void boostSpeed(boolean b) {
        isBoosted = b;
        if (b)
            max_velocity = (int) (MAX_VELOCITY * 1.5);
        else
            max_velocity = (int) (MAX_VELOCITY / 1.5);
    }

    public boolean getIsDead() {
        if (Math.round(HP) <= 0) {
            HP = 0;
            return true;
        } else
            return false;
    };

    public double getHP() {
        return HP;
    }

    public double getVelocity() {
        return Math.sqrt(velocityX * velocityX + velocityY * velocityY);
    }

    public void bounceAsteroid(Asteroid asteroid) {
        isHit = true;
        double thetaHit = MathUtils.getAngle(asteroid, this);
        if (isHit != wasHit) {
            double v_fromAsteroid = asteroid.getVelocity();
            double oldVelocity = getVelocity();
            velocityX = 0.8 * oldVelocity * Math.cos(thetaTrue - Math.PI - 2 * thetaHit)
                    + v_fromAsteroid * (Math.cos(thetaHit)) * 1;
            velocityY = 0.8 * oldVelocity * Math.sin(thetaTrue - Math.PI - 2 * thetaHit)
                    + v_fromAsteroid * (Math.sin(thetaHit)) * 1;
            if (game.getTimeLeft() > 0)
                HP -= Math.log10(asteroid.getRadius())
                        * Math.sqrt(Math.pow(velocityX - oldVelocity * Math.cos(thetaTrue), 2)
                                + Math.pow(velocityY - oldVelocity * Math.sin(thetaTrue), 2))
                        / Game.getUPS();
        }
        x = asteroid.getX() + (Math.cos(thetaHit) * (asteroid.getRadius() + 10)) + velocityX / Game.getUPS();
        y = asteroid.getY() + (Math.sin(thetaHit) * (asteroid.getRadius() + 10)) + velocityY / Game.getUPS();
        regenBoostStatus = 0;
        Border border = game.getBorder();
        switch (getBorderHit(game.getBorder())) {
            case 0:
                y = border.getY() + 15;
                break;

            case 1:
                x = border.getX() + 15;
                break;

            case 2:
                y = border.getY() + border.getHeight() - 15;
                break;

            case 3:
                x = border.getX() + border.getWidth() - 15;
                break;
            default:
                break;
        }

    }

    private void deathAnimation(Graphics g) {
        Random rng = new Random();
        if (deathFrame == 0) {
            deathPoints[0] = new GameObject(x, y, (rng.nextDouble()) * Math.PI * 2) {
                public void update() {
                }

                protected void updatePos() {
                }

                public void render(Graphics graphics) {
                    graphics.setColor(new Color(0, 0, 0, (MAX_DEATH_FRAME - deathFrame) * 128 / MAX_DEATH_FRAME));
                    graphics.fillOval((int) x - deathFrame * 2, (int) y - deathFrame * 2, deathFrame * 4,
                            deathFrame * 4);
                }
            };
            for (int i = 1; i < deathPoints.length; i++) {
                deathPoints[i] = new GameObject(x, y, (rng.nextDouble()) * Math.PI * 2) {
                    public void update() {
                        updatePos();
                    }

                    public void updatePos() {
                        x += rng.nextDouble() * 2 * Math.cos(theta);
                        y += rng.nextDouble() * 2 * Math.sin(theta);
                    }

                    public void render(Graphics graphics) {
                        graphics.setColor(new Color(0, 0, 0, (MAX_DEATH_FRAME - deathFrame) * 256 / MAX_DEATH_FRAME));
                        graphics.fillOval((int) x - 2, (int) y - 2, 4, 4);
                    }
                };
            }
            deathFrame++;
        } else if (deathFrame < MAX_DEATH_FRAME) {
            for (GameObject o : deathPoints) {
                o.update();
                o.render(g);
            }
            deathFrame++;
        }
    }
}
