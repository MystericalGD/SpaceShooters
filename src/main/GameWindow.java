package main;

import javax.swing.JFrame;
import controller.AbstractController;
// import controller.MouseKeyController;
import controller.KeyController;


public class GameWindow extends JFrame {
    GameWindow() {
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800,600);
    }
}
