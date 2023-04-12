package asteroidshooter.main.panel;

import javax.swing.JPanel;

import asteroidshooter.main.Game;

import java.awt.LayoutManager;
abstract class GamePanel extends JPanel{
    protected Game game;
    GamePanel() {
        super();
    }
    GamePanel(LayoutManager layout) {
        super(layout);
    }
    public void addGameListener(Game game) {
        this.game = game;
    }

}
