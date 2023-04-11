package asteroidshooter.main.panel;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JTextArea;

public class InfoPanel extends BasePanel { 
    JTextArea textArea = new JTextArea();
    public InfoPanel() {
        super(new GridLayout());
        textArea.setEditable(false);
        add(textArea);
        setPreferredSize(new Dimension(240,600));
        setFocusable(false);
    }
    public void addText(String playerInfo) {
        textArea.setText("- Player -\n" + playerInfo);
    }
}