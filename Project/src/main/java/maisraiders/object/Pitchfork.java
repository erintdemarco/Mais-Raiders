package maisraiders.object;

import maisraiders.entities.Farmer;
import maisraiders.panel.Game;
import maisraiders.ui.Ui;

import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Pitchfork extends GameObject{

    /**
     * Pitchfork Constructor (load image @ specific location)
     */
    public Pitchfork(Point point) {
        this.setPosition(point);
        this.setSubPosition(point);
        try { // draw image
            image = ImageIO.read(getClass().getResourceAsStream("/objects/pitchfork2.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * checks if the player has collided with a pitchfork object, gives player another life after getting caught
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
        int absDifY = Math.abs(farmerY - objY);

        // if player is on game object trigger the correct action based on name
        if(absDifX <= (40) && absDifY <= (40)) {
          // set collectedPitchfork to true to give player temporary immunity
                this.isCollected = true;
                this.removeObject(this);
               if (gl != null){
                   gl.sound.playSE(8);
               }
                farmer.updatePitchfork(true);
                ui.showMessage("You collected the pitchfork!");
            }

    }
}
