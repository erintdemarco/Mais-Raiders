package maisraiders.panel;


import java.util.ArrayList;
import java.util.List;

import maisraiders.entities.AStar;
import maisraiders.entities.Alien;
import maisraiders.entities.Farmer;
import maisraiders.enums.GameState;
import maisraiders.movement.KeyHandler;
import maisraiders.movement.PlayerMovementUpdater;
import maisraiders.object.AssetSetter;
import maisraiders.object.GameObject;
import maisraiders.sound.Sound;
import maisraiders.ui.SpriteDrawer;
import maisraiders.ui.Ui;
import maisraiders.ui.WindowSetting;
import maisraiders.util.Point;


/**
 * Implements runnable to run game logic
 */
public class GameLoop implements Runnable {
    Thread gameThread;
    GameWindow gameWindow;
    public boolean isRunning;
    SpriteDrawer sprite;
    public int tileSize = 64;
    int FPS;
    PlayerMovementUpdater playerMover;
    public Farmer gameFarmer;
    public Alien alien1;
    public Alien alien2;
    List<Alien> aliens;
    Board gameBoard;
    Game game;
    KeyHandler playerInput;
    public GameObject objects = new GameObject();
    public AssetSetter aSetter = new AssetSetter(this);
    public Ui ui;
    public Sound sound = new Sound();
    public boolean soundPlaying = false;
    private long lastCollisionTime = 0;
    private static final long COLLISION_COOLDOWN = 1000; // sound delay
    public int screenWidth = 1280;
    public void setupGame(Game game) {
        this.game = game;
        ui = new Ui(this, game);
        aSetter.setObject();
    }
    
    public void startGameThread() {
        isRunning = true;
        gameWindow = new GameWindow( game, this);
        sprite = gameWindow.getSpriteDrawer();
        tileSize = gameWindow.getTileSize();
        FPS = gameWindow.getFPS();
        playerMover = new PlayerMovementUpdater();
        gameFarmer = new Farmer(new Point(9, 7), 4);
        alien1 = new Alien(new Point(1, 1), 3);
        alien2 = new Alien(new Point(15, 5), 3, AStar.manhattanHeuristic);
        aliens = new ArrayList<>();
        aliens.add(alien1);
        aliens.add(alien2);
        gameBoard = new Board(WindowSetting.maxScreenCol, WindowSetting.maxScreenRow);
        gameThread = new Thread(this);
        gameThread.start();
        sprite.setSpriteDrawer(this, game, ui);
        sprite.setBoard(gameBoard);  // Pass the board to sprite drawer
    }
    public void resetGameThread() {

        gameFarmer.resetPosition();
        gameFarmer.resetFarmerBooleans();

        alien1.resetPosition();
        alien2.resetPosition();
        aSetter.resetObjects();
        sprite.resetSprites();

    }
    public void stopGameThread() {
        sound.stopMusic();
        isRunning = false;
       gameThread = null;
    }
    @Override
    public void run() {
        double drawInterval = 1000/FPS; // 60 FPS in ms
        double nextDrawTime = System.currentTimeMillis() + drawInterval;

        while (gameThread != null) {
            while (isRunning) {
                // Two things need to be done in this loop
                // 1 UPDATE: update the information such as character positions
                update();
                // 2 DRAW: draw the screen with the updated information

                sprite.drawSprite(gameFarmer, alien1, alien2, tileSize, objects);

                try {
                    double remainingTime = nextDrawTime - System.currentTimeMillis();
                    if (remainingTime < 0) {
                        remainingTime = 0;
                    }
                    Thread.sleep((long) remainingTime);

                    nextDrawTime += drawInterval;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void update() {
        //Check the game state
        // TITLE STATE
        if(game.getGameState() == GameState.TITLE){
            playerInput = gameWindow.getKeyInput();
            if (!soundPlaying){
                sound.playMusic(0);
                soundPlaying = true;
            }
        }
        //PLAY STATE
        if (game.getGameState() == GameState.RUNNING){
           updateRunning();
        }
        // PAUSE STATE
        if(game.getGameState() == GameState.PAUSE){
            updatePause();
        }
        // WIN STATE
        if(game.getGameState() == GameState.WIN){
         updateEndState(6);
        }
        // LOSE STATE
        if(game.getGameState() == GameState.LOSE){
          updateEndState(5);
        }

    }

    /**
     * a method to update game elements during the run state
     */
    private void updateRunning() {
        playerInput = gameWindow.getKeyInput();
        playerMover.updatePlayerMovement(playerInput, gameFarmer, gameBoard);
        gameFarmer.update(aliens, gameBoard);  // Pass gameBoard to farmer update
        alien1.update(gameBoard, gameFarmer);
        alien2.update(gameBoard, gameFarmer);
        aSetter.updateObjects(gameFarmer, game, ui);
        aSetter.setDisappearingReward(ui);
        game.updateGameState(gameFarmer, ui);  // Pass ui to game state update
        if (!soundPlaying){
            sound.playMusic(0);
            soundPlaying = true;
        }
        if (gameFarmer.getBarrierCollision() || (gameFarmer.getExitStatus() && game.getCollectedRegular() < game.getRequiredRegular())) {
            long currentTime = System.currentTimeMillis();

            // Check if the cooldown period has passed
            if (currentTime - lastCollisionTime > COLLISION_COOLDOWN) {
                if(gameFarmer.getBarrierCollision()){
                    sound.playSE(2);
                }else {
                    sound.playSE(9);
                }
                lastCollisionTime = currentTime;
            }
        }
    }
    /**
     * a method to update game elements during the pause state
     */
    private void updatePause() {
        // only want to display options, player and aliens shouldn't move
        playerInput = gameWindow.getKeyInput();
        if (soundPlaying){
            sound.stopMusic();
            soundPlaying = false;
        }
    }
    /**
     * a method to update game elements during the win/ lose state
     */
    private void updateEndState(int soundNumber) {
        playerInput = gameWindow.getKeyInput();
        if (soundPlaying){
            sound.stopMusic();
            sound.playSE(soundNumber);
            soundPlaying = false;
        }
    }
    /**
     * TEST HOOK for game window
     * @return local game window
     */
    public GameWindow _test_getGameWindow() {
        return this.gameWindow;
    }

    /**
     * TEST HOOK for player input
     * @return local player input
     */
    public KeyHandler _test_getPlayerInput() {
        return this.playerInput;
    }

    /**
     * TEST HOOK for game farmer
     * @return local farmer
     */
    public Farmer _test_getGameFarmer() {
        return this.gameFarmer;
    }

    /**
     * TEST HOOK for game board
     * @return local game board
     */
    public Board _test_getGameBoard() {
        return this.gameBoard;
    }
}