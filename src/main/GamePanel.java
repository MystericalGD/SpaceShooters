package main;
import javax.swing.JPanel;
import java.awt.Graphics;

public class GamePanel extends JPanel {
    GamePanel() {
        setFocusable(true);
    }
    protected void paintComponent(Graphics g) {}
    public void resetGraphics(Graphics g) {
        super.paintComponent(g);

    }
}
