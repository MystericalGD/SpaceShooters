package main;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class GameWindow extends JFrame {
    private static GamePanel gamePanel = new GamePanel();
    private static InfoPanel infoPanel = new InfoPanel();
    GameWindow(String title) {
        super(title);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // GameWindow gw = new GameWindow("Asteroid Shooter Game");
        

        add(gamePanel);
        add(infoPanel, BorderLayout.EAST);
        pack();
        new Game(gamePanel, infoPanel);
        gamePanel.requestFocusInWindow();
    }
}
