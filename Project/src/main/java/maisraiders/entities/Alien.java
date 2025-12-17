package maisraiders.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import maisraiders.enums.Direction;
import maisraiders.panel.Board;
import maisraiders.panel.Cell;
import maisraiders.util.Point;

/**
 * Alien (moving enemy) that pursues player.
 */
public class Alien extends MovingEntity {
    private List<Point> path = new ArrayList<>();
    private int pathIndex = 0;
    private final AStar.Heuristic heuristic;
    private boolean isParalyzed = false;
    private long paralysisEndTime = 0;

    private int ticksSinceCompute = 0;

    // tweak for performance/smoothness
    private final int recomputeTicks = 6; 

    // pixels threshold to consider the alien has "arrived" at a tile center
    private static final int ARRIVE_THRESHOLD = 4;

    private static int IMAGE_CYCLE = 4;

    /**
     * Alien cconstructor (octile default heuristic).
     * @param startPosition Alien starting grid position
     * @param startMoveSpeedAlien Movement speed
     */
    public Alien(Point startPosition, int startMoveSpeed) {
        this(startPosition, startMoveSpeed, AStar.octileHeuristic);
    }

    /**
     * Alien constructor (provided heuristic).
     * @param startPosition Alien starting grid position
     * @param startMoveSpeedAlien Movement speed
     * @param heuristic Provided heuristic
     */
    public Alien(Point startPosition, int startMoveSpeed, AStar.Heuristic heuristic) {
        super(startPosition, startMoveSpeed);
        this.heuristic = heuristic;
        getAlienImage();
    }

    /**
    * Load alien sprite image(s).
    */
    public final void getAlienImage() {

        try {
            BufferedImage[] up = loadFrames("/sprites/alien/alien-bw", IMAGE_CYCLE);
            BufferedImage[] down = loadFrames("/sprites/alien/alien-fw", IMAGE_CYCLE);
            BufferedImage[] left = loadFrames("/sprites/alien/alien-lw", IMAGE_CYCLE);
            BufferedImage[] right = loadFrames("/sprites/alien/alien-rw", IMAGE_CYCLE);

            up1 = up[0]; up2 = up[1]; up3 = up[2]; up4 = up[3];
            down1 = down[0]; down2 = down[1]; down3 = down[2]; down4 = down[3];
            right1 = right[0]; right2 = right[1]; right3 = right[2]; right4 = right[3];
            left1 = left[0]; left2 = left[1]; left3 = left[2]; left4 = left[3];

        } catch (IOException e) {
            System.err.println("Failed to load alien sprites: " + e.getMessage());
        }
    }


   /**
    * Load a cycle of frames from resources at the given path; throws on failure.
    * @param path Image path
    * @param cycle The number of image cycles for a direction
    * @return An array of BufferedImages
    * @throws IOException
    */
    private BufferedImage[] loadFrames(String path, int cycle) throws IOException {
        BufferedImage[] frames = new BufferedImage[cycle];
        for (int i = 0; i < cycle; i++) {
            String cyclePath = path + (i + 1) + ".png";
            try (InputStream input = getClass().getResourceAsStream(cyclePath)) {
                if (input == null) {
                    throw new IOException("Resource not found on classpath: " + cyclePath);
                }
                BufferedImage img = ImageIO.read(input);
                if (img == null) {
                    throw new IOException("Failed to decode image: " + cyclePath);
                }
                frames[i] = img;
            }
        }
        return frames;
    }

    // --- Extracted helpers ---

    /**
     * Compute path between two points using the configured A* heuristic; returns empty list on error.
     * @param board The game board
     * @param start The starting point
     * @param end The ending point
     * @return List of points from start to end
     */
    private List<Point> computePath(Board board, Point start, Point end) {
        try {
            List<Point> np = AStar.findPath(board, start, end, this.heuristic);
            return np == null ? Collections.emptyList() : np;
        } catch (Exception e) {
            // defensive: return empty path on unexpected errors
            return Collections.emptyList();
        }
    }

    /**
     * Choose a safe next index into a newly computed path given the alien's current cell.
     * @param newPath the newly computed path 
     * @param myCell the alien's current logical grid cell
     * @return an index into newPath representing the next step
     *         
     */
    private int chooseNextPathIndex(List<Point> newPath, Point myCell) {
        if (newPath == null || newPath.isEmpty()) return 0;
        int idx = -1;
        for (int i = 0; i < newPath.size(); i++) {
            Point p = newPath.get(i);
            if (p.getCol() == myCell.getCol() && p.getRow() == myCell.getRow()) { idx = i; break; }
        }
        if (idx >= 0) {
            return Math.min(idx + 1, newPath.size() - 1);
        } else {
            int bestI = 0;
            int bestDist = Integer.MAX_VALUE;
            for (int i = 0; i < newPath.size(); i++) {
                Point p = newPath.get(i);
                int dx = p.getCol() - myCell.getCol();
                int dy = p.getRow() - myCell.getRow();
                int d = dx*dx + dy*dy;
                if (d < bestDist) { bestDist = d; bestI = i; }
            }
            return Math.min(bestI + 1, newPath.size() - 1);
        }
    }

    /**
     * Check if alien has reached the center of given target tile and, if so,
     * update the alien's logical grid position.
     * @param target Alien's destination grid cell
     * @param tileSize Size of a single tile
     * @param halfTile Half of tileSize (used to compute tile center)
     * @return True if alien was close enough to the tile center; false otherwise
     */
    private boolean advanceIfArrived(Point target, int tileSize, int halfTile) {
        int subX = getSubPositionX();
        int subY = getSubPositionY();
        int targetCenterX = target.getCol() * tileSize + halfTile;
        int targetCenterY = target.getRow() * tileSize + halfTile;
        if (Math.abs(subX - targetCenterX) <= ARRIVE_THRESHOLD &&
            Math.abs(subY - targetCenterY) <= ARRIVE_THRESHOLD) {
            try {
                this.setPosition(new Point(target.getCol(), target.getRow()));
            } catch (Throwable ignored) {}
            return true;
        }
        return false;
    }

    /**
     * Fallback algorithm when no full path is available.
     * @param board The game board 
     * @param farmer The player being chased
     */
    private void greedyFallback(Board board, Farmer farmer) {
        Point logicalCell = getPosition();
        int fdx = farmer.getPosition().getCol() - logicalCell.getCol();
        int fdy = farmer.getPosition().getRow() - logicalCell.getRow();
        if (fdx != 0 || fdy != 0) {
            Direction dir = directionFromDelta(fdx, fdy);
            if (dir != null && dir != Direction.NONE) {
                Cell[] neighbours = buildNeighboursArray(board, logicalCell);
                move(dir, neighbours);
                directionEnum = dir;
            }
        }
    }

    /**
     * Convert a grid delta into a movement direction.
     * @param dx Horizontal difference (positive = right, negative = left)
     * @param dy Vertical difference (positive = down, negative = up)
     * @return The best direction to move
     */
    private Direction directionFromDelta(int dx, int dy) {
        int sx = Integer.signum(dx);
        int sy = Integer.signum(dy);

        if (sx == 0 && sy == -1) return Direction.N;
        if (sx == 0 && sy == 1)  return Direction.S;
        if (sx == -1 && sy == 0) return Direction.W;
        if (sx == 1 && sy == 0)  return Direction.E;
        if (sx == -1 && sy == -1) return Direction.NW;
        if (sx == 1  && sy == -1) return Direction.NE;
        if (sx == -1 && sy == 1)  return Direction.SW;
        if (sx == 1  && sy == 1)  return Direction.SE;

        return Direction.NONE;
    }

    /**
     * Return neighbour cells for the given grid cell from the board. 
     * @param board The game board
     * @param cell Current cell
     * @return Current cell's neighbours
     */
    private Cell[] buildNeighboursArray(Board board, Point cell) {
        return board.getNeighbours(cell);
    }

    /**
     * A function to paralyze the alien when catching the farmer with a pitchfork
     */
    public void paralyze(int durationMs) {
        isParalyzed = true;
        paralysisEndTime = System.currentTimeMillis() + durationMs;
    }

    /**
    * Calculates and updates path + movement towards the player.
    * @param board the game board
    * @param farmer the player being chased
    */
    public void update(Board board, Farmer farmer) {
        // is the alien catches the farmer with the pitchfork, pause alien movement
        if (isParalyzed) {
            long now = System.currentTimeMillis();
            if (now < paralysisEndTime) {
                // Skip ALL movement and behavior
                return;
            } else {
                // Paralysis has expired
                isParalyzed = false;
            }
        }

        ticksSinceCompute++;

        Point myCell = getPosition();
        Point farmerCell = farmer.getPosition();
        int tileSize = 64;
        int halfTile = tileSize / 2;


        boolean needRecompute = ticksSinceCompute >= recomputeTicks
                || path == null
                || path.isEmpty()
                || !path.get(path.size() - 1).equals(farmerCell)
                || ticksSinceCompute >= recomputeTicks;

        if (needRecompute) {
            List<Point> newPath = computePath(board, myCell, farmerCell);
            int newIndex = chooseNextPathIndex(newPath, myCell);

            path = newPath == null ? Collections.emptyList() : newPath;
            pathIndex = Math.max(0, Math.min(newIndex, (path == null ? 0 : Math.max(0, path.size()-1))));
            ticksSinceCompute = 0;
        }

        if (path != null && !path.isEmpty() && pathIndex >= 0 && pathIndex < path.size()) {
            Point target = path.get(pathIndex);

            Point approxCell = getPosition();
            
            int dx = target.getCol() - approxCell.getCol();
            int dy = target.getRow() - approxCell.getRow();

            if (advanceIfArrived(target, tileSize, halfTile)) {
                pathIndex++;
                return;
            }

            Direction dir = directionFromDelta(dx, dy);
            if (dir != null && dir != Direction.NONE) {
                Cell[] neighbours = buildNeighboursArray(board, approxCell);
                move(dir, neighbours);
                directionEnum = dir;
            }
          
            //update animation
            if(spriteCounter > 10) { // hold each sprite animation frame
                switch (spriteNum) {
                    case 1 -> spriteNum = 2;
                    case 2 -> spriteNum = 3;
                    case 3 -> spriteNum = 4;
                    case 4 -> spriteNum = 1;
                    default -> {
                    }

                }
                spriteCounter = 0;
            }
            spriteCounter++;
            return;
        }

        greedyFallback(board, farmer);
    }

    /**
    * Draw alien sprite centred on pixel position
    * @param g2 Image renderer
    * @param size drawing size
    */
    public void draw(Graphics2D g2, int size) {
        BufferedImage image = null;

            if(null !=directionEnum ) switch (directionEnum) {
            case N, NW, NE -> {
            switch (spriteNum) {
                case 1 -> image = up1;
                case 2 -> image = up2;
                case 3 -> image = up3;
                case 4 -> image = up4;
                default -> {
                }
            }
            }
            case S, SW, SE -> {
            switch (spriteNum) {
                case 1 -> image = down1;
                case 2 -> image = down2;
                case 3 -> image = down3;
                case 4 -> image = down4;
                default -> {
                }
            }
            }
            case E -> {
            switch (spriteNum) {
                case 1 -> image = right1;
                case 2 -> image = right2;
                case 3 -> image = right3;
                case 4 -> image = right4;
                default -> {
                }
            }
            }
            case W -> {
            switch (spriteNum) {
                case 1 -> image = left1;
                case 2 -> image = left2;
                case 3 -> image = left3;
                case 4 -> image = left4;
                default -> {
                }

            }
            }
            default -> {
            }
        }

        // Draw centered: subtract half the size from subPosition to center the sprite
        int drawX = getSubPositionX() - (size / 2);
        int drawY = getSubPositionY() - (size / 2);
        g2.drawImage(image, drawX, drawY, size, size, null);
    }
}