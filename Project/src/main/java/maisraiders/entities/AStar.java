package maisraiders.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import maisraiders.panel.Board;
import maisraiders.panel.Cell;
import maisraiders.util.Point;

/**
 * Finds path from one cell to another using A* algorithm
 */
public class AStar {
    private static final double DIAGONAL_COST = Math.sqrt(2.0);
    private static final double STRAIGHT_COST = 1.0;

    private static final int[][] neighbors = {
            {-1,  0}, // N
            { 1,  0}, // S
            { 0, -1}, // W
            { 0,  1}, // E
            {-1, -1}, // NW
            {-1,  1}, // NE
            { 1, -1}, // SW
            { 1,  1}  // SE
    };

    /**
     * Find path from start to goal (default heuristic).
     * @param board game board
     * @param start stating grid point 
     * @param goal goal grid point
     * @return list of points from start to goal
     */
    public static List<Point> findPath(Board board, Point start, Point goal) {
        return findPath(board, start, goal, octileHeuristic);
    }

    /**
     * Find path from start to goal (supplied heuristic).
     * @param board game board
     * @param start stating grid point 
     * @param goal goal grid point
     * @param heuristic supplied heuristic function
     * @return list of points from start to goal
     */
    public static List<Point> findPath(Board board, Point start, Point goal, Heuristic heuristic) {
        
        int rows = board.getRows();
        int cols = board.getCols();

        int startRow = start.getRow();
        int startCol = start.getCol();
        int goalRow = goal.getRow();
        int goalCol = goal.getCol();

        if (startRow == goalRow && startCol == goalCol) {
            return Collections.singletonList(start);
        }

        boolean[][] closed = new boolean[rows][cols];
        double[][] gCost = new double[rows][cols];
        for (double[] rowArr : gCost) Arrays.fill(rowArr, Double.POSITIVE_INFINITY);

        Node[][] nodes = new Node[rows][cols];

        PriorityQueue<Node> open = new PriorityQueue<>();
        Node startNode = new Node(startCol, startRow, 0, heuristic.heuristic(startCol, startRow, goalCol, goalRow), null);
        gCost[startRow][startCol] = 0;
        nodes[startRow][startCol] = startNode;
        open.add(startNode);

        while (!open.isEmpty()) {
            Node cur = open.poll();
            if (closed[cur.r][cur.c]) continue;
            if (cur.c == goalCol && cur.r == goalRow ) {
                return reconstructPath(cur);
            }
            closed[cur.r][cur.c] = true;

            for (int[] d : neighbors) {
                int nr = cur.r + d[0];
                int nc = cur.c + d[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;

                Cell cell = board.getCell(new Point(nc, nr));
                if (cell == null) continue;
                if (cell.isBlocked()) continue;

                double stepCost = (d[0] == 0 || d[1] == 0) ? STRAIGHT_COST : DIAGONAL_COST;
                double tentativeGCost = cur.g + stepCost;

                if (tentativeGCost < gCost[nr][nc]) {
                    gCost[nr][nc] = tentativeGCost;
                    double h = heuristic.heuristic(nc, nr, goalCol, goalRow);
                    Node neighbor = new Node(nc, nr, tentativeGCost, tentativeGCost + h, cur);
                    nodes[nr][nc] = neighbor;
                    open.add(neighbor);
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * Heuristic function for A*.
     */
    @FunctionalInterface
    public interface Heuristic {
        double heuristic(int c, int r, int gc, int gr);
    }

    /**
     * Octile heuristic (diagonal-aware).
     */
    public static final Heuristic octileHeuristic = (c, r, gc, gr) -> {
        int dx = Math.abs(c - gc);
        int dy = Math.abs(r - gr);
        int min = Math.min(dx, dy);
        int max = Math.max(dx, dy);
        return min * DIAGONAL_COST + (max - min) * STRAIGHT_COST;
    };

    /**
     * Manhattan heuristic.
     */
    public static final Heuristic manhattanHeuristic = (c, r, gc, gr) -> 
        Math.abs(c - gc) + Math.abs(r - gr);

    /**
     * Build final path from the reached node to start node
     * @param node end node
     * @return list of grid points from start to goal
     */
    private static List<Point> reconstructPath(Node node) {
        LinkedList<Point> path = new LinkedList<>();
        Node cur = node;
        while (cur != null) {
            path.addFirst(new Point(cur.c, cur.r));
            cur = cur.parent;
        }
        return path;
    }

    /**
     * Basic node used in A* (column, row, costs, and parent node)
     */
    private static class Node implements Comparable<Node> {
        // col, row of this node
        final int c, r; 
        // g = cost, f = g + h    
        final double g, f;
        final Node parent;
        Node(int c, int r, double g, double f, Node parent) {
            this.r = r; this.c = c; this.g = g; this.f = f; this.parent = parent;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.f, other.f);
        }
    }
}


