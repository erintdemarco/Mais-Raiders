package maisraiders;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import maisraiders.entities.AStar;
import maisraiders.entities.Alien;
import maisraiders.entities.Farmer;
import maisraiders.enums.Direction;
import maisraiders.util.Point;

public class AlienTest {
    // SampleMap map;
    Alien alien1;
    Alien alien2;
    Farmer farmer;
    SampleGame game;

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        // map = new SampleMap(); // use the real board/map loader
        game = SampleGame.simple();
        farmer = game.farmer;
        if (!game.aliens.isEmpty()) alien1 = game.aliens.get(0);
        if (game.aliens.size() > 1)    alien2 = game.aliens.get(1);
    }

    /**
     * Testing Octile Alien Heuristic (Alien #1).
     */
    @Test
    void testAlienOctileHeuristic() {
        Point alienPos = new Point(1, 1);
        Point farmerPos = new Point(9, 7);

        double raw = AStar.octileHeuristic.heuristic(alienPos.getCol(), alienPos.getRow(), farmerPos.getCol(), farmerPos.getRow());
        double actual = BigDecimal.valueOf(raw)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();

        double expected = 10.485;

        assertEquals(expected, actual , 1e-3, "octile heuristic should match expected formula");
    }

    /**
     * Testing Manhattan Alien Heuristic (Alien #2).
     */
    @Test
    void testAlienManhattanHeuristic() {
        Point alienPos = new Point(1, 1);
        Point farmerPos = new Point(9, 7);

        double raw = AStar.manhattanHeuristic.heuristic(alienPos.getCol(), alienPos.getRow(), farmerPos.getCol(), farmerPos.getRow());
        double expected = 14.0;

        assertEquals(expected, raw , 0, "manhattan heuristic should match expected formula");
    }

    /**
     * Testing Alien1's next position.
     */
    @Test
    void octileAStarMovement(){
        Point expectedNext = new Point(2, 1);
        List<Point> path = AStar.findPath(game.board, new Point(1,1), new Point(9,7), AStar.octileHeuristic);
        assertEquals(expectedNext, path.get(1), "A* with octile heuristic should attempt to move East from current position");
    }

    /**
     * Testing Alien2's next position.
     */
    @Test
    void manhattanAStarMovement(){
        Point expectedNext = new Point(14, 5);
        List<Point> path = AStar.findPath(game.board, new Point(15,5), new Point(9,7), AStar.octileHeuristic);
        assertEquals(expectedNext, path.get(1), "A* with manhattan heuristic should attempt to move West from current position");
    }

    /**
     * Testing Alien1's movement.
     */
    @Test
    void testAlien1PositionChange() {
        Point alienPos = new Point(1, 1);
        Point farmerPos = new Point(9, 7);
        alien1.setPosition(alienPos);
        farmer.setPosition(farmerPos);

        int i = 0;
        while(i < 35){
            alien1.update(game.board, farmer);
            i++;
        }

        Point newAlienPos = alien1.getPosition();

        assertNotEquals(alienPos, newAlienPos, "After multiple updates our alien should correctly move in its intended direction (East)");
    }

    /**
     * Testing Alien2's movement.
     */
    @Test
    void testAlien2PositionChange() {
        Point alienPos = new Point(15, 5);
        Point farmerPos = new Point(9, 7);
        alien2.setPosition(alienPos);
        farmer.setPosition(farmerPos);

        int i = 0;
        while(i < 35){
            alien2.update(game.board, farmer);
            i++;
        }

        Point newAlienPos = alien2.getPosition();

        assertNotEquals(alienPos, newAlienPos, "After multiple updates our alien should correctly move in its intended direction (West)");
    }

    /**
     * Testing Alien1's collision detection.
     */
    @Test
    void testAlien1CatchesFarmer() {
        Point alienPos = new Point(1, 1);
        Point farmerPos = new Point(2, 1);
        alien1.setPosition(alienPos);
        farmer.setPosition(farmerPos);
        
        int i = 0;

        while(i < 35){
            alien1.update(game.board, farmer);
            i++;
        }

        boolean collided = farmer.getPosition().equals(alien1.getPosition());
        assertTrue(collided);
    }

    /**
     * Testing Alien2's collision detection.
     */
    @Test
    void testAlien2CatchesFarmer() {
        Point alienPos = new Point(1, 1);
        Point farmerPos = new Point(2, 1);
        alien2.setPosition(alienPos);
        farmer.setPosition(farmerPos);
        
        int i = 0;

        while(i < 35){
            alien2.update(game.board, farmer);
            i++;
        }

        boolean collided = farmer.getPosition().equals(alien2.getPosition());
        assertTrue(collided);
    }

    /**
     * Testing Alien2's Diagonal movement 
     * @throws Exception If test setup or reflective access fails
     */
    @Test
    void testAlien1DiagonalStepChangesBothAxes() throws Exception {
        Alien a = game.aliens.get(0);
        Farmer f = game.farmer;
        a.setPosition(new Point(1,1));
        f.setPosition(new Point(2,2)); 
        int beforeX = a.getSubPositionX();
        int beforeY = a.getSubPositionY();

        a.update(game.board, f);
        int afterX = a.getSubPositionX();
        int afterY = a.getSubPositionY();

        int dx = Math.abs(afterX - beforeX);
        int dy = Math.abs(afterY - beforeY);
        int expectedDiag = (int) Math.round(3.0 / Math.sqrt(2.0)); // alien 

        assertTrue(dx == expectedDiag && dy == expectedDiag, "Diagonal update should move both axes by moveSpeedDiagonal");
    }

    /**
     * Testing Alien2's Diagonal movement 
     * @throws Exception If test setup or reflective access fails
     */
    @Test
    void testAlien2DiagonalStepChangesBothAxes() throws Exception {
        Alien a = game.aliens.get(1);
        Farmer f = game.farmer;
        a.setPosition(new Point(1,1));
        f.setPosition(new Point(2,2)); 
        int beforeX = a.getSubPositionX();
        int beforeY = a.getSubPositionY();

        a.update(game.board, f);
        int afterX = a.getSubPositionX();
        int afterY = a.getSubPositionY();

        int dx = Math.abs(afterX - beforeX);
        int dy = Math.abs(afterY - beforeY);
        int expectedDiag = (int) Math.round(3.0 / Math.sqrt(2.0)); // alien 

        assertTrue(dx == expectedDiag && dy == expectedDiag, "Diagonal update should move both axes by moveSpeedDiagonal");
    }

    /**
     * Testing Alien1's greedy pathfinding fallback
     * @throws Exception If test setup or reflective access fails
     */
    @Test
    void testAlien1FallbackGreedy() throws Exception {
        Alien a = game.aliens.get(0);
        Farmer f = game.farmer;
        a.setPosition(new Point(1,1));
        f.setPosition(new Point(2,1)); 

        Field pathField = Alien.class.getDeclaredField("path");
        pathField.setAccessible(true);
        pathField.set(a, null);

        a.update(game.board, f);

        assertEquals(Direction.E, a.directionEnum, "Fallback greedy branch should set directionEnum to E when farmer is east");
    }

    /**
     * Testing Alien2's greedy pathfinding fallback
     * @throws Exception If test setup or reflective access fails
     */
    @Test
    void testAlien2FallbackGreedy() throws Exception {
        Alien a = game.aliens.get(1);
        Farmer f = game.farmer;
        a.setPosition(new Point(1,1));
        f.setPosition(new Point(2,1)); 

        Field pathField = Alien.class.getDeclaredField("path");
        pathField.setAccessible(true);
        pathField.set(a, null);

        a.update(game.board, f);

        assertEquals(Direction.E, a.directionEnum, "Fallback greedy branch should set directionEnum to E when farmer is east");
    }
}

