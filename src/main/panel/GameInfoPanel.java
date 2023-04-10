package main.panel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Game;
public class GameInfoPanel extends JPanel {
    Game game;
    JLabel scoreLabel = new JLabel("Score: 0");
    JLabel HPLabel = new JLabel("HP: 100");
    public GameInfoPanel() {
        super(new GridLayout(1,2));
        add(scoreLabel);
        add(HPLabel);
    }
    public void addGameListener(Game game) {
        this.game = game;
    }
    public void update() {
        scoreLabel.setText(String.format("score: %d", game.getScore()));
        HPLabel.setText(String.format("HP: %d", Math.round(game.getPlayer().getHP())));
    }
}
