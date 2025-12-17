package maisraiders.panel;

import maisraiders.enums.CellFill;
import maisraiders.map.MapLoader;
import maisraiders.util.Point;

public class Board {
    private final int rows;
    private final int cols;

    private final Cell[][] grid;

    /**
     * Board constructor.
     * @param startCols # of columns
     * @param startRows # of rows
     */
    public Board(int startCols, int startRows){
        this.rows = startRows;
        this.cols = startCols;
        
        // Create a Matrix of Cells using the MapLoader
        this.grid = new Cell[this.cols][this.rows];
        
        // Get the map layout from MapLoader
        int[][] mapLayout = MapLoader.getMapLayout();

        for (int c = 0; c < cols; c++){
            for (int r = 0; r < rows; r++){
                // Get the cell fill type from the map
                CellFill fillType = MapLoader.getCellFillFromMapValue(mapLayout[c][r]);
                this.grid[c][r] = new Cell(new Point(c, r), fillType);
                
                // Optional: Print barrier locations for debugging
                /*if (fillType == CellFill.BARRIER) {
                    System.out.println("Barrier at (" + c + ", " + r + ")");
                } else if (fillType == CellFill.EXIT) {
                    System.out.println("Exit at (" + c + ", " + r + ")");
                }*/
            }
        }
    }

    /**
     * Get cell at given grid Point
     * @param position Grid Point
     * @return Cell at that postion (null if doesn't exist)
     */
    public Cell getCell(Point position){
        int c = position.getCol();
        int r = position.getRow();
        
        // Bounds checking
        if (c < 0 || c >= cols || r < 0 || r >= rows) {
            return null;
        }
        
        return this.grid[c][r];
    }

    /**
     * Get # of columns.
     * @return # of columns
     */
    public int getCols() {
        return cols;
    }
    
    /**
     * Get # of rows.
     * @return # of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get four neightbouring cells around given grid Point.
     * @param position Grid Point
     * @return Array of 4 cells:
     *          North = [0]
     *          East = [1]
     *          South = [2]
     *          West = [3]
     */
    public Cell[] getNeighbours(Point position){
        Cell[] neighbours = new Cell[4];
        if(position == null) return neighbours;

        int c = position.getCol();
        int r = position.getRow();

        neighbours[0] = getCell(new Point(c, r - 1)); // North
        neighbours[1] = getCell(new Point(c + 1, r)); // East
        neighbours[2] = getCell(new Point(c, r + 1)); // South
        neighbours[3] = getCell(new Point(c - 1, r)); // West

        return neighbours;
    }

    /**
     * Check if grid Point is outside of board.
     * @param position Grid Position
     * @return true if Point is outside board
     */
    public boolean isOutOfBounds(Point position){
        return (((position.getRow() < 0) || (position.getRow() >= this.rows)) || 
                ((position.getCol() < 0) || (position.getCol() >= this.cols)));
    }

    /**
     * Check if grid Point is blocked.
     * @param position Grid Point
     * @return true if tile is blocked (player may not walk onto it)
     */
    public boolean isBlocked(Point position){
        if (isOutOfBounds(position)) {
            return true;
        }
        
        Cell cell = getCell(position);
        if (cell == null) {
            return true;
        }
        
        CellFill checkedCellFill = cell.getCellFill();
        return (checkedCellFill == CellFill.BARRIER);
    }
    
    /**
     * Check if a position is the exit
     */
    public boolean isExit(Point position){
        if (isOutOfBounds(position)) {
            return false;
        }
        
        Cell cell = getCell(position);
        if (cell == null) {
            return false;
        }
        
        return (cell.getCellFill() == CellFill.EXIT);
    }
}