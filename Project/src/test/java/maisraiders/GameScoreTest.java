package maisraiders;

    import maisraiders.panel.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A class that preforms unit tests on updating the games score
 */
public class GameScoreTest {
    Game game;
    @BeforeEach
    public void setup() {
        game = new Game();
    }

    @Test
    public void increaseGameScore() {
        int initialScore = game.getScore();
        // farmer collecting regular and bundled corn increases score
        game.addScore(50);
        // verify score adds correctly
        assertEquals(initialScore + 50, game.getScore());
    }

    @Test
    public void decreaseGameScore() {
        int initialScore = game.getScore();
        //mud traps decrease score
        game.addScore(-25);
        assertEquals(initialScore - 25, game.getScore());
    }

    @Test
    public void resetGameScore() {
        game.resetScore();
        assertEquals(0, game.getScore());
    }

    @Test
    public void increaseDecreaseGameScore() {
        int initialScore = game.getScore();
        game.addScore(50);
        assertEquals(initialScore + 50, game.getScore());
        game.addScore(-25);
        game.addScore(-25);
        assertEquals(initialScore, game.getScore());
    }
}
