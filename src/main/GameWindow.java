package main;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.panel.GameInfoPanel;
import main.panel.GamePanel;
import main.panel.InGameMenuPanel;
import main.panel.InfoPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

public class GameWindow extends JFrame {
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private InGameMenuPanel menuPanel;
    private GameInfoPanel gameInfoPanel;
    GameWindow(String title) {
        super(title);
        setLayout(new BorderLayout());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        gamePanel = new GamePanel();
        infoPanel = new InfoPanel();
        menuPanel = new InGameMenuPanel();
        gameInfoPanel = new GameInfoPanel();

        JPanel southPanel = new JPanel(new GridLayout(1,2));
        southPanel.add(menuPanel);
        southPanel.add(gameInfoPanel);
        southPanel.setPreferredSize(new Dimension(800, 40));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gamePanel);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        // add(gamePanel);
        add(infoPanel, BorderLayout.EAST);
        pack();
        new Game(gamePanel, infoPanel, menuPanel, gameInfoPanel);
        gamePanel.requestFocusInWindow();
    }
}
