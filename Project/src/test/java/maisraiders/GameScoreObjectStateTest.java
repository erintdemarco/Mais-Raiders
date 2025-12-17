package maisraiders;
import maisraiders.entities.Farmer;
import maisraiders.enums.GameState;
import maisraiders.object.*;
import maisraiders.panel.Game;
import maisraiders.panel.GameLoop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A class that preforms integration tests between the GameObject, Farmer, and Game, and Ui
 * these tests make sure that object collecting properly updates the score and game states
 */
public class GameScoreObjectStateTest {
    Game game;
    Farmer farmer;
    GameObject obj;
    TestableUi ui;
    int tileSize = 64;
    GameLoop gl;

    @BeforeEach
    public void setup() {
        game = new Game();
        gl = new GameLoop();
        ui = new TestableUi(gl, game);

        game.setGameState(GameState.RUNNING);
    }
// CORN related tests
    @Test
    public void increaseGameScoreWhenSingleCornCollected() {
        int initialScore = game.getScore();
        int collected = game.getCollectedRegular();

        // farmer collecting regular and bundled corn increases score
        obj = new Corn(new Point(tileSize * 2, tileSize * 2));
        farmer = new Farmer(new maisraiders.util.Point(2,2), 4);

        // update object-> farmer collides with object, score += 50
        obj.updateObject(farmer, game, ui);
        // update ui
        ui.draw();
        // verify score adds correctly
        assertEquals(initialScore + 50, game.getScore());
        // verify count is updated
        assertEquals(collected + 1, game.getCollectedRegular());
        // verify corn object is removed
        assertTrue(obj.isCollected);
        // verify ui is drawing play screen
        assertTrue(ui.drewPlay);
    }

    @Test
    public void increaseGameScoreWhenAllCornCollected() {
        int initialScore = game.getScore();
        for (int i = 0; i < game.getRequiredRegular(); i++) {
            // farmer collecting regular and bundled corn increases score
            obj = new Corn(new Point(tileSize * 2, tileSize * 2));
            farmer = new Farmer(new maisraiders.util.Point(2, 2), 4);
            // update object-> farmer collides with object, score += 50
            obj.updateObject(farmer, game, ui);
        }
        game.updateGameState(farmer, ui);
        ui.draw();
        // verify score adds correctly
        assertEquals(initialScore + 50 * game.getRequiredRegular(), game.getScore());
        assertEquals(game.getRequiredRegular(), game.getCollectedRegular());
        assertEquals("All corn collected! Find the EXIT!", ui.message);
        assertTrue(game.isExitMessageShown());
        // verify player hasn't won, no exit status set
        assertEquals(GameState.RUNNING, game.getGameState());
        // verify ui is drawing play screen
        assertTrue(ui.drewPlay);
    }

    @Test
    public void cornCollectionAndExitTriggerWinScreen() {
        int initialScore = game.getScore();
        for (int i = 0; i < game.getRequiredRegular(); i++) {
            // farmer collecting regular and bundled corn increases score
            obj = new Corn(new Point(tileSize * 2, tileSize * 2));
            farmer = new Farmer(new maisraiders.util.Point(2, 2), 4);
            // update object-> farmer collides with object, score += 50
            obj.updateObject(farmer, game, ui);
        }
        farmer.updateExitStatus(true);
        game.updateGameState(farmer, ui);
        ui.draw();

        // verify score adds correctly
        assertEquals(initialScore + 50 * game.getRequiredRegular(), game.getScore());
        assertEquals(game.getRequiredRegular(), game.getCollectedRegular());
        assertEquals("All corn collected! Find the EXIT!", ui.message);
        assertTrue(game.isExitMessageShown());
        assertEquals(GameState.WIN, game.getGameState());
        // verify ui is drawing win screen
        assertTrue(ui.drewWin);
    }

    // BUNDLE CORN related tests
    @Test
    public void increaseGameScoreWhenBundleCornCollected() {
        int initialScore = game.getScore();

        // farmer collecting regular and bundled corn increases score
        obj = new CornBundle(new Point(tileSize * 2, tileSize * 2));
        farmer = new Farmer(new maisraiders.util.Point(2,2), 4);

        // update object-> farmer collides with object, score += 150
        obj.updateObject(farmer, game, ui);
        ui.draw();

        // verify score adds correctly
        assertEquals(initialScore + 150, game.getScore());
        assertTrue(obj.isCollected);
        // verify ui is drawing play screen
        assertTrue(ui.drewPlay);
    }

    // MUD related tests
    @Test
    public void decreaseGameScoreWhenMudTriggered() {
        game.setScore(100); // don't want score to go negative
        int initialScore = game.getScore();

        // farmer collecting regular and bundled corn increases score
        obj = new MudTrap(new Point(tileSize * 2, tileSize * 2));
        farmer = new Farmer(new maisraiders.util.Point(2,2), 4);

        // update object-> farmer collides with object, score -= 25
        obj.updateObject(farmer, game, ui);
        ui.draw();
        // verify score adds correctly
        assertEquals(initialScore - 25, game.getScore());
        assertTrue(obj.isTriggered);
        // verify ui is drawing play screen
        assertTrue(ui.drewPlay);
    }
    @Test
    public void decreaseGameScoreOnceWhenMudTriggeredForExtendedTime() {
        game.setScore(100); // don't want score to go negative
        int initialScore = game.getScore();

        // farmer collecting regular and bundled corn increases score
        obj = new MudTrap(new Point(tileSize * 2, tileSize * 2));
        farmer = new Farmer(new maisraiders.util.Point(2,2), 4);

        // update object-> farmer collides with object, score -= 25
        obj.updateObject(farmer, game, ui);
        obj.updateObject(farmer, game, ui);
        ui.draw();

        // verify score adds correctly, score should only decrease once
        assertEquals(initialScore - 25, game.getScore());
        assertTrue(obj.isTriggered);
        // verify farmer is slowed to speed 2
        assertEquals(2, farmer.getMoveSpeed());
        // verify ui is drawing play screen
        assertTrue(ui.drewPlay);
    }

    @Test
    public void farmerSpeedResetAfterMudTriggered() {
        int initialScore = game.getScore();

        // farmer collecting regular and bundled corn increases score
        obj = new MudTrap(new Point(tileSize * 2, tileSize * 2));
        farmer = new Farmer(new maisraiders.util.Point(2,2), 4);

        // update object-> farmer collides with object, score -= 25
        obj.updateObject(farmer, game, ui);
        // move farmer to reset speed and trigger status
        farmer.setSubPositionX(5);
        farmer.setSubPositionY(5);
        obj.updateObject(farmer, game, ui);
        ui.draw();

        // verify score adds correctly, score should only decrease once
        assertEquals(initialScore - 25, game.getScore());
        assertFalse(obj.isTriggered);
        // verify farmer speed is reset
        assertEquals(4, farmer.getMoveSpeed());
        // verify ui is drawing play screen
        assertTrue(ui.drewPlay);
    }

    @Test
    public void scoreDecreasesWhenMudTrapReTriggered() {
        game.setScore(100);
        int initialScore = game.getScore();

        // farmer collecting regular and bundled corn increases score
        obj = new MudTrap(new Point(tileSize * 2, tileSize * 2));
        farmer = new Farmer(new maisraiders.util.Point(2,2), 4);

        // update object-> farmer collides with object, score -= 25
        obj.updateObject(farmer, game, ui);
        // move farmer to reset speed and trigger status
        farmer.setSubPositionX(5);
        farmer.setSubPositionY(5);
        obj.updateObject(farmer, game, ui);

        // retrigger the same trap, score decreases again
        farmer.setSubPositionX(2);
        farmer.setSubPositionY(2);
        obj.updateObject(farmer, game, ui);
        ui.draw();

        // verify score adds correctly, score should decrease twice
        assertEquals(initialScore - 50, game.getScore());
        assertTrue(obj.isTriggered);
        assertEquals(2, farmer.getMoveSpeed());
        // verify ui is drawing play screen
        assertTrue(ui.drewPlay);
    }

    @Test
    public void negativeScoreLoseScreen() {
        int initialScore = game.getScore();

        // farmer collecting regular and bundled corn increases score
        obj = new MudTrap(new Point(tileSize * 2, tileSize * 2));
        farmer = new Farmer(new maisraiders.util.Point(2,2), 4);

        // update object-> farmer collides with object, score -= 25
        obj.updateObject(farmer, game, ui);
        game.updateGameState(farmer, ui);
        ui.draw();

        // verify score adds correctly,
        assertEquals(initialScore - 25, game.getScore());
        assertEquals(GameState.LOSE, game.getGameState());
        assertEquals("You slipped too many times and lost all your corn :(", game.losseMsg);
        // verify ui is drawing lose screen
        assertTrue(ui.drewLose);
    }

  // PITCHFORK related tests
  @Test
  public void farmerDoesNotDieWithPitchFork() {

      obj = new Pitchfork(new Point(tileSize * 2, tileSize * 2));
      farmer = new Farmer(new maisraiders.util.Point(2,2), 4);

      // update object-> farmer collides with object, update farmer booleans
      obj.updateObject(farmer, game, ui);

      // verify obj was collected,
      assertTrue(farmer.pitchforkStatus());
      assertEquals("You collected the pitchfork!", ui.message);
      assertTrue(obj.isCollected);

      // farmer and alien collide while farmer has pitchfork
      farmer.updateAlienCollision(true);
      game.updateGameState(farmer, ui);
      ui.draw();

      assertEquals(GameState.RUNNING, game.getGameState());
      assertEquals("Used pitchfork to escape alien!", ui.message);
      // verify ui is drawing play screen
      assertTrue(ui.drewPlay);
  }

    @Test
    public void farmerDiesWithPitchFork() {

        obj = new Pitchfork(new Point(tileSize * 2, tileSize * 2));
        farmer = new Farmer(new maisraiders.util.Point(2,2), 4);

        // update object-> farmer collides with object, update farmer booleans
        obj.updateObject(farmer, game, ui);

        // farmer and alien collide while farmer has pitchfork
        farmer.updateAlienCollision(true);
        game.updateGameState(farmer, ui);
        farmer.updatePitchfork(false);
        ui.draw();

        assertEquals(GameState.RUNNING, game.getGameState());
        assertEquals("Used pitchfork to escape alien!", ui.message);
        // verify ui is drawing play screen
        assertTrue(ui.drewPlay);

        // collide again after losing pitchfork
        farmer.updateAlienCollision(true);
        game.updateGameState(farmer, ui);
        ui.draw();

        assertEquals(GameState.LOSE, game.getGameState());
        assertEquals("You got caught by the aliens :(", game.losseMsg);
        // verify ui is drawing play screen
        assertTrue(ui.drewLose);
    }


}