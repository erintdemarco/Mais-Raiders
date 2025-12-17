package maisraiders;

import maisraiders.enums.CellFill;
import maisraiders.map.MapLoader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Unit tests for MapLoader - validates map structure and layout
 */
public class MapLoaderTest {
    
    private int[][] map;
    
    @BeforeEach
    void setUp() {
        map = MapLoader.getMapLayout();
    }
    
    // Map Structure 
    
    @Test
    void testMapDimensions() {
        assertEquals(20, map.length, "Map should have 20 columns");
        assertEquals(16, map[0].length, "Map should have 16 rows");
    }
    
    // Border Validation 
    
    @Test
    void testAllBordersAreComplete() {
        // Top border (BORDER1 = 8) - skip corners which are left/right borders
        for (int c = 1; c < 19; c++) {
            assertEquals(8, map[c][0], "Top border incomplete at column " + c);
        }
        
        // Bottom border (BORDER2 = 9) - skip corners which are left/right borders  
        for (int c = 1; c < 19; c++) {
            assertEquals(9, map[c][15], "Bottom border incomplete at column " + c);
        }
        
        // Left border (BORDER3 = 10) - includes corners
        for (int r = 0; r < 16; r++) {
            assertEquals(10, map[0][r], "Left border incomplete at row " + r);
        }
        
        // Right border - house tiles at rows 1-4, then BORDER4
        assertEquals(4, map[19][1], "Missing HOUSE1");
        assertEquals(5, map[19][2], "Missing HOUSE2");
        assertEquals(6, map[19][3], "Missing HOUSE3");
        assertEquals(7, map[19][4], "Missing HOUSE4");
        
        // Rest should be BORDER4 (skip row 0 and 15 which might be top/bottom borders)
        for (int r = 5; r < 15; r++) {
            assertEquals(11, map[19][r], "Right border incomplete at row " + r);
        }
    }
    
    // Exit Validation 
    
    @Test
    void testExitPlacement() {
        assertEquals(2, map[18][2], "Exit should be at (18,2)");
        
        // Count exits
        int exitCount = 0;
        for (int c = 0; c < 20; c++) {
            for (int r = 0; r < 16; r++) {
                if (map[c][r] == 2) exitCount++;
            }
        }
        assertEquals(1, exitCount, "Should have exactly one exit");
    }
    
    //  Spawn Points 
    
    @Test
    void testSpawnPointsAreWalkable() {
        // Farmer spawn (9,7)
        int farmerTile = map[9][7];
        assertTrue(farmerTile == 0 || farmerTile == 3, "Farmer spawn should be walkable");
        
        // Alien spawns (1,1) and (15,5)
        int alien1Tile = map[1][1];
        assertTrue(alien1Tile == 0 || alien1Tile == 3, "Alien1 spawn should be walkable");
        
        int alien2Tile = map[15][5];
        assertTrue(alien2Tile == 0 || alien2Tile == 3, "Alien2 spawn should be walkable");
    }
    
    // Object Placement 
    
    @Test
    void testGameObjectPositionsAreWalkable() {
        // Corn positions
        int[][] cornPos = {{3,5}, {6,1}, {10,2}, {8,12}, {12,10}, {14,4}, {5,8}, {2,9}};
        for (int[] pos : cornPos) {
            int value = map[pos[0]][pos[1]];
            assertTrue(value == 0 || value == 3, "Corn at (" + pos[0] + "," + pos[1] + ") should be walkable");
        }
        
        // Trap positions
        int[][] trapPos = {{7,5}, {4,10}, {16,7}};
        for (int[] pos : trapPos) {
            int value = map[pos[0]][pos[1]];
            assertTrue(value == 0 || value == 3, "Trap at (" + pos[0] + "," + pos[1] + ") should be walkable");
        }
        
        // Pitchfork
        int pitchforkValue = map[16][12];
        assertTrue(pitchforkValue == 0 || pitchforkValue == 3, "Pitchfork should be walkable");
    }
    
    @Test
    void testObstaclesArePlaced() {
        assertEquals(13, map[18][12], "Tractor should be at (18,12)");
        assertEquals(12, map[5][14], "Hay bale should be at (5,14)");
        assertEquals(12, map[8][14], "Hay bale should be at (8,14)");
        assertEquals(12, map[12][7], "Hay bale should be at (12,7)");
    }
    
    // CellFill Conversion 
    
    @Test
    void testGetCellFillFromMapValueAllTypes() {
        assertEquals(CellFill.NULL, MapLoader.getCellFillFromMapValue(0));
        assertEquals(CellFill.BARRIER, MapLoader.getCellFillFromMapValue(1));
        assertEquals(CellFill.EXIT, MapLoader.getCellFillFromMapValue(2));
        assertEquals(CellFill.START, MapLoader.getCellFillFromMapValue(3));
        assertEquals(CellFill.HOUSE1, MapLoader.getCellFillFromMapValue(4));
        assertEquals(CellFill.HOUSE2, MapLoader.getCellFillFromMapValue(5));
        assertEquals(CellFill.HOUSE3, MapLoader.getCellFillFromMapValue(6));
        assertEquals(CellFill.HOUSE4, MapLoader.getCellFillFromMapValue(7));
        assertEquals(CellFill.BORDER1, MapLoader.getCellFillFromMapValue(8));
        assertEquals(CellFill.BORDER2, MapLoader.getCellFillFromMapValue(9));
        assertEquals(CellFill.BORDER3, MapLoader.getCellFillFromMapValue(10));
        assertEquals(CellFill.BORDER4, MapLoader.getCellFillFromMapValue(11));
        assertEquals(CellFill.HAYBALE, MapLoader.getCellFillFromMapValue(12));
        assertEquals(CellFill.TRACTOR, MapLoader.getCellFillFromMapValue(13));
    }
    
    @Test
    void testGetCellFillFromMapValueInvalidReturnsNull() {
        assertEquals(CellFill.NULL, MapLoader.getCellFillFromMapValue(-1));
        assertEquals(CellFill.NULL, MapLoader.getCellFillFromMapValue(100));
    }
    
    // ==================== Map Validity ====================
    
    @Test
    void testAllMapValuesAreValid() {
        for (int c = 0; c < 20; c++) {
            for (int r = 0; r < 16; r++) {
                int value = map[c][r];
                assertTrue(value >= 0 && value <= 13, 
                    "Invalid value " + value + " at (" + c + "," + r + ")");
            }
        }
    }
    
    @Test
    void testMapHasSufficientWalkableSpace() {
        int walkableCount = 0;
        for (int c = 0; c < 20; c++) {
            for (int r = 0; r < 16; r++) {
                if (map[c][r] == 0 || map[c][r] == 3) {
                    walkableCount++;
                }
            }
        }
        assertTrue(walkableCount >= 50, "Map should have at least 50 walkable tiles");
    }
    
    @Test
    void testMapConsistency() {
        int[][] map2 = MapLoader.getMapLayout();
        
        for (int c = 0; c < 20; c++) {
            for (int r = 0; r < 16; r++) {
                assertEquals(map[c][r], map2[c][r], "Map inconsistent at (" + c + "," + r + ")");
            }
        }
    }
}
