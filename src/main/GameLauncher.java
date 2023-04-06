package main;

public class GameLauncher {
    public GameLauncher() {
        GameWindow gw = new GameWindow();
        GamePanel gamePanel = new GamePanel();
        gw.add(gamePanel);

        new Game(gamePanel);

    }
}
