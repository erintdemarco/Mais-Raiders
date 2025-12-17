package maisraiders.object;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import maisraiders.entities.Farmer;
import maisraiders.panel.Game;
import maisraiders.panel.GameLoop;
import maisraiders.ui.Ui;


/**
 * A class that contains a list of game Objects (corn, pitchfork, trap) and updates them.
 */
public class GameObject {
    public int size = 64;
    private Point position;
    private Point subPosition;
    private static final CopyOnWriteArrayList<GameObject> objectInstances = new CopyOnWriteArrayList<>();
    public BufferedImage image;
    public boolean isCollected = false;
    public boolean isTriggered = false;
    GameLoop gl;

    /**
     * Draw object at specific pixel position.
     * @param g2 Image renderer
     * @param gl The GameLoop
     */
    public void draw(Graphics2D g2, GameLoop gl){
        this.gl = gl;
        // FIXED: Draw at actual position without incorrect offsets
        // Objects are positioned in pixels already from AssetSetter
        g2.drawImage(image, position.x, position.y, gl.tileSize, gl.tileSize, null);
    }
    
    /**
     * Return list of all instances (all live objects).
     * @return list of instaces (live objects)
     */
    public List<GameObject> getInstances() {
        return objectInstances;
    }

    /**
     * Add object to game.
     * @param object Object being added
     */
    public void addObject(GameObject object) {
        objectInstances.add(object);
    }

    /**
     * Remove object from game.
     * @param object Object being removed
     */
    public void removeObject( GameObject object) {
        objectInstances.remove(object);
    }

    /**
     * Get pixel position of object (top-left pixel).
     * @return Pixel postion (x, y)
     */
    public Point getPosition(){
        return position;
    }

    /**
     * Set pixel position of object (top-left pixel).
     * @param newPosition New pixel postion (x, y)
     */
    public void setPosition(Point newPosition) {
        position = newPosition;
    }

    /**
     * Get pixel position of object (center pixel).
     * @return Center pixel postion 
     */
    public Point getSubPosition(){
        return subPosition;
    }

    /**
     * Set pixel position of object (center pixel).
     * @param point Top-left pixel position
     */
    public void setSubPosition(Point point) {
        Point coords = this.getPosition();
        int objCenterX = coords.x + 32;
        int objCenterY = coords.y + 32;
        this.subPosition = new Point(objCenterX, objCenterY);
    }

    public void resetObj(GameObject object) {
        object.isCollected = false;
        object.isTriggered = false;
        removeObject(object);
    }

    /**
     * Update object state and apply object effect onto farmer.
     * @param farmer The player
     * @param game The game state
     * @param ui The game UI
     */
    public void updateObject(Farmer farmer, Game game, Ui ui) { }
}