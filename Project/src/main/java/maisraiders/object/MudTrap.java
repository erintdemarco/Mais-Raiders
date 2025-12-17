package maisraiders.object;

import maisraiders.entities.Farmer;
import maisraiders.panel.Game;
import maisraiders.ui.Ui;

import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MudTrap extends GameObject{
    
    /**
     * MudTrap Constructor (load image @ specific location)
     */
    public MudTrap(Point point) {
        this.setPosition(point);
        this.setSubPosition(point);
        try { // draw image
            image = ImageIO.read(getClass().getResourceAsStream("/traps/mudtrap.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * checks if a player has collided with the mudtrap object, slows on collision.
     * @param farmer The player
     * @param game The game state
     * @param ui The game UI
     */
    @Override
    public void updateObject(Farmer farmer, Game game, Ui ui) {
        int farmerX = farmer.getSubPositionX();
        int farmerY = farmer.getSubPositionY();

        int objX = this.getSubPosition().x;
        int objY = this.getSubPosition().y;

        int absDifX = Math.abs(farmerX - objX);
        int absDifY = Math.abs(farmerY - objY) - farmer.getTileSize()/4;

        // if player is on game object trigger the correct action based on name
        if(absDifX <= (32) && absDifY <= (32)) {
              // mud trap = -25 pts: slows player while they are on top of the trap,
            if (!isTriggered){
                game.subtractScore(25);
                if (gl != null){
                    gl.sound.playSE(3);
                }
            }
            isTriggered = true;
            farmer.setMoveSpeed(2);
        } else if (isTriggered) {
            farmer.resetMoveSpeed();
            isTriggered = false;
        }
    }
}
