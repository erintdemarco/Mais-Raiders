package maisraiders;

import java.util.ArrayList;
import java.util.List;

import maisraiders.entities.AStar;
import maisraiders.entities.Alien;
import maisraiders.entities.Farmer;
import maisraiders.panel.Board;
import maisraiders.util.Point;

/**
 * Lightweight bundle of objects for tests/dev: Board, Farmer, Aliens.
 * Construct with SampleGame.simple() or provide your own Board.
 */
public class SampleGame {

    public final Board board;
    public final Farmer farmer;
    public final List<Alien> aliens = new ArrayList<>();

    /**
     * SampleGame constructor.
     * @param board The board instance
     * @param farmer The farmer instance
     * @param aliens The list of alien instances
     */
    public SampleGame(Board board, Farmer farmer, List<Alien> aliens) {
        this.board = board;
        this.farmer = farmer;
        if (aliens != null) this.aliens.addAll(aliens);
    }

    /**
     * Factory that creates a default test fixture similar to our game layout
     * @return A SampleGame for testing
     */
    public static SampleGame simple() {
        Board b = SampleMap.createDefault();
        Farmer f = new Farmer(new Point(9, 7), 4);
        Alien a1 = new Alien(new Point(1, 1), 3);
        Alien a2 = new Alien(new Point(1, 1), 3, AStar.manhattanHeuristic);
        List<Alien> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);
        return new SampleGame(b, f, list);
    }


    /**
     * Helper Method: Create alien instance at specified grid tile.
     * @param col The column for the alien
     * @param row The row for the alien
     * @param speed The alien's speed
     * @return The alien
     */
    public Alien addAlienAt(int col, int row, int speed) {
        Alien a = new Alien(new Point(col, row), speed);
        aliens.add(a);
        return a;
    }


    /**
     * Reset positions of all entites within SampleGame back to defaults.
     */
    public void resetPositions() {
        farmer.setPosition(new Point(9, 7));
        for (int i = 0; i < aliens.size(); i++) {
            Alien a = aliens.get(i);
            a.setPosition(new Point(1 + i, 1));
        }
    }
}