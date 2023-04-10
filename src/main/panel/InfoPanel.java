package main.panel;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class InfoPanel extends JPanel{
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