package maisraiders;

import maisraiders.enums.GameState;
import maisraiders.panel.Game;
import maisraiders.panel.GameLoop;
import maisraiders.ui.Ui;

/**
 * A testable ui class that sets game state specific bools when ui draw methods are called
 * actual visual aspects of ui are to be manually tested/verified
 */
public class TestableUi extends Ui {

    public boolean drewTitle = false;
    public boolean drewPlay = false;
    public boolean drewPause = false;
    public boolean drewWin = false;
    public boolean drewLose = false;
    Game game;
    GameLoop gl;

    public TestableUi(Game game) {this.game = game;} // empty default constructor for testing

    public TestableUi(GameLoop gl, Game game) {
        this.gl = gl;
        this.game = game;
    }

    public void draw() {

        //TITLE STATE
        if(game.getGameState() == GameState.TITLE){
            drawTitleScreen();
        }
        // PLAY STATE
        if(game.getGameState() == GameState.RUNNING){
            drawPlayScreen();

        }
        // PAUSE STATE
        if(game.getGameState() == GameState.PAUSE){
            drawPauseScreen();
        }
        // WIN STATE
        if(game.getGameState() == GameState.WIN){
            drawWinScreen();
        }
        // LOSE STATE
        if(game.getGameState() == GameState.LOSE){
            drawLoseScreen();
        }

    }

    @Override
    public void drawTitleScreen() {
        drewTitle = true;
    }

    @Override
    public void drawPlayScreen() {
        drewPlay = true;
    }

    @Override
    public void drawPauseScreen() {
        drewPause = true;
    }

    @Override
    public void drawWinScreen() {
        drewWin = true;
    }

    @Override
    public void drawLoseScreen() {
        drewLose = true;
    }
}
