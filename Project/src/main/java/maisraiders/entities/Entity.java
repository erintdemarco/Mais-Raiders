package maisraiders.entities;

import java.awt.image.BufferedImage;

import maisraiders.enums.Direction;
import maisraiders.util.Point;

/**
 * Base game entity (player and enemy)
 */
public abstract class Entity {
    private Point position;
    private Point defaultPosition;
    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, right1, right2, right3, right4, left1, left2, left3, left4;
    public String direction;
    public Direction directionEnum;
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // default constructor for testing purposes
    public Entity() {}
    /**
     * Entity constructor.
     * @param startPosition initial grid position
     */
    public Entity(Point startPosition){
        this.position = startPosition;
        this.defaultPosition = startPosition;
    }

    /**
     * Return entity's current grid position.
     * @return current position
     */
    public Point getPosition(){
        return position;
    }

    /**
     * Set entity to a new grid position.
     * @param newPosition new position to set
     */
    public void setPosition(Point newPosition){
        this.position = newPosition;
    }
    /**
     * reset the entities position to default
     */
    public void resetPosition(){
        this.position = defaultPosition;
    }
}