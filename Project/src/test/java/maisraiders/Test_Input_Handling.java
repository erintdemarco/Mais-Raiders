package maisraiders;

import java.awt.event.KeyEvent;

// For fake component in button press

import javax.swing.JButton;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import maisraiders.enums.GameState;
import maisraiders.panel.Game;
import maisraiders.panel.GameLoop;
import maisraiders.movement.KeyHandler;
import maisraiders.ui.Ui;


/**
 *
 * @author MackAndSneeze
 */
public class Test_Input_Handling {
    Game game = new Game();
    GameLoop gl = new GameLoop();
    KeyHandler keyHandler = new KeyHandler(game, gl);

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");    
        this.gl.ui = new Ui(gl, game);
        this.game.gl = this.gl;
    }

    @Test
    void testTitleCursor() {
        game.setGameState(GameState.TITLE);
        //

        int actual = keyHandler._test_getGameLoopUICommandNum();
        int expected = 0;

        assertEquals(expected, actual);
    }

    @Test
    void testTitleCursorUp() {
        game.setGameState(GameState.TITLE);
        //
        // Fake Keyboard Press "W"

        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');

        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);

        int actual = keyHandler._test_getGameLoopUICommandNum();
        int expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void testTitleCursorDown() {
        game.setGameState(GameState.TITLE);
        //
        // Fake Keyboard Press "W"

        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'W');

        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);

        int actual = keyHandler._test_getGameLoopUICommandNum();
        int expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void testRunningCursor() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, false, false};

        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorNorth() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "W"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        this.keyHandler.keyPressed(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {true, false, false, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorNorthRelease() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "W"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, false, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorNorthEast() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "W"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        this.keyHandler.keyPressed(event);
        // Fake Keyboard Press "D"
        event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        this.keyHandler.keyPressed(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {true, true, false, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorNorthEastRelease() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "W"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);
        // Fake Keyboard Press "D"
        event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, false, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorEast() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "D"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        this.keyHandler.keyPressed(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, true, false, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorEastRelease() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "D"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, false, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorSouthEast() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "S"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        this.keyHandler.keyPressed(event);
        // Fake Keyboard Press "D"
        event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        this.keyHandler.keyPressed(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, true, true, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorSouthEastRelease() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "S"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);
        // Fake Keyboard Press "D"
        event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, false, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorSouth() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "S"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        this.keyHandler.keyPressed(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, true, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorSouthRelease() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "S"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, false, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorSouthWest() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "S"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        this.keyHandler.keyPressed(event);
        // Fake Keyboard Press "A"
        event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        this.keyHandler.keyPressed(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, true, true};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorSouthWestRelease() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "S"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);
        // Fake Keyboard Press "A"
        event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, false, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorWest() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "A"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        this.keyHandler.keyPressed(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, false, true};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorWestRelease() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "A"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, false, false};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorNorthWest() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "W"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        this.keyHandler.keyPressed(event);
        // Fake Keyboard Press "A"
        event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        this.keyHandler.keyPressed(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {true, false, false, true};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testRunningCursorNorthWestRelease() {
        game.setGameState(GameState.RUNNING);
        // game.StartGame();
        //
        // Fake Keyboard Press "W"
        KeyEvent event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);
        // Fake Keyboard Press "A"
        event = new KeyEvent(new JButton(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        this.keyHandler.keyPressed(event);
        this.keyHandler.keyReleased(event);

        boolean actual[] = {
            keyHandler._test_isUpPressed(),
            keyHandler._test_isRightPressed(),
            keyHandler._test_isDownPressed(),
            keyHandler._test_isLeftPressed(),
        };
        boolean expected[] = {false, false, false, false};
        assertArrayEquals(expected, actual);
    }
}