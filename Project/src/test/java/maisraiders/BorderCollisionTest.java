package maisraiders;

import maisraiders.entities.Farmer;
import maisraiders.enums.Direction;
import maisraiders.panel.Board;
import maisraiders.util.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * INTEGRATION TESTS for border collision detection
 * 
 * These are integration tests because they test the interaction between multiple components:
 *  Board: Provides the map layout and neighbor cells
 * Farmer (MovingEntity): Handles movement logic and pixel positioning
 * Cell: Defines which tiles are blocked
 * Direction: Movement direction enum
 * 
 * Integration aspects tested:
 * 1. Board.getNeighbours() correctly identifies adjacent cells
 * 2. MovingEntity.move() respects Cell.isBlocked() from neighbors
 * 3. Collision detection works across tile boundaries
 * 4. Pixel and grid position synchronization during collision
 */

public class BorderCollisionTest {
    
    private Board board;
    private Farmer farmer;
    private static final int TILE_SIZE = 64;
    
    @BeforeEach
    void setUp() {
        board = new Board(20, 16);
        farmer = new Farmer(new Point(9, 7), 4);
    }
    
  
    
    @Test
    void testCannotMoveIntoTopBorder() {
        farmer = new Farmer(new Point(10, 1), 4);
        
        for (int i = 0; i < 20; i++) {
            farmer.move(Direction.N, board.getNeighbours(farmer.getPosition()));
        }
        
        assertTrue(farmer.getPosition().getRow() >= 1, "Should be blocked by top border");
        // Allow farmer to move within row 1 but not into row 0
        assertTrue(farmer.getSubPositionY() >= TILE_SIZE / 2, "Should not move far into border row");
    }
    
    @Test
    void testCannotMoveIntoBottomBorder() {
        farmer = new Farmer(new Point(10, 14), 4);
        
        for (int i = 0; i < 20; i++) {
            farmer.move(Direction.S, board.getNeighbours(farmer.getPosition()));
        }
        
        assertTrue(farmer.getPosition().getRow() <= 14, "Should be blocked by bottom border");
        assertTrue(farmer.getSubPositionY() <= 15 * TILE_SIZE, "Pixel position blocked at border");
    }
    
    @Test
    void testCannotMoveIntoLeftBorder() {
        farmer = new Farmer(new Point(1, 7), 4);
        
        for (int i = 0; i < 20; i++) {
            farmer.move(Direction.W, board.getNeighbours(farmer.getPosition()));
        }
        
        assertTrue(farmer.getPosition().getCol() >= 1, "Should be blocked by left border");
        assertTrue(farmer.getSubPositionX() >= TILE_SIZE, "Pixel position blocked at border");
    }
    
    @Test
    void testCannotMoveIntoRightBorder() {
        farmer = new Farmer(new Point(18, 7), 4);
        
        for (int i = 0; i < 20; i++) {
            farmer.move(Direction.E, board.getNeighbours(farmer.getPosition()));
        }
        
        assertTrue(farmer.getPosition().getCol() <= 18, "Should be blocked by right border");
        assertTrue(farmer.getSubPositionX() <= 19 * TILE_SIZE, "Pixel position blocked at border");
    }
    
    // Diagonal Border Tests (INTEGRATION) 
    
    @Test
    void testCannotMovePastTopLeftCorner() {
        farmer = new Farmer(new Point(1, 1), 4);
        
        for (int i = 0; i < 20; i++) {
            farmer.move(Direction.NW, board.getNeighbours(farmer.getPosition()));
        }
        
        assertTrue(farmer.getPosition().getCol() >= 1 && farmer.getPosition().getRow() >= 1,
            "Should be blocked at corner");
    }
    
    @Test
    void testCannotMovePastTopRightCorner() {
        farmer = new Farmer(new Point(18, 1), 4);
        
        for (int i = 0; i < 20; i++) {
            farmer.move(Direction.NE, board.getNeighbours(farmer.getPosition()));
        }
        
        assertTrue(farmer.getPosition().getCol() <= 18 && farmer.getPosition().getRow() >= 1,
            "Should be blocked at corner");
    }
    
    @Test
    void testCannotMovePastBottomLeftCorner() {
        farmer = new Farmer(new Point(1, 14), 4);
        
        for (int i = 0; i < 20; i++) {
            farmer.move(Direction.SW, board.getNeighbours(farmer.getPosition()));
        }
        
        assertTrue(farmer.getPosition().getCol() >= 1 && farmer.getPosition().getRow() <= 14,
            "Should be blocked at corner");
    }
    
    @Test
    void testCannotMovePastBottomRightCorner() {
        farmer = new Farmer(new Point(18, 14), 4);
        
        for (int i = 0; i < 20; i++) {
            farmer.move(Direction.SE, board.getNeighbours(farmer.getPosition()));
        }
        
        assertTrue(farmer.getPosition().getCol() <= 18 && farmer.getPosition().getRow() <= 14,
            "Should be blocked at corner");
    }
    
    // Interior Barrier Tests (INTEGRATION) 
    
    @Test
    void testCannotMoveThroughInteriorBarrier() {
        farmer = new Farmer(new Point(1, 2), 4);
        
        for (int i = 0; i < 20; i++) {
            farmer.move(Direction.E, board.getNeighbours(farmer.getPosition()));
        }
        
        assertTrue(farmer.getPosition().getCol() <= 2, 
            "Should not move past barrier at column 2");
    }
    
    @Test
    void testHayBaleBlocksMovement() {
        farmer = new Farmer(new Point(4, 14), 4);
        
        for (int i = 0; i < 20; i++) {
            farmer.move(Direction.E, board.getNeighbours(farmer.getPosition()));
        }
        
        assertTrue(farmer.getPosition().getCol() <= 5,
            "Should be blocked by hay bale");
    }
    
    @Test
    void testTractorBlocksMovement() {
        farmer = new Farmer(new Point(17, 12), 4);
        
        for (int i = 0; i < 20; i++) {
            farmer.move(Direction.E, board.getNeighbours(farmer.getPosition()));
        }
        
        assertTrue(farmer.getPosition().getCol() <= 18,
            "Should be blocked by tractor");
    }
    
 
   
    
    // Collision State Tests (INTEGRATION) 
    
    @Test
    void testBorderEventuallyStopsMovement() {
        farmer = new Farmer(new Point(3, 7), 4);
        
        // Move west many times
        for (int i = 0; i < 50; i++) {
            farmer.move(Direction.W, board.getNeighbours(farmer.getPosition()));
        }
        
        // Should have hit the left border (column 1)
        assertTrue(farmer.getPosition().getCol() <= 2,
            "Should be stopped at or near left border after many movements");
        
        // Pixel position should be at or near left boundary
        assertTrue(farmer.getSubPositionX() <= 2 * TILE_SIZE + TILE_SIZE / 2,
            "Should not move far past starting column");
    }
    
    @Test
    void testMultipleDirectionalMovements() {
        farmer = new Farmer(new Point(10, 10), 4);
        Point startPos = farmer.getPosition();
        
        for (int i = 0; i < 5; i++) farmer.move(Direction.N, board.getNeighbours(farmer.getPosition()));
        for (int i = 0; i < 5; i++) farmer.move(Direction.E, board.getNeighbours(farmer.getPosition()));
        for (int i = 0; i < 5; i++) farmer.move(Direction.S, board.getNeighbours(farmer.getPosition()));
        for (int i = 0; i < 5; i++) farmer.move(Direction.W, board.getNeighbours(farmer.getPosition()));
        
        int colDiff = Math.abs(farmer.getPosition().getCol() - startPos.getCol());
        int rowDiff = Math.abs(farmer.getPosition().getRow() - startPos.getRow());
        
        assertTrue(colDiff <= 1 && rowDiff <= 1,
            "Should return close to start after square movement");
    }
}