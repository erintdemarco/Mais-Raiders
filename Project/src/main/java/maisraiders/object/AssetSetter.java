package maisraiders.object;

import java.awt.Point;
import java.util.List;
import java.util.Random;

import maisraiders.entities.Farmer;
import maisraiders.panel.Game;
import maisraiders.panel.GameLoop;
import maisraiders.ui.Ui;

/**
 * Takes in a gameLoop and sets all object asset locations and adds them to the object list
 */
public class AssetSetter {
    GameLoop gl;
    List<GameObject> objects;
    private CornBundle cornbund1;
    private CornBundle cornbund2;
    // integers for random corn spawning
    private long spawnTime = -1;
    private long removeTime = -1;
    private boolean cornSpawned = false;
    private final Random rand = new Random();

    /**
     * AssetSetter constructor.
     * @param gl GameLoop that holds the objects
     */
    public AssetSetter(GameLoop gl) {
        this.gl = gl;
    }

    /**
     * Initialize + add objects to game.
     */
    public void setObject() {
        // screen size is 16 rows x 20 cols ( x <= 20, y <= 16)
        Corn corn1 = new Corn(new Point(3*gl.tileSize,5*gl.tileSize));
        Corn corn2 = new Corn(new Point(6*gl.tileSize,1*gl.tileSize));
        Corn corn3 = new Corn(new Point(10*gl.tileSize,2*gl.tileSize));
        Corn corn4 = new Corn(new Point(8*gl.tileSize,12*gl.tileSize));
        Corn corn5 = new Corn(new Point(12*gl.tileSize,10*gl.tileSize));
        Corn corn6 = new Corn(new Point(14*gl.tileSize,4*gl.tileSize));
        Corn corn7 = new Corn(new Point(5*gl.tileSize,8*gl.tileSize));
        Corn corn8 = new Corn(new Point(2*gl.tileSize,9*gl.tileSize));
        Pitchfork pitchfork = new Pitchfork(new Point(16*gl.tileSize,12*gl.tileSize));
        MudTrap trap1 = new MudTrap(new Point(7*gl.tileSize,5*gl.tileSize));
        MudTrap trap2 = new MudTrap(new Point(4*gl.tileSize,10*gl.tileSize));
        MudTrap trap3 = new MudTrap(new Point(16*gl.tileSize,7*gl.tileSize));

        gl.objects.addObject(corn1);
        gl.objects.addObject(corn2);
        gl.objects.addObject(corn3);
        gl.objects.addObject(corn4);
        gl.objects.addObject(corn5);
        gl.objects.addObject(corn6);
        gl.objects.addObject(corn7);
        gl.objects.addObject(corn8);
        gl.objects.addObject(pitchfork);
        gl.objects.addObject(trap1);
        gl.objects.addObject(trap2);
        gl.objects.addObject(trap3);
    }
    /**
     * A method that spawns in the bundles corn that disappear
     */
    public void setDisappearingReward(Ui ui) {
        long currentTime = System.currentTimeMillis();

        // between 6 and 11 seconds of this function being called spawn corn
        if (spawnTime == -1) {
            int spawnDelay = 6 + rand.nextInt(6);
            spawnTime = currentTime + spawnDelay * 1000L; // convert to ms
        }
        // Check if it's time to spawn the corn
        if (!cornSpawned && currentTime >= spawnTime) {
            ui.showMessage("A Special Bonus Rewards has spawned!");
            cornbund1 = new CornBundle(new Point(1 * gl.tileSize, 14 * gl.tileSize));
            cornbund2 = new CornBundle(new Point(18 * gl.tileSize, 7 * gl.tileSize));
            gl.objects.addObject(cornbund1);
            gl.objects.addObject(cornbund2);
            cornSpawned = true;

            // Schedule removal
            int removeDelay = 7 + rand.nextInt(6);
            removeTime = currentTime + removeDelay * 1000L;
        }
            // Check if it's time to remove the corn
            if (cornSpawned && currentTime >= removeTime) {
                gl.objects.removeObject(cornbund1);
                gl.objects.removeObject(cornbund2);
                //cornSpawned = false;

                // Reset times so this can happen again if desired
                spawnTime = -1;
                removeTime = -1;
            }
    }

    /**
     * Update every object once every tick
     * @param farmer The player
     * @param game The game state
     * @param ui The game UI
     */
    public void updateObjects(Farmer farmer, Game game, Ui ui) {
        boolean onMud = false;
        objects = gl.objects.getInstances();
        if (objects != null) {
            for (GameObject object : objects) {
                object.updateObject(farmer, game, ui);

                if(object instanceof MudTrap && object.isTriggered){
                    onMud = true;
                }
            }
            if(!onMud){
                farmer.resetMoveSpeed();
            }
        }
    }

    /**
     * resets all game objects and spawns new ones
     */
    public void resetObjects(){
        //remove all objects on board
        objects = gl.objects.getInstances();
        if (objects != null) {
            for (GameObject current : objects) {
                current.resetObj(current);
            }
        }
        // reset bool variables
        this.spawnTime = -1;
        this.removeTime = -1;
        this.cornSpawned = false;
        //set all new objects
        setObject();
    }

}
