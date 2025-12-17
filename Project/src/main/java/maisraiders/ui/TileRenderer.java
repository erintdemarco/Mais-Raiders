package maisraiders.ui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import maisraiders.enums.CellFill;
import maisraiders.panel.Board;
import maisraiders.panel.Cell;
import maisraiders.util.Point;

import javax.imageio.ImageIO;

/**
 * REFACTORED: Beautiful tile renderer with improved code quality
 * 
 * REFACTORING CHANGES:
 * 1. Organized BufferedImage fields into logical groups
 * 2. Extracted image loading into configuration-driven method
 * 3. Replaced long switch with Strategy pattern
 * 4. Fixed poor formatting
 * 5. Unified duplicate tile drawing methods
 */
public class TileRenderer {
    private final int tileSize;
    
    // REFACTORING #1: Organize Fields into Logical Groups
    // OLD CODE: All 13 images on one line
    // BufferedImage dirt, cornWall, exit, door, houseLHS, houseRHS1, houseRHS2, borderT, borderB, borderR, borderL, hay, tractor;
    //
    // REASON: Hard to read, no organization, difficult to find specific image
    // NEW: Grouped by category with clear comments
    
    // Terrain tiles
    private BufferedImage dirt;
    private BufferedImage cornWall;
    
    // Special tiles  
    private BufferedImage exit;
    
    // House tiles
    private BufferedImage door;
    private BufferedImage houseLHS;
    private BufferedImage houseRHS1;
    private BufferedImage houseRHS2;
    
    // Border tiles
    private BufferedImage borderT;
    private BufferedImage borderB;
    private BufferedImage borderR;
    private BufferedImage borderL;
    
    // Objects
    private BufferedImage hay;
    private BufferedImage tractor;
    
    // REFACTORING #2: Configuration for Image Loading
    // REASON: Eliminates 13 lines of repetitive ImageIO.read() calls
    //         Makes it easy to add new images
    //         Separates data (paths) from logic (loading)
    private static final String[][] IMAGE_CONFIGS = {
        {"dirt", "/tiles/dirt2.png"},
        {"cornWall", "/tiles/grass.png"},
        {"exit", "/tiles/exit.png"},
        {"door", "/tiles/doorB.png"},
        {"houseLHS", "/tiles/lhs-houseB.png"},
        {"houseRHS1", "/tiles/rhs-house-inB.png"},
        {"houseRHS2", "/tiles/rhs-house-outB.png"},
        {"borderB", "/tiles/bordB.png"},
        {"borderT", "/tiles/bordT.png"},
        {"borderR", "/tiles/bordR.png"},
        {"borderL", "/tiles/bordL.png"},
        {"hay", "/objects/haybale.png"},
        {"tractor", "/objects/tractor.png"}
    };
    
    // REFACTORING #5: Arrays for Unified Drawing
    // REASON: Eliminates duplicate if-else logic in drawBorderTile and drawHouseTile
    //         Makes it easier to add new border/house types
    private BufferedImage[] borderImages;
    private BufferedImage[] houseImages;
    
    // REFACTORING #3: Strategy Pattern for Tile Drawing
    // REASON: Replaces 57-line switch statement
    //         Follows Open/Closed Principle - can add new tiles without modifying switch
    //         More flexible and extensible
    private Map<CellFill, TileDrawer> tileDrawers;
    
    public TileRenderer(int tileSize) {
        this.tileSize = tileSize;
        loadImages();
        initializeTileArrays();
        initializeTileDrawers();
    }
    
    /**
     * REFACTORED: Load all images using configuration
     * 
     * OLD CODE (lines 28-45): 13 individual ImageIO.read() calls
     * try {
     *     dirt = ImageIO.read(getClass().getResourceAsStream("/tiles/dirt2.png"));
     *     cornWall = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
     *     exit = ImageIO.read(getClass().getResourceAsStream("/tiles/exit.png"));
     *     door = ImageIO.read(getClass().getResourceAsStream("/tiles/doorB.png"));
     *     houseLHS = ImageIO.read(getClass().getResourceAsStream("/tiles/lhs-houseB.png"));
     *     houseRHS1 = ImageIO.read(getClass().getResourceAsStream("/tiles/rhs-house-inB.png"));
     *     houseRHS2 = ImageIO.read(getClass().getResourceAsStream("/tiles/rhs-house-outB.png"));
     *     borderB = ImageIO.read(getClass().getResourceAsStream("/tiles/bordB.png"));
     *     borderT = ImageIO.read(getClass().getResourceAsStream("/tiles/bordT.png"));
     *     borderR = ImageIO.read(getClass().getResourceAsStream("/tiles/bordR.png"));
     *     borderL = ImageIO.read(getClass().getResourceAsStream("/tiles/bordL.png"));
     *     hay = ImageIO.read(getClass().getResourceAsStream("/objects/haybale.png"));
     *     tractor = ImageIO.read(getClass().getResourceAsStream("/objects/tractor.png"));
     * } catch (IOException e) {
     *     e.printStackTrace();
     * }
     * 
     * REFACTORING #2: Configuration-driven loading
     * REASON: DRY principle - eliminates duplication
     *         Easier to add new images - just add to IMAGE_CONFIGS array
     *         Cleaner, more maintainable code
     */
    private void loadImages() {
        try {
            // NEW: Loop through configuration instead of 13 individual lines
            for (String[] config : IMAGE_CONFIGS) {
                String fieldName = config[0];
                String path = config[1];
                BufferedImage image = ImageIO.read(getClass().getResourceAsStream(path));
                
                // Assign to appropriate field using reflection alternative
                assignImageToField(fieldName, image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to assign loaded images to fields
     * REASON: Avoids reflection while keeping code clean
     */
    private void assignImageToField(String fieldName, BufferedImage image) {
        switch (fieldName) {
            case "dirt" -> dirt = image;
            case "cornWall" -> cornWall = image;
            case "exit" -> exit = image;
            case "door" -> door = image;
            case "houseLHS" -> houseLHS = image;
            case "houseRHS1" -> houseRHS1 = image;
            case "houseRHS2" -> houseRHS2 = image;
            case "borderB" -> borderB = image;
            case "borderT" -> borderT = image;
            case "borderR" -> borderR = image;
            case "borderL" -> borderL = image;
            case "hay" -> hay = image;
            case "tractor" -> tractor = image;
        }
    }
    
    /**
     * REFACTORED: Initialize tile arrays for unified drawing
     * NEW METHOD
     * REASON: Sets up arrays for drawBorderTile and drawHouseTile
     *         Eliminates duplicate if-else logic
     */
    private void initializeTileArrays() {
        // Index 0 unused, indices 1-4 map to tile types
        borderImages = new BufferedImage[]{null, borderT, borderB, borderL, borderR};
        houseImages = new BufferedImage[]{null, houseLHS, door, houseRHS1, houseRHS2};
    }
    
    /**
     * REFACTORED: Initialize tile drawing strategy map
     * NEW METHOD
     * 
     * REASON: Replaces 57-line switch statement with flexible map-based dispatch
     *         Each tile type has its own drawing strategy
     *         Easy to add new tile types without modifying switch
     */
    private void initializeTileDrawers() {
        tileDrawers = new HashMap<>();
        
        // Map each CellFill type to its drawing method
        tileDrawers.put(CellFill.BARRIER, this::drawWallTile);
        tileDrawers.put(CellFill.EXIT, this::drawExitTile);
        tileDrawers.put(CellFill.START, this::drawDirtTile);
        tileDrawers.put(CellFill.NULL, this::drawDirtTile);
        
        // House tiles with type parameter
        tileDrawers.put(CellFill.HOUSE1, (g2, x, y) -> drawTileFromArray(g2, x, y, houseImages, 1));
        tileDrawers.put(CellFill.HOUSE2, (g2, x, y) -> drawTileFromArray(g2, x, y, houseImages, 2));
        tileDrawers.put(CellFill.HOUSE3, (g2, x, y) -> drawTileFromArray(g2, x, y, houseImages, 3));
        tileDrawers.put(CellFill.HOUSE4, (g2, x, y) -> drawTileFromArray(g2, x, y, houseImages, 4));
        
        // Border tiles with type parameter
        tileDrawers.put(CellFill.BORDER1, (g2, x, y) -> drawBorderTile(g2, x, y, 1));
        tileDrawers.put(CellFill.BORDER2, (g2, x, y) -> drawBorderTile(g2, x, y, 2));
        tileDrawers.put(CellFill.BORDER3, (g2, x, y) -> drawBorderTile(g2, x, y, 3));
        tileDrawers.put(CellFill.BORDER4, (g2, x, y) -> drawBorderTile(g2, x, y, 4));
        
        // Objects (drawn on dirt background)
        tileDrawers.put(CellFill.HAYBALE, (g2, x, y) -> {
            drawDirtTile(g2, x, y);
            drawHay(g2, x, y);
        });
        tileDrawers.put(CellFill.TRACTOR, (g2, x, y) -> {
            drawDirtTile(g2, x, y);
            drawTractor(g2, x, y);
        });
    }
    
    /**
     * Draws the entire game board with enhanced graphics
     * NO CHANGES - method is already clean
     */
    public void drawBoard(Graphics2D g2, Board board) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int cols = board.getCols();
        int rows = board.getRows();
        
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                Point pos = new Point(c, r);
                Cell cell = board.getCell(pos);
                
                if (cell != null) {
                    drawTile(g2, cell, c, r);
                }
            }
        }
    }
    
    /**
     * REFACTORED: Draws a single tile using strategy pattern
     * 
     * OLD CODE (lines 74-138): 57-line switch statement
     * private void drawTile(Graphics2D g2, Cell cell, int col, int row) {
     *     int x = col * tileSize;
     *     int y = row * tileSize;
     *     CellFill fillType = cell.getCellFill();
     *     
     *     switch (fillType) {
     *         case BARRIER:
     *             drawWallTile(g2, x, y);
     *             break;
     *         case EXIT:
     *             drawExitTile(g2, x, y);
     *             break;
     *         case START:
     *             drawDirtTile(g2, x, y);
     *             break;
     *         case HOUSE1:
     *             drawHouseTile(g2, x, y, 1);
     *             break;
     *         case HOUSE2:
     *             drawHouseTile(g2, x, y, 2);
     *             break;
     *         // ... 10 more cases
     *         case NULL:
     *         default:
     *             drawDirtTile(g2, x, y);
     *             break;
     *     }
     * }
     * 
     * REFACTORING #3: Strategy Pattern
     * REASON: Eliminates long switch statement
     *         More flexible - easy to add new tile types
     *         Follows Open/Closed Principle
     *         Cleaner, more maintainable code (64 lines → 8 lines)
     */
    private void drawTile(Graphics2D g2, Cell cell, int col, int row) {
        int x = col * tileSize;
        int y = row * tileSize;
        CellFill fillType = cell.getCellFill();
        
        // NEW: Use strategy map instead of switch
        TileDrawer drawer = tileDrawers.getOrDefault(fillType, this::drawDirtTile);
        drawer.draw(g2, x, y);
    }
    
    /**
     * Functional interface for tile drawing strategies
     * NEW INTERFACE
     * REASON: Enables strategy pattern for tile rendering
     */
    @FunctionalInterface
    interface TileDrawer {
        void draw(Graphics2D g2, int x, int y);
    }
    
    // ==================== Individual Tile Drawing Methods ====================
    
    /**
     * REFACTORED: Basic tile drawing methods with proper formatting
     * 
     * OLD CODE (lines 146-147): Poor formatting - all on one line
     * private void drawHay(Graphics2D g2, int x, int y){g2.drawImage(hay, x, y, tileSize, tileSize,null);}
     * private void drawTractor(Graphics2D g2, int x, int y) { g2.drawImage(tractor, x, y, tileSize, tileSize,null);}
     * 
     * REFACTORING #4: Fix Poor Formatting
     * REASON: Follows Java conventions, easier to read and debug
     */
    private void drawExitTile(Graphics2D g2, int x, int y) {
        g2.drawImage(exit, x, y, tileSize, tileSize, null);
    }
    
    private void drawWallTile(Graphics2D g2, int x, int y) {
        g2.drawImage(cornWall, x, y, tileSize, tileSize, null);
    }
    
    private void drawHay(Graphics2D g2, int x, int y) {
        g2.drawImage(hay, x, y, tileSize, tileSize, null);
    }
    
    private void drawTractor(Graphics2D g2, int x, int y) {
        g2.drawImage(tractor, x, y, tileSize, tileSize, null);
    }
    
    private void drawDirtTile(Graphics2D g2, int x, int y) {
        g2.drawImage(dirt, x, y, tileSize, tileSize, null);
    }
    
    /**
     * REFACTORED: Draw border tile using array lookup
     * 
     * OLD CODE (lines 151-161): if-else chain
     * private void drawBorderTile(Graphics2D g2, int x, int y, int type) {
     *     if (type == 1) {
     *         g2.drawImage(borderT, x, y, tileSize, tileSize, null);
     *     } else if (type == 2) {
     *         g2.drawImage(borderB, x, y, tileSize, tileSize, null);
     *     } else if (type == 3) {
     *         g2.drawImage(borderL, x, y, tileSize, tileSize, null);
     *     } else if (type == 4) {
     *         g2.drawImage(borderR, x, y, tileSize, tileSize, null);
     *     }
     * }
     * 
     * REFACTORING #5: Unified Array-Based Drawing
     * REASON: Eliminates duplicate if-else structure
     *         Cleaner, more maintainable (11 lines → 3 lines)
     *         Same pattern used for borders and houses
     */
    private void drawBorderTile(Graphics2D g2, int x, int y, int type) {
        drawTileFromArray(g2, x, y, borderImages, type);
    }
    
    /**
     * REFACTORED: Draw house tile using array lookup
     * 
     * OLD CODE (lines 163-173): Same if-else structure as drawBorderTile
     * private void drawHouseTile(Graphics2D g2, int x, int y, int type) {
     *     if (type == 1) {
     *         g2.drawImage(houseLHS, x, y, tileSize, tileSize, null);
     *     } else if (type == 2) {
     *         g2.drawImage(door, x, y, tileSize, tileSize, null);
     *     } else if (type == 3) {
     *         g2.drawImage(houseRHS1, x, y, tileSize, tileSize, null);
     *     } else if (type == 4) {
     *         g2.drawImage(houseRHS2, x, y, tileSize, tileSize, null);
     *     }
     * }
     * 
     * REFACTORING #5: Same unified approach as borders
     */
    
    /**
     * REFACTORED: Unified array-based tile drawing
     * NEW METHOD
     * 
     * REASON: Eliminates duplicate if-else logic in drawBorderTile and drawHouseTile
     *         Single implementation for array-based drawing
     *         DRY principle - reusable for any array-based tile drawing
     */
    private void drawTileFromArray(Graphics2D g2, int x, int y, BufferedImage[] images, int index) {
        if (index > 0 && index < images.length && images[index] != null) {
            g2.drawImage(images[index], x, y, tileSize, tileSize, null);
        }
    }
}

/*
 * SUMMARY OF REFACTORINGS:
 * 
 * 1. LONG PARAMETER LIST → ORGANIZED FIELDS
 *    - 13 images on one line → Grouped by category
 *    - Improves readability and maintainability
 * 
 * 2. CODE DUPLICATION → CONFIGURATION LOOP
 *    - 13 ImageIO.read() calls → Data-driven loading
 *    - DRY principle, easier to add new images
 * 
 * 3. LONG SWITCH STATEMENT → STRATEGY PATTERN
 *    - 57-line switch → Map-based dispatch
 *    - More flexible, follows Open/Closed Principle
 * 
 * 4. POOR FORMATTING → PROPER JAVA STYLE
 *    - Single-line methods → Multi-line formatted methods
 *    - Follows Java conventions
 * 
 * 5. DUPLICATE CODE → UNIFIED METHOD
 *    - Two identical if-else chains → One array-based method
 *    - DRY principle, more maintainable
 * 
 * TESTING: Visual verification - game renders identically ✓
 * BEHAVIOR: Preserved - all tiles draw correctly
 * 
 * BENEFITS:
 * - Easier to add new tile types
 * - Cleaner, more organized code
 * - Better follows SOLID principles
 * - More maintainable and extensible
 */