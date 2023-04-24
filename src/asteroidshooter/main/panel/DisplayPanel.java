package asteroidshooter.main.panel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import asteroidshooter.main.Game;

import java.awt.BasicStroke;
import java.awt.Color;   
public class DisplayPanel extends GamePanel {
    private Color bgColor = Color.WHITE;
    
    public DisplayPanel() {
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
    // Future version
    // public void setDarkColor(boolean dark) {
    //     if (dark) bgColor = new Color(35, 35, 59);
    //     else bgColor = Color.WHITE;
    //     setBackground(bgColor);
    // }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw background timer
        Graphics2D g2d = (Graphics2D)g;
        int size = 150;
        int gap = 10;
        int alphaPercent = 10;
        int x = (int)(getSize().getWidth() - size)/ 2;
        int y = (int)(getSize().getHeight() - size)/ 2;
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(new Color(0,0,0,alphaPercent*256/100));
        if (!game.getPlayer().getIsDead()) {
            g2d.fillArc(x, y, size, size,90,game.getUpdateLeft() * 360 /(game.MAX_TIME_SEC * Game.getUPS()));
            if (game.getTimeLeft() == 0) {
                Color c = Color.GREEN;
                g2d.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(), alphaPercent*256/100));
                g.setFont(new Font("Helvetica", Font.BOLD, 50));
                g.drawString("END", x+22, (int)getSize().getHeight()/2+20);
            } 
        }
        else {
            Color c = Color.RED;
            g2d.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),alphaPercent*256/100));
            g.setFont(new Font("Helvetica", Font.BOLD, 50));
            g.drawString("DIED", x+16, (int)getSize().getHeight()/2+20);
        }
        g2d.drawOval(x-gap, y-gap, size+2*gap, size+2*gap); 

        game.render(g);
    }

    @Override
    public void addGame(Game game) {
        super.addGame(game);
        addMouseMotionListener(game.getController());
        addMouseListener(game.getController());
        addKeyListener(game.getController());
    }
}
