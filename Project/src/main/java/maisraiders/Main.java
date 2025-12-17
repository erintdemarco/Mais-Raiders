package maisraiders;

import maisraiders.enums.GameState;
import maisraiders.panel.Game;


/**
 * A class to run the game
 */
public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.setGameState(GameState.TITLE);
        game.StartGame();
    }
}