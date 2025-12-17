package maisraiders;

import maisraiders.util.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import maisraiders.entities.Farmer;
import maisraiders.enums.Direction;
import maisraiders.panel.Game;
import maisraiders.panel.GameLoop;
import maisraiders.panel.Board;
 
/**
 *
 * @author MackAndSneeze
 */
public class Test_Grid_Pixel_Conversion {
    Game game = new Game();
    GameLoop gl = new GameLoop();
    Farmer farmer = new Farmer(new Point(9, 7), 4);
    Board board = new Board(20, 16);

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        // map = new SampleMap(); // use the real board/map loader
        // game.StartGame();
        //
    }

    @Test
    void testGridToPixel() {
        // game.StartGame();
        //

        farmer.setPosition(new Point(9, 7));
        int subPositionX = farmer.getSubPositionX();
        int subPositionY = farmer.getSubPositionY();

        // Grid (9,7) should convert to the middle of a 64x64 tile, i.e., (5*64 + 32, 7*64 + 32) = (608, 480)
        assertEquals(608, subPositionX);
        assertEquals(480, subPositionY);
    }

    @Test
    void testPixelToGridMiddle() {
        // game.StartGame();
        //

        // Set sub-position to (608, 480) which is the middle of tile (9,7)
        farmer._test_setPosition(new Point(8, 6));
        int subPositionX = farmer.getSubPositionX();
        int subPositionY = farmer.getSubPositionY();

        // Convert back to grid position
        int expectedCol = (subPositionX) / 64;
        int expectedRow = (subPositionY + 16) / 64;

        // Should map back to (5,7)
        assertEquals(expectedCol, farmer.getPosition().getCol());
        assertEquals(expectedRow, farmer.getPosition().getRow());
    }

    @Test
    void testPixelToGridEdge() {
        // game.StartGame();
        //

        // Set sub-position to (512, 384) which is the top left corner of tile (8,6), by moving farmer there
        farmer._test_setPosition(new Point(8, 6));

        // move up eight and left four to reach (512, "384") - hitbox
        for (int i = 0; i < 12 ; i++) {
            farmer.move(Direction.N, board.getNeighbours(farmer.getPosition()));
        }

        for (int i = 0; i < 8 ; i++) {
            farmer.move(Direction.W, board.getNeighbours(farmer.getPosition()));
        }

        int subPositionX = farmer.getSubPositionX(); // should be 512
        int subPositionY = farmer.getSubPositionY(); // should be 384
        // Print to verify
        System.out.println("SubPositionX: " + subPositionX + ", SubPositionY: " + (subPositionY + 16));

        // Convert back to grid position
        int expectedCol = (subPositionX) / 64;
        int expectedRow = (subPositionY + 16) / 64; // account for 16 pixel hitbox offset

        // Should map back to (5,7)
        assertEquals(expectedCol, farmer.getPosition().getCol());
        assertEquals(expectedRow, farmer.getPosition().getRow());
    }

    @Test
    void testPixelToGridOutside() {
        // game.StartGame();
        //

        // Set sub-position to (576, 448) which is the bottom-right corner of tile (8,6), by moving farmer there
        farmer._test_setPosition(new Point(8, 6));

        // move down 12 and right 8 to reach (576, "448") - hitbox
        for (int i = 0; i < 4; i++) {
            farmer.move(Direction.S, board.getNeighbours(farmer.getPosition()));
        }
        for (int i = 0; i < 8; i++) {
            farmer.move(Direction.E, board.getNeighbours(farmer.getPosition()));
        }

        int subPositionX = farmer.getSubPositionX(); // should be 576
        int subPositionY = farmer.getSubPositionY(); // should be 448
        // Print to verify
        System.out.println("SubPositionX: " + subPositionX + ", SubPositionY: " + (subPositionY + 16));

        // Convert back to grid position (should still be (8,6), the cell is biased towards top-left)
        int expectedCol = (subPositionX) / 64 - 1;
        int expectedRow = (subPositionY + 48) / 64 - 1; // account for 16 pixel hitbox offset

        // Should map back to (5,7)
        assertEquals(expectedCol, farmer.getPosition().getCol());
        assertEquals(expectedRow, farmer.getPosition().getRow());
    }
        


}