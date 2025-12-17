package maisraiders.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import maisraiders.panel.Board;
import maisraiders.util.Point;

public class Farmer extends MovingEntity {
    private boolean hasPitchfork = false;
    private boolean alienCollision = false;
    private boolean exitReached = false;
    public int pitchforkCounter = 0;
    // Default constructor for testing purposes
    public Farmer() {
        super();
    }
    /**
     * Farmer constructor.
     * @param startPosition Farmer starting grid position
     * @param startMoveSpeed Farmer movement speed
     */
    public Farmer(Point startPosition, int startMoveSpeed) {
        super(startPosition, startMoveSpeed);
        getFarmerImage();
        this.direction = "down";
    }
    /**
     * resets the farmer's boolean variables
     */
    public void resetFarmerBooleans() {
        updateAlienCollision(false);
        updatePitchfork(false);
        updateExitStatus(false);
    }
    /**
     * Set if farmer has pitchfork.
     * @param bool true if farmer has pitchfork
     */
    public void updatePitchfork(boolean bool) {this.hasPitchfork = bool;}

    /**
     * Check if farmer has pitchfork.
     * @return true if farmer has pitchfork
     */
    public boolean pitchforkStatus() {return hasPitchfork;}

    /**
     * Check if farmer has collided with alien.
     * @return true after collision with alien
     */
    public boolean hasAlienCollision() {return this.alienCollision;}
    
    /**
     * Record that farmer has collided with alien.
     */
    public void updateAlienCollision(boolean bool) {this.alienCollision = bool;}

    /**
     * Record that farmer has reached exit.
     */
    public void updateExitStatus(boolean bool){this.exitReached = bool;}

    /**
     * Check if farmer has reached exit.
     * @return true if farmer has reached exit
     */
    public boolean getExitStatus(){return exitReached;}
    /**
     * removes the pitchfork from the farmer after alien catches farmer
     */
    public void removePitchfork(){
        if (pitchforkCounter > 30){
            updatePitchfork(false);
            pitchforkCounter = 0;
        }else{
            pitchforkCounter ++;
        }
    }
    /**
     * Load farmer sprite image(s).
     */
    public final void getFarmerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-bw1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-bw2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-bw3.png"));
            up4 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-bw4.png"));

            down1 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-fw1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-fw2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-fw3.png"));
            down4 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-fw4.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-rw1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-rw2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-rw3.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-rw4.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-lw1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-lw2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-lw3.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/sprites/farmer/farmer-lw4.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Update farmer animation and check for collisions.
     * @param alien1 the alien to check collision for
     * @param gameBoard the game board for exit detection
     */
    public void update(List<Alien> aliens, Board gameBoard) {
        // Sprite animation, sprite counter is updated in player movement
        if(spriteCounter > 10) { // hold each sprite animation frame
            switch (spriteNum) {
                case 1 -> spriteNum = 2;
                case 2 -> spriteNum = 3;
                case 3 -> spriteNum = 4;
                case 4 -> spriteNum = 1;
                default -> {
                }
            }
            spriteCounter = 0;
        }
        
        // Check collision with alien
        int farmerX = getSubPositionX();
        int farmerY = getSubPositionY();

        boolean collided = false;
        if (aliens != null) {
            for (Alien a : aliens) {
                if (a == null) continue;
                if (this.getPosition().equals(a.getPosition())) {
                    collided = true;
                    break;
                }
                int absDifX = Math.abs(farmerX - a.getSubPositionX());
                int absDifY = Math.abs(farmerY - a.getSubPositionY());
                if (absDifX <= 20 && absDifY <= 20) {
                    collided = true;
                    break;
                }
            }
        }
        updateAlienCollision(collided);
        
        // Check if farmer reached the exit - use pixel-based collision
        // Exit is at grid position (18, 1), convert to pixels
        int exitCenterX = 18 * 64 + 32;  // col * tileSize + half tile
        int exitCenterY = 2 * 64 + 32;   // row * tileSize + half tile
        
        int exitDifX = Math.abs(farmerX - exitCenterX);
        int exitDifY = Math.abs(farmerY - exitCenterY);
        
        // If farmer is within 32 pixels (half tile) of exit center
        if(exitDifX <= 32 && exitDifY <= 32) {
            updateExitStatus(true);
        }else{
            updateExitStatus(false);
        }
    }

    /**
     * Draw farmer sprite centred on pixel position
     * @param g2 Image renderer
     * @param size drawing size
     */
    public void draw(Graphics2D g2, int size) {
        BufferedImage image = null;

        switch(direction) {
            case "up" -> {
            switch (spriteNum) {
                case 1 -> image = up1;
                case 2 -> image = up2;
                case 3 -> image = up3;
                case 4 -> image = up4;
                default -> {
                }
            }
            }
            case "down" -> {
            switch (spriteNum) {
                case 1 -> image = down1;
                case 2 -> image = down2;
                case 3 -> image = down3;
                case 4 -> image = down4;
                default -> {
                }
            }
            }
            case "right" -> {
            switch (spriteNum) {
                case 1 -> image = right1;
                case 2 -> image = right2;
                case 3 -> image = right3;
                case 4 -> image = right4;
                default -> {
                }
            }
            }
            case "left" -> {
            switch (spriteNum) {
                case 1 -> image = left1;
                case 2 -> image = left2;
                case 3 -> image = left3;
                case 4 -> image = left4;
                default -> {
                }
            }
            }
        }
        // Draw centered: subtract half the size from subPosition to center the sprite
        int drawX = getSubPositionX() - (size / 2);
        int drawY = getSubPositionY() - (size / 2);
        g2.drawImage(image, drawX, drawY, size, size, null);
    }
}