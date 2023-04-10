package main.panel;

import javax.swing.JPanel;
import java.awt.LayoutManager;
import main.Game;
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
