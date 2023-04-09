package main;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class GamePanel extends JPanel {
    private Color bgColor = Color.WHITE;
    GamePanel() {
        setPreferredSize(new Dimension(800,600));
        setFocusable(true);
        
        setBackground(bgColor);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                requestFocus();
            }
        });
    }

    public void setDarkColor(boolean dark) {
        if (dark) bgColor = new Color(35, 35, 59);
        else bgColor = Color.WHITE;
        setBackground(bgColor);
    }

    public void resetGraphics(Graphics g) {
        super.paintComponent(g);
    }
    
}
