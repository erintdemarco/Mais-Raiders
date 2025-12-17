package maisraiders.util;

public class Point {
    private final int row;
    private final int col;

    /**
     * Point Constructor.
     * @param startCol Column index 
     * @param startRow Row index
     */
    public Point(int startCol, int startRow){
        this.row = startRow;
        this.col = startCol;
    }

    /**
     * Get row index.
     * @return Row index
     */
    public int getRow(){
        return row;
    }

    /**
     * Get column index.
     * @return Column index
     */
    public int getCol(){
        return col;
    }

    /**
     * Compare two by points via column and row.
     * @param o other object
     * @return true if both points share idental coordinates (column and row)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return this.col == p.col && this.row == p.row;
    }

    /**
     * Hash code based on column and row
     * @return Hash value
     */
    @Override
    public int hashCode() {
        return 31 * col + row;
    }

    /**
     * Convert point to string representaion
     * @return string representaion of point
     */
    @Override
    public String toString() {
        return "(" + col + "," + row + ")";
    }
}
