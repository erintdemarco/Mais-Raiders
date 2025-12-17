package maisraiders.ui;

import maisraiders.enums.GameState;
import maisraiders.panel.Game;
import maisraiders.panel.GameLoop;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.text.DecimalFormat;

public class Ui {

    GameLoop gl;
    Font maruMonica;
    Font ariel50;
    Game game;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    double playTime;
    public int commandNum;
    BufferedImage cornLHS, cornRHS, alienLHS, alienRHS, bkg, farmerbkg;
    Graphics2D g2;
    DecimalFormat dFormat = new DecimalFormat("#0.0");

    //default constructor for testing purposes
    public Ui() {}

    public Ui(GameLoop gl, Game game) {
        this.gl = gl;
        this.game = game;
        ariel50 = new Font("Ariel", Font.PLAIN, 50);
        try {
            //get font package
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            //get images for title screen
            cornLHS = ImageIO.read(getClass().getResourceAsStream("/objects/corn-side-bar-lhs.png"));
            cornRHS = ImageIO.read(getClass().getResourceAsStream("/objects/corn-side-bar-rhs.png"));
            alienLHS = ImageIO.read(getClass().getResourceAsStream("/objects/alien-side-bar-lhs.png"));
            alienRHS = ImageIO.read(getClass().getResourceAsStream("/objects/alien-side-bar-rhs.png"));
            bkg = ImageIO.read(getClass().getResourceAsStream("/sprites/titlescreen/title-bkg-house.png"));
            farmerbkg = ImageIO.read(getClass().getResourceAsStream("/sprites/titlescreen/farmer-title.png"));
        }catch(FontFormatException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    /**
     * draws the ingame ui based on the game's game state
     * @param g2 current graphics drawer
     */
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);
        //TITLE STATE
        if(game.getGameState() == GameState.TITLE){
            drawTitleScreen();
        }
        // PLAY STATE
        if(game.getGameState() == GameState.RUNNING){
            drawPlayScreen();

        }
        // PAUSE STATE
        if(game.getGameState() == GameState.PAUSE){
            drawPauseScreen();
        }
        // WIN STATE
        if(game.getGameState() == GameState.WIN){
            drawWinScreen();
        }
        // LOSE STATE
        if(game.getGameState() == GameState.LOSE){
            drawLoseScreen();
        }

    }

    public void drawTitleScreen(){

        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,1280, 1024);
        // main background image:
        g2.drawImage(bkg,0,0, 1280, 1024, null);

        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 135F));
        String text = "Mais Raiders";
        int x = getXforCenteredText(text);
        int y = gl.tileSize*4;

        //SHADOW
        g2.setColor(Color.gray);
        g2.drawString(text, x+5,y+5);

        //Main color
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        //farmer image
        x = gl.screenWidth/2 - (gl.tileSize*8)/2;
        y -= gl.tileSize*2;
        //g2.drawImage(gl.gameFarmer.down1, x, y, gl.tileSize*4, gl.tileSize*4,null); // (image, x,y, width,height (of img), observer)
        g2.drawImage(farmerbkg, x, y, gl.tileSize*8, gl.tileSize*8,null);
        //left side panel
        int xSide = 0;
        int ySide = gl.tileSize*4;
        g2.drawImage(cornLHS, xSide, ySide, gl.tileSize*6, gl.tileSize*12, null);

        xSide = gl.tileSize;
        ySide = gl.tileSize;
        g2.drawImage(alienLHS,xSide, ySide, gl.tileSize*4, gl.tileSize*8, null);

        //right side panel
        xSide = gl.tileSize*14;
        ySide = gl.tileSize*4;
        g2.drawImage(cornRHS,xSide, ySide, gl.tileSize*6, gl.tileSize*12, null);

        xSide = gl.tileSize*15;
        ySide = gl.tileSize;
        g2.drawImage(alienRHS,xSide, ySide, gl.tileSize*4, gl.tileSize*8, null);

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));

        text = "PLAY GAME";
        x = getXforCenteredText(text);
        y += gl.tileSize*10;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x -gl.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gl.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x -gl.tileSize, y);
        }
    }
    public void drawPlayScreen(){

        g2.setFont(ariel50);
        g2.setColor(Color.white);
        g2.drawString("Corn "+ game.getCollectedRegular() + " / 8", 60, 55);
        g2.drawString("Points: "+ game.getScore(), 525,55);

        // TIME
        playTime +=(double)1/60; //called 60 times per second
        g2.drawString("Time:"+ dFormat.format(playTime), gl.tileSize*16, 55);
        //MESSAGES
        if(messageOn) {
            g2.setFont(maruMonica);
            g2.setFont(g2.getFont().deriveFont(50F)); //make text smaller
            g2.drawString(message, getXforCenteredText(message), gl.tileSize * 3);

            messageCounter++;
            if (messageCounter > 120) { // 2 seconds
                messageCounter = 0;
                messageOn = false;
            }
        }

    }
    public void drawPauseScreen(){
        g2.setColor( new Color(0,0,0,150));
        g2.fillRect(0,0, 1280, 1024);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gl.tileSize*6;
        //SHADOW
        g2.setColor(Color.gray);
        g2.drawString(text, x+5,y+5);
        // main text
        g2.setColor(Color.white);
        g2.drawString(text, x,y);

        // change font size for options
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        text = "Time:"+ dFormat.format(playTime);
        y += gl.tileSize;
        g2.drawString(text, getXforCenteredText(text), y);

        text = "RESUME";
        x = getXforCenteredText(text);
        y += gl.tileSize*2;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x -gl.tileSize, y);
        }

        text = "MAIN MENU";
        x = getXforCenteredText(text);
        y += gl.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x -gl.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gl.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 2){
            g2.drawString(">", x -gl.tileSize, y);
        }

    }
    public void drawWinScreen(){
        g2.setColor( new Color(0,0,0,150));
        g2.fillRect(0,0, 1280, 1024);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "CONGRATULATIONS YOU WIN!";
        int x = getXforCenteredText(text);
        int y = gl.tileSize*6;
        //SHADOW
        g2.setColor(Color.gray);
        g2.drawString(text, x+5,y+5);
        // main text
        g2.setColor(Color.white);
        g2.drawString(text, x,y);

        // change font size for options
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        text = "Time:"+ dFormat.format(playTime);
        y += gl.tileSize;
        g2.drawString(text, getXforCenteredText(text), y);

        text = "Score:"+ dFormat.format(game.getScore());
        y += gl.tileSize;
        g2.drawString(text, getXforCenteredText(text), y);


        text = "MAIN MENU";
        x = getXforCenteredText(text);
        y += gl.tileSize*3;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x -gl.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gl.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x -gl.tileSize, y);
        }

    }
    public void drawLoseScreen(){
        g2.setColor( new Color(0,0,0,150));
        g2.fillRect(0,0, 1280, 1024);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "YOU LOSE";
        int x = getXforCenteredText(text);
        int y = gl.tileSize*6;
        //SHADOW
        g2.setColor(Color.gray);
        g2.drawString(text, x+5,y+5);
        // main text
        g2.setColor(Color.white);
        g2.drawString(text, x,y);

        // change font size for options
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        text = game.losseMsg;
        y += gl.tileSize;
        g2.drawString(text, getXforCenteredText(text), y);

        text = "Time:"+ dFormat.format(playTime);
        y += gl.tileSize;
        g2.drawString(text, getXforCenteredText(text), y);

        text = "Score:"+ dFormat.format(game.getScore());
        y += gl.tileSize;
        g2.drawString(text, getXforCenteredText(text), y);

        text = "MAIN MENU";
        x = getXforCenteredText(text);
        y += gl.tileSize*2;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x -gl.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gl.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x -gl.tileSize, y);
        }

    }
    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
         return gl.screenWidth/2 - length/2;

    }
    public void resetPlayTime() {
    playTime = 0;
}
}
