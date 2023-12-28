package asteroidshooter.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import asteroidshooter.controller.AbstractController;
import asteroidshooter.controller.KeyController;
import asteroidshooter.main.panel.GameInfoPanel;
import asteroidshooter.main.panel.DisplayPanel;
import asteroidshooter.main.panel.GameMenuPanel;
import asteroidshooter.main.panel.StatusPanel;
import asteroidshooter.main.panel.InstructionPanel;

public class GameWindow extends JFrame {
    private DisplayPanel displayPanel = new DisplayPanel();
    private StatusPanel statusPanel = new StatusPanel();
    private GameMenuPanel menuPanel = new GameMenuPanel();
    private GameInfoPanel gameInfoPanel = new GameInfoPanel();
    private InstructionPanel instructionPanel = new InstructionPanel();

    public GameWindow(String title) {
        super(title);
        setLayout(new BorderLayout());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel southPanel = new JPanel(new GridLayout(1, 2));
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setPreferredSize(new Dimension(230, 600));
        eastPanel.add(instructionPanel, BorderLayout.NORTH);
        eastPanel.add(menuPanel, BorderLayout.CENTER);

        southPanel.add(gameInfoPanel);
        southPanel.setPreferredSize(new Dimension(800, 40));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(displayPanel);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        pack();
        AbstractController controller = new KeyController("WASD");
        addMouseListener(controller);
        new Game(displayPanel, statusPanel, menuPanel, gameInfoPanel, controller);
        displayPanel.requestFocusInWindow();
    }
}
