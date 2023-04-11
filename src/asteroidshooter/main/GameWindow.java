package asteroidshooter.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import asteroidshooter.main.panel.GameInfoPanel;
import asteroidshooter.main.panel.GamePanel;
import asteroidshooter.main.panel.InGameMenuPanel;
import asteroidshooter.main.panel.InfoPanel;
import asteroidshooter.main.panel.InstructionPanel;

public class GameWindow extends JFrame {
    private GamePanel gamePanel = new GamePanel();
    private InfoPanel infoPanel = new InfoPanel();
    private InGameMenuPanel menuPanel = new InGameMenuPanel();
    private GameInfoPanel gameInfoPanel = new GameInfoPanel();
    private InstructionPanel instructionPanel = new InstructionPanel();
    GameWindow(String title) {
        super(title);
        setLayout(new BorderLayout());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        
        JPanel southPanel = new JPanel(new GridLayout(1,2));
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setPreferredSize(new Dimension(230, 600));

        eastPanel.add(instructionPanel, BorderLayout.NORTH);
        eastPanel.add(menuPanel, BorderLayout.CENTER);

        southPanel.add(gameInfoPanel);
        southPanel.setPreferredSize(new Dimension(800, 40));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gamePanel);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        pack();
        new Game(gamePanel, infoPanel, menuPanel, gameInfoPanel);
        gamePanel.requestFocusInWindow();
    }
}
