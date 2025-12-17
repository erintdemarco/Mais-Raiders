package maisraiders.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import maisraiders.movement.KeyHandler;
import maisraiders.panel.Game;
import maisraiders.panel.GameLoop;

/**
 * Extends JPanel to set specific window settings
 */
public class WindowSetting extends JPanel {

    // SCREEN SETTINGS
    final int originalTileSize = 32; // 32 x 32 tile
    final int scale = 2;

    public int tileSize = originalTileSize * scale; // 64 x 64 tile
    public static final int maxScreenCol = 20;
    public static final int maxScreenRow = 16;
    private int screenWidth = tileSize * maxScreenCol; // 1280 pixels
    private int screenHeight = tileSize * maxScreenRow; // 1024 pixels

    // FPS
    private final int FPS = 60;
    // Keyboard input source
    private KeyHandler keyH;


    public WindowSetting(Game game, GameLoop gl) {
        keyH = new KeyHandler(game, gl);

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // for better rendering performance
        this.addKeyListener(keyH);
        this.setFocusable(true); // let's window receive key inputs
    }
    /**
     * get method for key handler
     */
    public KeyHandler getKeyH() {
        return keyH;
    }
    /**
     * get method for FPS
     */
    public int getFPS() {
        return FPS;
    }
    /**
     * TEST HOOK to return (override) Key Handler
     * @return local Key Handler
     */
    public KeyHandler _test_getKeyHandler() {
        return this.keyH;
    }
}
