package maisraiders.movement;

import maisraiders.enums.GameState;
import maisraiders.panel.Game;
import maisraiders.panel.GameLoop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Implements KeyListener to get player inputs for movement
 */
public class KeyHandler implements KeyListener {
    Game game;
    GameLoop gl;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
   // pass the game class to check game States
    public KeyHandler(Game game, GameLoop gl){
        this.game = game;
        this.gl = gl;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Set corresponding directional flag when key is pressed.
     * @param e key event
     */
    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        //TITLE STATE
        if (game.getGameState() == GameState.TITLE  ){
            if (code == KeyEvent.VK_W){
                gl.ui.commandNum--;
                gl.sound.playSE(7);
                if (gl.ui.commandNum < 0){ gl.ui.commandNum = 1;}
            }
            if (code == KeyEvent.VK_S){
                gl.ui.commandNum++;
                gl.sound.playSE(7);
                if (gl.ui.commandNum > 1){ gl.ui.commandNum = 0;}
            }
            if(code == KeyEvent.VK_ENTER){
                if(gl.ui.commandNum == 0){ //play game
                    game.resetGame();
                }
                else if(gl.ui.commandNum == 1){ //quit
                    gl.stopGameThread();
                    System.exit(0);
                }
            }
        }
        //PLAY STATE
        if (game.getGameState()== GameState.RUNNING){
            if (code == KeyEvent.VK_W){
                upPressed = true;
            }
            if (code == KeyEvent.VK_S){
                downPressed = true;
            }
            if (code == KeyEvent.VK_A){
                leftPressed = true;
            }
            if (code == KeyEvent.VK_D){
                rightPressed = true;
            }
            if (code == KeyEvent.VK_ESCAPE) {
                if (game.getGameState() == GameState.RUNNING) {
                    game.setGameState(GameState.PAUSE);
                }
            }
        }
        //PAUSE STATE
        if (game.getGameState() == GameState.PAUSE){
            if (code == KeyEvent.VK_W){
                gl.ui.commandNum--;
                gl.sound.playSE(7);
                if (gl.ui.commandNum < 0){ gl.ui.commandNum = 1;}
            }
            if (code == KeyEvent.VK_S){
                gl.ui.commandNum++;
                gl.sound.playSE(7);
                if (gl.ui.commandNum > 2){ gl.ui.commandNum = 0;}
            }
            if(code == KeyEvent.VK_ENTER){
                switch (gl.ui.commandNum) {
                    case 0:
                        //resume
                        game.setGameState(GameState.RUNNING);
                        break;
                    case 1:
                        //main menu
                        game.setGameState(GameState.TITLE);
                        break;
                    case 2:
                        //quit
                        System.exit(0);
                    default:
                        break;
                }
            }
        }
        // WIN and LOSE STATE
        if (game.getGameState() == GameState.WIN || game.getGameState() == GameState.LOSE ){
            if (code == KeyEvent.VK_W){
                gl.ui.commandNum--;
                gl.sound.playSE(7);
                if (gl.ui.commandNum < 0){ gl.ui.commandNum = 1;}
            }
            if (code == KeyEvent.VK_S){
                gl.ui.commandNum++;
                gl.sound.playSE(7);
                if (gl.ui.commandNum > 1){ gl.ui.commandNum = 0;}
            }
            if(code == KeyEvent.VK_ENTER){
                if(gl.ui.commandNum == 0){ //main menu
                    game.setGameState(GameState.TITLE);
                }
                else if(gl.ui.commandNum == 1){ //quit
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Clear corresponding directional flag when key is released.
     * @param e key event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W){
            upPressed = false;
        }
        if (code == KeyEvent.VK_S){
            downPressed = false;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = false;
        }
    }

    /**
     * TEST HOOK to return ui state
     * @return gl.ui.commandNum (cursor position)
     */
    public int _test_getGameLoopUICommandNum() {
        return gl.ui.commandNum;
    }

    /**
     * TEST HOOK to return boolean upPressed
     * @return upPressed
     */
    public boolean _test_isUpPressed() {
        return upPressed;
    }

    /**
     * TEST HOOK to return boolean downPressed
     * @return downPressed
     */
    public boolean _test_isDownPressed() {
        return downPressed;
    }

    /**
     * TEST HOOK to return boolean leftPressed
     * @return leftPressed
     */
    public boolean _test_isLeftPressed() {
        return leftPressed;
    }

    /**
     * TEST HOOK to return boolean rightPressed
     * @return rightPressed
     */
    public boolean _test_isRightPressed() {
        return rightPressed;
    }
}
