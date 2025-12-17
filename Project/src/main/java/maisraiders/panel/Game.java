package maisraiders.panel;

import maisraiders.entities.Farmer;
import maisraiders.enums.GameState;
import maisraiders.ui.Ui;


public class Game {
    private GameState state;
    private int score = 0;
    private long tick;
    private final int requiredRegular = 8;
    private int collectedRegular;
    private boolean exitMessageShown = false;
    public String losseMsg = " ";
    public GameLoop gl;
    public void StartGame() {

        GameLoop gameLoop = new GameLoop();
        this.gl = gameLoop;
        gameLoop.setupGame(this);
        gameLoop.startGameThread();
    }

    // GameState Methods
    public GameState getGameState() {
        return state;
    }

    public void setGameState(GameState newState) {
        state = newState;
    }

    // Score Methods
    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }
    
    public void subtractScore(int subScore){ 
        this.score -= subScore;
    }
    
    public void addScore(int addScore) {
        this.score += addScore;
    }

    public void resetScore() {this.score = 0;}

    public boolean isExitMessageShown() {return this.exitMessageShown;}
    // Tick Methods
    public long getTick() {
        return tick;
    }

    public void setTick(long newTick) {
        this.tick = newTick;
    }

    public void tickGame() {
        this.tick++;
    }

    // Corn collection methods
    public int getRequiredRegular() {
        return requiredRegular;
    }
    
    public int getCollectedRegular() { 
        return collectedRegular;
    }
    
    public void addCollectedRegular() { 
        this.collectedRegular += 1;
    }
    public void resetGame(){
    this.collectedRegular = 0;
    this.score = 0;
    this.exitMessageShown = false;  
    gl.ui.resetPlayTime();          
    gl.ui.commandNum = 0;            
    setGameState(GameState.RUNNING);
    gl.resetGameThread();
}
    /**
     * update the game screen based on game states
     * @param farmer
     * @param ui
     */
    public void updateGameState(Farmer farmer, Ui ui){
        // Don't update if already won or lost
        if (state == GameState.WIN || state == GameState.LOSE) {
            return;
        }
        
        // Check if all corn is collected and show exit message
        if (collectedRegular >= requiredRegular && !exitMessageShown) {
            //ui.showMessage("All corn collected! Find the EXIT!");
            if (ui != null) ui.showMessage("All corn collected! Find the EXIT!");
            exitMessageShown = true;
        }

        // Win condition: collect all corn AND reach the exit
        if (farmer.getExitStatus() && collectedRegular >= requiredRegular) {
            setGameState(GameState.WIN);
            return;
        }else if(farmer.getExitStatus() && collectedRegular < requiredRegular) {
            //ui.showMessage("You must collect all the corn before exiting!");
            if (ui != null) ui.showMessage("You must collect all the corn before exiting!");
        }
        
        // Lose condition: score below 0
        if (score < 0) {
            losseMsg = "You slipped too many times and lost all your corn :(";
            setGameState(GameState.LOSE);
            return;
        }
        
        // Lose condition: alien collision
        if (farmer.hasAlienCollision()) {
            if (!farmer.pitchforkStatus()) {
                // Farmer has no pitchfork, end game
                losseMsg = "You got caught by the aliens :(";
                setGameState(GameState.LOSE);
            } else {
                // Farmer 'kills' alien with pitchfork, remove pitchfork
                setGameState(GameState.RUNNING);

                //ui.showMessage("Used pitchfork to escape alien!");
                if (ui != null) ui.showMessage("Used pitchfork to escape alien!");
                if (gl != null){
                    gl.alien1.paralyze(250);  // 0.5 seconds freeze
                    gl.alien2.paralyze(250);
                }

                farmer.removePitchfork();
            }
        }
    }

}