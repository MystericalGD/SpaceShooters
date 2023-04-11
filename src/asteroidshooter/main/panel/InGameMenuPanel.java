package asteroidshooter.main.panel;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import asteroidshooter.main.Game;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class InGameMenuPanel extends BasePanel {
    public JRadioButton FPS30 = new JRadioButton("30 FPS");
    public JRadioButton FPS60 = new JRadioButton("60 FPS");
    public JButton restartBT = new JButton("Restart");
    public JButton pauseBT = new JButton("Pause");
    public JButton resumeBT = new JButton("Resume");
    // public JPanel pausePanel = new JPanel(new GridLayout(1,3));
    public JSlider sensitivitySlider = new JSlider(1,5);
    public JPanel pausePanel = new JPanel();
    public JComboBox<String> controllerModeSelectionBox;
    public InGameMenuPanel() {
        super(new FlowLayout());
        setPreferredSize(new Dimension(120,600));
        JPanel FPSPanel = new JPanel();
        JPanel ControllerPanel = new JPanel();
        
        FPS60.setSelected(true);
        FPSPanel.setLayout(new BoxLayout(FPSPanel, BoxLayout.Y_AXIS));
        ControllerPanel.setLayout(new BoxLayout(ControllerPanel, BoxLayout.Y_AXIS));
        // FPSPanel.add(new JLabel("FPS"));
        FPSPanel.add(FPS30);
        FPSPanel.add(FPS60);
        FPSPanel.setBorder(new TitledBorder("FPS"));
        ControllerPanel.setMaximumSize(new Dimension(90, 100));
        ControllerPanel.setBorder(new TitledBorder("C"));
        // ControllerPanel.setBorder(new TitledBorder("Controller"));
        sensitivitySlider.setPreferredSize(new Dimension(120, 50));
        sensitivitySlider.setValue(3);
        sensitivitySlider.setPaintTicks(true);
        sensitivitySlider.setPaintLabels(true);
        sensitivitySlider.setMajorTickSpacing(1);
        sensitivitySlider.setSnapToTicks(true);
        
        controllerModeSelectionBox = new JComboBox<String>(new String[]{"WASD", "Arrow"});
        JLabel sensitivityLabel = new JLabel("Sensitivity");
        JLabel controllerLabel = new JLabel("Controller Mode");
        pausePanel.setPreferredSize(new Dimension(120, 300));
        pausePanel.add(resumeBT);
        pausePanel.add(FPSPanel);
        pausePanel.add(controllerLabel);
        pausePanel.add(controllerModeSelectionBox);
        pausePanel.add(sensitivityLabel);
        pausePanel.add(sensitivitySlider);
        pausePanel.add(restartBT);
        
        add(pauseBT);

    }
    public void changeMenu(Game.Status status) {
        removeAll();
        switch (status) {
            case PLAY:
            add(pauseBT);
            break;

            case PAUSE:
            pausePanel.remove(restartBT);
            pausePanel.add(restartBT);
            add(pausePanel);
            break;

            case END:
            add(restartBT);
            break;
        }
        revalidate();
        repaint();
    }

    public void addGameListener(Game game) {
        // ActionListener
        FPS30.addActionListener(game);
        FPS60.addActionListener(game);
        restartBT.addActionListener(game);
        resumeBT.addActionListener(game);
        pauseBT.addActionListener(game);

        // ItemListener
        FPS30.addItemListener(game);
        FPS60.addItemListener(game);

        sensitivitySlider.addChangeListener(game);
        controllerModeSelectionBox.addItemListener(game);
    }

}
