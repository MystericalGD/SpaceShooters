package asteroidshooter.main.panel;

import javax.swing.border.TitledBorder;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;
public class InstructionPanel extends JPanel {
    private String instructionText =
    "<html>" +
        "<ul style=\"margin:10;padding:0\">" +
            "<li> Press W or S to move <br>forward/backward</li>" +
            "<li> Press A or D to turn left/<br>right</li>" +
            "<li> Press Space to shoot</li>" +
            "<li> Press Shift to boost speed</li>" +
        "</ul>" +
    "<html>";
    private String scoringText =
    "<html>" +
    "<ul style=\"margin:10;padding:0\">" +
    "<li>Score is calculated by<br>"+
    "the points from asteroids<br>"+
    "destroyed plus the<br>"+
    "remaining HP times 4. </li>" +
    "<li>You must survive to<br>update the high score.</li>" +
    "<html>";

    public InstructionPanel() {
        super();
        setPreferredSize(new Dimension(getWidth(),310));
        JLabel instructionLabel = new JLabel(instructionText);
        JLabel scoringLabel = new JLabel(scoringText);
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(210,150));
        p.setBorder(new TitledBorder("Instruction"));
        p.add(instructionLabel);
        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(210,150));
        p2.setBorder(new TitledBorder("Scoring"));
        p2.add(scoringLabel);
        System.out.println();
        instructionLabel.setBackground(getBackground());
        add(p);
        add(p2);
    }
}
