package maisraiders.entities;

import maisraiders.enums.Direction;
import maisraiders.panel.Cell;
import maisraiders.util.Point;

public abstract class MovingEntity extends Entity {
    
    private int subPositionX; // Sub-cell values (pixel position)
    private int subPositionY;
    private final int baseSpeed;
    private int moveSpeed;
    private int moveSpeedDiagonal;
    private static final int TILE_SIZE = 64;
    boolean hasBarrierCollision = false;

    /**
     * Moving entity constructor.
     * @param startPosition Entity starting grid position
     * @param startMoveSpeed Entity movement speed
     */
    public MovingEntity(Point startPosition, int startMoveSpeed) {
        super(startPosition);
        // Initialize at CENTER of tile
        this.subPositionX = (this.getPosition().getCol() * TILE_SIZE) + (TILE_SIZE / 2);
        this.subPositionY = (this.getPosition().getRow() * TILE_SIZE) + (TILE_SIZE / 2);
        this.baseSpeed = startMoveSpeed;
        this.moveSpeed = this.baseSpeed;

        // Round to int, instead of ceil
        this.moveSpeedDiagonal = (int) Math.round(this.moveSpeed / Math.sqrt(2));
    }

    public MovingEntity() { // default constuctor for testing purposes
        super();
        this.baseSpeed = 4;
    }

    /**
     * Get current pixel X position
     * @return pixel X position
     */
    public int getSubPositionX(){
        return this.subPositionX;
    }

    /**
     * Get current pixel Y position
     * @return pixel Y position
     */
    public int getSubPositionY(){
        return this.subPositionY;
    }
    // set subpositions methods: for testing purposes to 'move' farmer
    public void setSubPositionX(int subPositionX){ this.subPositionX = subPositionX * TILE_SIZE + (TILE_SIZE/2); }
    public void setSubPositionY(int subPositionY){ this.subPositionY = subPositionY * TILE_SIZE+ (TILE_SIZE/2); }
    /**
     * Get current Tile Size
     * @return tile size (in pizels)
     */
    public int getTileSize(){
        return TILE_SIZE;
    }

    /**
     * Set new movement speed/
     * @param speed new movement speed
     */
    public void setMoveSpeed(int speed) { 
        this.moveSpeed = speed;
        this.moveSpeedDiagonal = (int) Math.round(speed / Math.sqrt(2));
        }

    /**
     * Reset current movement speed to base speed.
     */
    public void resetMoveSpeed() {
        this.moveSpeed = baseSpeed;
        this.moveSpeedDiagonal = (int) Math.round(baseSpeed / Math.sqrt(2));
        }
    public int getMoveSpeed() {
        return this.moveSpeed;
    }
    /**
     * returns where there is a collision with a barrier
     */
    public boolean getBarrierCollision() { return hasBarrierCollision;}
    /**
     * Move entity in a specific direction, updating its grid cell based on its direction
     * @param direction direction to move
     * @param neighbours array of neighbouring cells
     */
    public void move(Direction direction, Cell[] neighbours){
        if (neighbours == null) return;
        
        // Apply movement
        switch (direction) {
            case N -> this.subPositionY -= this.moveSpeed;
            case NE -> {
                this.subPositionX += this.moveSpeedDiagonal;
                this.subPositionY -= this.moveSpeedDiagonal;
            }
            case E -> this.subPositionX += this.moveSpeed;
            case SE -> {
                this.subPositionX += this.moveSpeedDiagonal;
                this.subPositionY += this.moveSpeedDiagonal;
            }
            case S -> this.subPositionY += this.moveSpeed;
            case SW -> {
                this.subPositionX -= this.moveSpeedDiagonal;
                this.subPositionY += this.moveSpeedDiagonal;
            }
            case W -> this.subPositionX -= this.moveSpeed;
            case NW -> {
                this.subPositionX -= this.moveSpeedDiagonal;
                this.subPositionY -= this.moveSpeedDiagonal;
            }
            default -> {
            }
        }
        
        // Get current logical position
        int currentCol = this.getPosition().getCol();
        int currentRow = this.getPosition().getRow();
        
        // Calculate tile boundaries
        int leftBound = currentCol * TILE_SIZE;
        int rightBound = (currentCol + 1) * TILE_SIZE;
        int topBound = currentRow * TILE_SIZE - TILE_SIZE/4;
        int bottomBound = (currentRow + 1) * TILE_SIZE - TILE_SIZE/4;
        
        
        // neighbours: [0] = North, [1] = West, [2] = South, [3] = East

        // Check vertical collision (North / South)
        if (this.subPositionY < topBound) {
            // Moving North - check if we can enter next tile
            if (neighbours.length > 0 && neighbours[0] != null && neighbours[0].isBlocked()) {
                // Blocked! Snap back to boundary
                this.hasBarrierCollision = true;
                this.subPositionY = topBound;
            } else {
                // Can move - update logical position
                this.hasBarrierCollision = false;
                this.setPosition(new Point(currentCol, currentRow - 1));
            }
        }

        else if (this.subPositionY > bottomBound) {
            // Moving South - check if we can enter next tile
            if (neighbours.length > 2 && neighbours[2] != null && neighbours[2].isBlocked()) {
                // Blocked! Snap back to boundary
                this.hasBarrierCollision = true;
                this.subPositionY = bottomBound;
            } else {
                // Can move - update logical position
                this.hasBarrierCollision = false;
                this.setPosition(new Point(currentCol, currentRow + 1));
            }
        }
        
        // Check horizontal collision (West / East)
        if (this.subPositionX > rightBound) {
            // Moving East - check if we can enter next tile
            if (neighbours.length > 3 && neighbours[1] != null && neighbours[1].isBlocked()) {
                // Blocked! Snap back to boundary
                this.hasBarrierCollision = true;
                this.subPositionX = rightBound;
            } else {
                // Can move - update logical position
                this.hasBarrierCollision = false;
                this.setPosition(new Point(currentCol + 1, currentRow));
            }
        }
        else if (this.subPositionX < leftBound) {
            // Moving West - check if we can enter next tile
            if (neighbours.length > 1 && neighbours[3] != null && neighbours[3].isBlocked()) {
                // Blocked! Snap back to boundary
                this.hasBarrierCollision = true;
                this.subPositionX = leftBound;
            } else {
                // Can move - update logical position
                this.hasBarrierCollision = false;
                this.setPosition(new Point(currentCol - 1, currentRow));
            }
        }
    }
    /**
     * Reset entity position to default, including pixel positions
     */
    @Override
    public void resetPosition(){
        super.resetPosition();
        // Reset pixel positions to center of default tile
        this.subPositionX = (this.getPosition().getCol() * TILE_SIZE) + (TILE_SIZE / 2);
        this.subPositionY = (this.getPosition().getRow() * TILE_SIZE) + (TILE_SIZE / 2);
    }

    /**
     * TEST HOOK for setting position directly in tests.
     * @param newPosition new grid position
     */
    public void _test_setPosition(Point newPosition){
        super.setPosition(newPosition);
        // Update sub-position to match grid position
        this.subPositionX = newPosition.getCol() * TILE_SIZE + TILE_SIZE / 2;
        this.subPositionY = newPosition.getRow() * TILE_SIZE + TILE_SIZE / 2;
    }
        
}