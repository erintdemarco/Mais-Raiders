package maisraiders;

import maisraiders.panel.Board;

/**
 * Test Helper: Creates board instance for unit testing.
 */
public class SampleMap {

    // default map size
    public static final int DEFAULT_COLS = 20;
    public static final int DEFAULT_ROWS = 16;

    /**
     * Create a simple board with default dimensions without starting GUI or load assets. 
     * @return
     */
    public static Board createDefault() {
        return new Board(DEFAULT_COLS, DEFAULT_ROWS);
    }

    /**
     * Create a board of specified size.
     * @param cols Number of columns
     * @param rows Number of rows
     * @return a new board of specified cols x rows
     */
    public static Board create(int cols, int rows) {
        return new Board(cols, rows);
    }
}