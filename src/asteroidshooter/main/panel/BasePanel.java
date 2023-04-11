package asteroidshooter.main.panel;

import javax.swing.JPanel;

import asteroidshooter.main.Game;

import java.awt.LayoutManager;
abstract class BasePanel extends JPanel{
    protected Game game;
    BasePanel() {
        super();
    }
    BasePanel(LayoutManager layout) {
        super(layout);
    }
    public void addGameListener(Game game) {
        this.game = game;
    }

}
