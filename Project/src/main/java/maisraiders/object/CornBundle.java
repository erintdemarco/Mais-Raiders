package maisraiders.object;

import maisraiders.entities.Farmer;
import maisraiders.panel.Game;
import maisraiders.ui.Ui;

import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Collectible corn object.
 */
public class CornBundle extends GameObject {

    /**
     * Corn Constructor (load image @ specific location)
     */
    public CornBundle(Point point) {
        this.setPosition(point);
        this.setSubPosition(point);
        try { // get image
            image = ImageIO.read(getClass().getResourceAsStream("/objects/rainbowcorn.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * checks if the player had collided with the  rainbow corn object
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
        if (absDifX <= (50) && absDifY <= (50)) {
             //rainbow corn = 150 pts, 2 per map
                this.isCollected = true;
                game.addScore(150);
                if (gl != null){
                    gl.sound.playSE(1);
                }
                this.removeObject(this);
                ui.showMessage("you collected a special corn!");
            }
    }
}
