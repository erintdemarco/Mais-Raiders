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
public class Corn extends GameObject {

    /**
     * Corn Constructor (load image @ specific location)
     */
    public Corn(Point point) {
        this.setPosition(point);
        this.setSubPosition(point);
        try { // get image
            image = ImageIO.read(getClass().getResourceAsStream("/objects/corn3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * checks if the player had collided with the corn object
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
            //corn = 50 pts, number of corn to collect = 7
                this.isCollected = true;
                game.addScore(50);
                game.addCollectedRegular();
                if (gl != null) {
                    gl.sound.playSE(1);
                }
                this.removeObject(this);
                ui.showMessage("you collected a corn!");
                if (game.getCollectedRegular() == game.getRequiredRegular()) {
                    ui.showMessage("Enough corn collected, find the Exit!");
                }
        }
    }
}
