package maisraiders.panel;

import maisraiders.enums.CellFill;
import maisraiders.util.Point;

public class Cell {
    private Point position;
    private final CellFill cellFill;

    /**
     * Cell constructor.
     * @param startPosition Cell grid location
     * @param startCellFill Content of cell (CellFill enum)
     */
    public Cell(Point startPosition, CellFill startCellFill){
        this.position = startPosition;
        this.cellFill = startCellFill;
    }

    /**
     * Get the cell's grid position.
     * @return Cell's Point
     */
    public Point getPosition(){
        return position;
    }

    /**
     * Set the cell's grid positon.
     * @param newPosition Cell's new Point
     */
    public void setPosition(Point newPosition){
        this.position = newPosition;
    }

    /**
     * Get what this cell contains.
     * @return CellFill enum
     */
    public CellFill getCellFill(){
        return cellFill;
    }


    /**
     * Checks if cell is blocked (if CellFill == BARRIER).
     * @return true of cell is wall/barrier
     */
    public boolean isBlocked(){
        return !(getCellFill() == CellFill.START || getCellFill() == CellFill.EXIT || getCellFill() == CellFill.NULL);
    }
}
