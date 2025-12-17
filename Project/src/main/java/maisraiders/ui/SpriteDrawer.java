package maisraiders.ui;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

import maisraiders.entities.Alien;
import maisraiders.entities.Farmer;
import maisraiders.enums.GameState;
import maisraiders.object.GameObject;
import maisraiders.panel.Board;
import maisraiders.panel.Game;
import maisraiders.panel.GameLoop;

public class SpriteDrawer extends JPanel {
    int spriteSize;
    Farmer farmer;
    Alien alien1;
    Alien alien2;
    public List<GameObject> objects;
    GameLoop gl;
    Ui ui;
    Game game;
    Board gameBoard;
    TileRenderer tileRenderer;
    Graphics2D g2;
    
    public void setSpriteDrawer(GameLoop gameLoop, Game game, Ui ui) {
        gl = gameLoop;
        this.game = game;
        this.ui = ui;
        
        // Initialize the tile renderer
        tileRenderer = new TileRenderer(gl.tileSize);
    }
    
    public void setBoard(Board board) {
        this.gameBoard = board;
    }
    public void resetSprites(){
        // 2. Draw objects (corn, traps, pitchfork)
        if (objects != null) {
            for (int i = 0; i < objects.size(); i++) {
                objects.get(i).draw(g2, gl);
            }
        }

        // 3. Draw player
        if (farmer != null){
            farmer.draw(g2, spriteSize);
        }

        // 4. Draw 1st alien
        if (alien1 != null) {
            alien1.draw(g2, spriteSize);
        }

        // 5. Draw 2nd alien
        if (alien2 != null) {
            alien2.draw(g2, spriteSize);
        }

        // 6. Draw UI on top
        if (ui != null) {
            ui.draw(g2);
        }
    }
    public void drawSprite(Farmer farmer, Alien alien1, Alien alien2,int size, GameObject obj) {
        this.farmer = farmer;
        this.alien1 = alien1;
        this.alien2 = alien2;
        spriteSize = size;
        objects = obj.getInstances();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        this.g2 = (Graphics2D) g;
        if (game != null) {
            if (game.getGameState() == GameState.TITLE) {
                ui.draw(g2);
            } else {
                // DRAW ORDER (back to front):

                // 1. Draw the board tiles (walls, grass, exit)
                if (gameBoard != null && tileRenderer != null) {
                    tileRenderer.drawBoard(g2, gameBoard);
                }

                // 2. Draw objects (corn, traps, pitchfork)
                if (objects != null) {
                    for (int i = 0; i < objects.size(); i++) {
                        objects.get(i).draw(g2, gl);
                    }
                }

                // 3. Draw player
                if (farmer != null) {
                    farmer.draw(g2, spriteSize);
                }

                // 4. Draw 1st alien
                if (alien1 != null) {
                    alien1.draw(g2, spriteSize);
                }

                // 5. Draw 2nd alien
                if (alien2 != null) {
                    alien2.draw(g2, spriteSize);
                }

                // 6. Draw UI on top
                if (ui != null) {
                    ui.draw(g2);
                }
            }


            g2.dispose();
        }
    }
}