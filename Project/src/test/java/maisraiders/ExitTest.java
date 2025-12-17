package maisraiders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import maisraiders.entities.Farmer;
import maisraiders.enums.GameState;
import maisraiders.panel.Game;

/**
 * Simple, focused tests for exit / win behaviour.
 *
 * These tests avoid UI and reflection by exercising only the game logic:
 * - farmer at exit with too little corn -> game stays RUNNING
 * - farmer at exit with enough corn -> game transitions to WIN
 */
public class ExitTest {

    @Test
    void exitWithoutEnoughCornDoesNotWin() {
        System.setProperty("java.awt.headless", "true");

        SampleGame sg = SampleGame.simple();
        Farmer farmer = sg.farmer;
        Game game = new Game();
        game.setGameState(GameState.RUNNING);

        // farmer stands on exit
        farmer.updateExitStatus(true);

        // run game logic that checks exit conditions
        game.updateGameState(farmer, null);

        // still running because not enough corn collected
        assertEquals(GameState.RUNNING, game.getGameState());
    }
@Test
    void exitWithEnoughCornWinsGame() {
        System.setProperty("java.awt.headless", "true");

        SampleGame sg = SampleGame.simple();
        Farmer farmer = sg.farmer;
        Game game = new Game();
        game.setGameState(GameState.RUNNING);

        // give the game the required amount of regular corn
        int required = game.getRequiredRegular();
        for (int i = 0; i < required; i++) {
            game.addCollectedRegular();
        }

        // farmer stands on exit
        farmer.updateExitStatus(true);

        // run game logic that checks exit conditions
        game.updateGameState(farmer, null);

        // now the game should be in WIN state
        assertEquals(GameState.WIN, game.getGameState());
    }
}