package maisraiders.panel;

import javax.swing.JFrame;

import maisraiders.movement.KeyHandler;
import maisraiders.ui.SpriteDrawer;
import maisraiders.ui.WindowSetting;

/**
 * Creates and displays game window.
 */
public class GameWindow {
    SpriteDrawer sprite = new SpriteDrawer();
    WindowSetting windowSetting;


    public GameWindow(Game game, GameLoop gl) {
        windowSetting = new WindowSetting(game, gl);

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Mais Raiders");

        window.add(windowSetting);

        window.pack(); // size window to specified sizes in GamePanel
        window.add(sprite);

        // display the window in the center of the screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }

    /**
     * Get panel that draws the game sprites.
     * @return SpriteDrawer used for rendering
     */
    public SpriteDrawer getSpriteDrawer() {
        return sprite;
    }

    /**
     * Get tile size used by window settings
     * @return tile size (pixels)
     */
    public int getTileSize() { return windowSetting.tileSize; }

    /**
     * Get key input handler.
     * @return KeyHandler for keyboard inputs
     */
    public KeyHandler getKeyInput() { return windowSetting.getKeyH(); }

    /**
     * Get current FPS.
     * @return FPS value
     */
    public int getFPS() { return windowSetting.getFPS();}


}
