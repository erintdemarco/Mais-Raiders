package maisraiders.movement;

import maisraiders.entities.Farmer;
import maisraiders.enums.Direction;
import maisraiders.panel.Board;

/**
 * Takes in player keyboard input and moves player accordingly.
 */
public class PlayerMovementUpdater {
    private int moveX;
    private int moveY;

    /**
     * Reads KeyHandler flags and moves the farmer based on said flag.
     * @param keyH current keyboard flags
     * @param gameFarmer The farmer
     * @param gameBoard Game board for collision handling
     */
    public void updatePlayerMovement(KeyHandler keyH, Farmer gameFarmer, Board gameBoard){
        moveX = 0;
        moveY = 0;
        // top left corner is (0,0)
        // X values increase to the right, Y values increase downwards
        //      ^ This is brain-breaking - Will

        if(keyH.upPressed) {
            moveY--;
            gameFarmer.direction = "up";
            gameFarmer.spriteCounter++;
        }
        if(keyH.downPressed) {
            moveY++;
            gameFarmer.direction = "down";
            gameFarmer.spriteCounter++;
        }
        if(keyH.leftPressed) {
            moveX--;
            gameFarmer.direction = "left";
            gameFarmer.spriteCounter++;
        }
        if(keyH.rightPressed) {
            moveX++;
            gameFarmer.direction = "right";
            gameFarmer.spriteCounter++;
        }

        if ((moveX != 0) || (moveY != 0)) {
            if ((moveX != 0) && (moveY != 0)) {
                if (moveX + moveY > 0) { gameFarmer.move(Direction.SE, gameBoard.getNeighbours(gameFarmer.getPosition())); }
                else if (moveX + moveY < 0) { gameFarmer.move(Direction.NW, gameBoard.getNeighbours(gameFarmer.getPosition())); }
                else if (moveX > 0) { gameFarmer.move(Direction.NE, gameBoard.getNeighbours(gameFarmer.getPosition())); }
                else { gameFarmer.move(Direction.SW, gameBoard.getNeighbours(gameFarmer.getPosition())); }
            }
            else {
                if (moveY < 0) { gameFarmer.move(Direction.N, gameBoard.getNeighbours(gameFarmer.getPosition())); }
                else if (moveY > 0) { gameFarmer.move(Direction.S, gameBoard.getNeighbours(gameFarmer.getPosition())); }
                else if (moveX > 0) { gameFarmer.move(Direction.E, gameBoard.getNeighbours(gameFarmer.getPosition())); }
                else { gameFarmer.move(Direction.W, gameBoard.getNeighbours(gameFarmer.getPosition())); }
            }
        }
    }
}
