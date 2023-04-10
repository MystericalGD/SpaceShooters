package main.panel;
import javax.swing.JPanel;

import controller.AbstractController;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import main.Game;   
public class GamePanel extends BasePanel {
    private Color bgColor = Color.WHITE;
    private Game game;
    
    public GamePanel() {
        setPreferredSize(new Dimension(800,600));
        setFocusable(true);
        
        setBackground(bgColor);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                requestFocus();
            }
        });
        setDoubleBuffered(true);
    }

    public void setDarkColor(boolean dark) {
        if (dark) bgColor = new Color(35, 35, 59);
        else bgColor = Color.WHITE;
        setBackground(bgColor);
    }
    public void addGameListener(Game game) {
        this.game = game;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.renderBullets(g);
        game.renderAsteroids(g);
        game.getPlayer().render(g);
        game.getBorder().render(g);
    }

    public void addController(AbstractController controller) {
        addMouseMotionListener(controller);
        addMouseListener(controller);
        addKeyListener(controller);
    }   
}
