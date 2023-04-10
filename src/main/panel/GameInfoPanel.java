package main.panel;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.lang.NullPointerException;
import main.Game;
public class GameInfoPanel extends BasePanel {
    JPanel scorePanel = new JPanel();
    JLabel scoreLabel = new JLabel();
    HPPanel HPPanelObj = new HPPanel();
    public GameInfoPanel() {
        super(new GridLayout(1,2));
        add(scoreLabel);
        add(HPPanelObj);
    }

    public void update() {
        scoreLabel.setText(String.format("score: %d", game.getScore()));
        HPPanelObj.repaint();
    }

    private class HPPanel extends JPanel {
        HPPanel() {}
        private int xOffset = 25;
        private int yOffset = 5;
        private int HPBarHeight = 15;
        private int edgeDistance = 3;
        protected void paintComponent(Graphics g) {
            try {
                int midPanelY = (int)(getSize().getHeight()/2);
                super.paintComponent(g);
                g.setColor(Color.black);
                g.drawRect(xOffset, midPanelY - HPBarHeight/2  - edgeDistance, 100 + 2*edgeDistance, HPBarHeight + 2*edgeDistance);
                g.fillRect(xOffset + edgeDistance, midPanelY - HPBarHeight/2, (int)Math.round(game.getPlayer().getHP()), HPBarHeight);
                g.drawString(Math.round(game.getPlayer().getHP()) + "/100", 110 + xOffset, midPanelY + 5);
                g.drawString("HP", 0,  midPanelY + 5);
                
            }
            catch (NullPointerException e) {
            }
        }
        
    }
}

