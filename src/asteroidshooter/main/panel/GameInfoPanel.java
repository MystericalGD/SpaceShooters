package asteroidshooter.main.panel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


import java.lang.NullPointerException;
import java.awt.Component;
import java.awt.Dimension;

public class GameInfoPanel extends BasePanel {
    TextPanel textPanel = new TextPanel();
    HPPanel HPPanelObj = new HPPanel();
    JLabel scoreLabel = new JLabel();
    public GameInfoPanel() {
        super(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        
        setPreferredSize(new Dimension(160,40));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.3;
        c.weighty = 1;
        c.gridheight = 3;
        c.gridx = 0;
        c.ipady = 40;
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(scoreLabel,c);
        c.weightx = 0.5;
        c.gridx = 1;
        add(HPPanelObj, c);
    }

    public void update() {
        // textPanel.updateText();
        scoreLabel.setText(String.format("High Score: %d   |   Score: %d", game.getHighestScore(), game.getScore()));
        HPPanelObj.repaint();
    }

    private class HPPanel extends JPanel {
        HPPanel() {
            setBackground(Color.WHITE);
        }
        private int xOffset = 25;
        private int HPBarHeight = 15;
        private int edgeDistance = 3;
        protected void paintComponent(Graphics g) {
            try {
                int midPanelY = (int)(getSize().getHeight()/2);
                super.paintComponent(g);
                g.setColor(Color.black);
                g.drawRect(xOffset + 25, midPanelY - HPBarHeight/2  - edgeDistance, 100 + 2*edgeDistance, HPBarHeight + 2*edgeDistance);
                g.fillRect(xOffset + edgeDistance + 25, midPanelY - HPBarHeight/2, (int)Math.round(game.getPlayer().getHP()), HPBarHeight);
                g.drawString(Math.round(game.getPlayer().getHP()) + "/100", 135 + xOffset, midPanelY + 5);
                g.drawString("HP", xOffset,  midPanelY + 5);
            }
            catch (NullPointerException e) {}
        }
    }
    private class TextPanel extends JPanel {
        JLabel scoreLabel = new JLabel();
        
        TextPanel() {
            setLayout(new FlowLayout());
            add(scoreLabel);
            scoreLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            scoreLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        }
    }
}

