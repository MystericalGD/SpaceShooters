package main.panel;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;

import main.Game;

public class InGameMenuPanel extends BasePanel {
    public JRadioButton FPS30 = new JRadioButton("30 FPS");
    public JRadioButton FPS60 = new JRadioButton("60 FPS");
    public JButton restartBT = new JButton("Restart");
    public JButton pauseBT = new JButton("Pause");
    public JButton resumeBT = new JButton("Resume");
    // public JPanel pausePanel = new JPanel(new GridLayout(1,3));
    public JSlider sensitivitySlider = new JSlider(1,5);
    public JPanel pausePanel = new JPanel(new FlowLayout());
    public InGameMenuPanel() {
        super();
        setPreferredSize(new Dimension(100,600));
        add(pauseBT);
        JPanel FPSPanel = new JPanel(new FlowLayout());

        FPS60.setSelected(true);
        FPSPanel.setLayout(new BoxLayout(FPSPanel, BoxLayout.Y_AXIS));
        // FPSPanel.add(new JLabel("FPS"));
        FPSPanel.add(FPS30);
        FPSPanel.add(FPS60);
        FPSPanel.setBorder(new TitledBorder("FPS"));
        sensitivitySlider.setValue(2);
        sensitivitySlider.setPreferredSize(new Dimension(100, 40));
        sensitivitySlider.setPaintTicks(true);
        sensitivitySlider.setMajorTickSpacing(1);
        sensitivitySlider.setSnapToTicks(true);
        
        pausePanel.setPreferredSize(new Dimension(100, 600));
        pausePanel.add(resumeBT);
        pausePanel.add(FPSPanel);
        pausePanel.add(new JLabel("Sensitivity"));
        pausePanel.add(sensitivitySlider);
        pausePanel.add(restartBT);

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
        sensitivitySlider.addChangeListener(game);
        // ItemListener
        FPS30.addItemListener(game);
        FPS60.addItemListener(game);
    }

}
