package main.panel;

import javax.swing.border.TitledBorder;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class InstructionPanel extends JPanel {
    String instructionText =
    "<html>" +
        "<ul style=\"margin:10;padding:0\">" +
            "<li> Press W or S to move <br>forward/backward</li>" +
            "<li> Press A or D to turn left/<br>right</li>" +
            "<li> Press Space to shoot</li>" +
            "<li> Press Shift to boost speed</li>" +
        "</ul>" +
    "<html>";
    JLabel instructionArea = new JLabel(instructionText);
    String scoreCalcText =
    "<html>" +
        "<ul style=\"margin:10;padding:0\">" +
            "<li>Score is calculated by<br>"+
            "the points from asteroids<br>"+
            "destroyed plus the<br>"+
            "remaining HP times 4. </li>" +
            "<li>You must survive to<br>update the high score.</li>" +
    "<html>";
    JLabel scoreCalcArea = new JLabel(scoreCalcText);
    public InstructionPanel() {
        setPreferredSize(new Dimension(230,300));

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder("Instruction"));
        p.add(instructionArea);
        JPanel p2 = new JPanel();
        p2.setBorder(new TitledBorder("Scoring"));
        p2.add(scoreCalcArea);
        instructionArea.setBackground(getBackground());
        add(p);
        add(p2);
    }
}
