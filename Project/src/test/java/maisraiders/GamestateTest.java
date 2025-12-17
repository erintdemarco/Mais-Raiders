package maisraiders;

import static org.junit.jupiter.api.Assertions.*;

import maisraiders.entities.Farmer;
import maisraiders.enums.GameState;
import maisraiders.panel.Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Junit tests for gamestate transitions
 */
public class GamestateTest {
    private Game game;
    private Farmer farmer;
    private TestableUi ui;

    @BeforeEach
    void setup() {
        game = new Game();
        farmer = new Farmer(); // needed to update game state
        ui = new TestableUi(game);

        game.setGameState(GameState.RUNNING);
    }

    // WIN state
    @Test
    void winWhenAllCornCollectedAndExitReached() {
        // collect enough corn
        for (int i = 0; i < game.getRequiredRegular(); i++) {
            game.addCollectedRegular();
        }
       // reach the exit
        farmer.updateExitStatus(true);
       // update game state
        game.updateGameState(farmer, ui);
        // check if true
        assertEquals(GameState.WIN, game.getGameState());
    }

    @Test
    void cannotWinIfExitReachedButNotEnoughCorn() {

        // only collect some corn
        game.addCollectedRegular(); // < required

        // reach the exit
        farmer.updateExitStatus(true);

        game.updateGameState(farmer, ui);

        // should NOT win
        assertEquals(GameState.RUNNING, game.getGameState());
        // make sure message got shown
        assertEquals("You must collect all the corn before exiting!", ui.message);
    }
    // exit message test
    @Test
    void exitMessageShownOnceWhenAllCornCollected() {
        // collect all corn
        for (int i = 0; i < game.getRequiredRegular(); i++) {
            game.addCollectedRegular();
        }
        // don't reach exit
        farmer.updateExitStatus(false);

        game.updateGameState(farmer, ui);
        // make sure message got shown
        assertEquals("All corn collected! Find the EXIT!", ui.message);
        assertTrue(game.isExitMessageShown());

    }
    // LOOSE state
    @Test
    void loseWhenScoreBelowZero() {
        game.setScore(-1);

        game.updateGameState(farmer, ui);

        assertEquals(GameState.LOSE, game.getGameState());
        assertEquals("You slipped too many times and lost all your corn :(", game.losseMsg);
    }

    @Test
    void loseWhenAlienCollisionWithoutPitchfork() {
        farmer.updateAlienCollision(true);
        farmer.updatePitchfork(false);

        game.updateGameState(farmer, ui);

        assertEquals(GameState.LOSE, game.getGameState());
        assertEquals("You got caught by the aliens :(", game.losseMsg);
    }

    @Test
    void surviveAlienCollisionWithPitchfork() {
        farmer.updateAlienCollision(true);
        farmer.updatePitchfork(true);

        game.updateGameState(farmer, ui);
        // verify game does not end, pitchfork is removed, and msg was displayed
        assertEquals(GameState.RUNNING, game.getGameState());
        assertEquals("Used pitchfork to escape alien!", ui.message);
    }
// Ui tests for game states
    @Test
    void pauseStateCallsPauseScreen() {
        game.setGameState(GameState.PAUSE);

        ui.draw();

        assertTrue(ui.drewPause);
    }

    @Test
    void TitleStateCallsTitleScreen() {
        game.setGameState(GameState.TITLE);

        ui.draw();

        assertTrue(ui.drewTitle);
    }

    @Test
    void LoseStateCallsLoseScreen() {
        game.setGameState(GameState.LOSE);

        ui.draw();

        assertTrue(ui.drewLose);
    }

    @Test
    void WinStateCallsWinScreen() {
        game.setGameState(GameState.WIN);

        ui.draw();

        assertTrue(ui.drewWin);
    }

}
