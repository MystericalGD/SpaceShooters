package asteroidshooter.main.panel;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

import java.lang.NullPointerException;
import java.awt.Dimension;

public class GameInfoPanel extends GamePanel {
    // private TextPanel textPanel = new TextPanel();
    private HPPanel HPPanelObj = new HPPanel();
    private JLabel scoreLabel = new JLabel();
    public GameInfoPanel() {
        super(new FlowLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800,40));
        add(scoreLabel);
        add(HPPanelObj);
    }

    public void update() {
        // textPanel.updateText();
        scoreLabel.setText(String.format("High Score: %d   |   Score: %d", game.getHighestScore(), game.getScore()));
        HPPanelObj.repaint();
    }

    private class HPPanel extends JPanel {
        private int xOffset = 25;
        private int HPBarHeight = 15;
        private int edgeDistance = 3;

        HPPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(250,40));
        }
        
        protected void paintComponent(Graphics g) {
            try {
                int midPanelY = (int)(getSize().getHeight()/2);
                super.paintComponent(g);
                g.drawString("HP", xOffset,  midPanelY + 5);
                g.setColor(Color.black);
                g.drawRect(xOffset + 25, midPanelY - HPBarHeight/2  - edgeDistance, 100 + 2*edgeDistance, HPBarHeight + 2*edgeDistance);
                g.fillRect(xOffset + edgeDistance + 25, midPanelY - HPBarHeight/2, (int)Math.round(game.getPlayer().getHP()), HPBarHeight);
                g.drawString(Math.round(game.getPlayer().getHP()) + "/100", 135 + xOffset, midPanelY + 5);
            }
            catch (NullPointerException e) {}
        }
    }
}

