/*
    Aufgabe 1) Zweidimensionale Arrays und Gameplay - Mastermind
*/

import codedraw.CodeDraw;
import codedraw.Palette;
import codedraw.textformat.HorizontalAlign;
import codedraw.textformat.TextFormat;
import codedraw.textformat.VerticalAlign;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class Aufgabe1 {
    // global constants
    private static final int NUMBER_OF_TURNS = 10;
    private static final int CODE_LENGTH = 4;
    private static final int NUMBER_OF_COLUMNS = CODE_LENGTH * 2 + 1;
    private static final Color[] COLORS = new Color[]{Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.DARK_GRAY, Color.RED, Color.PINK, Color.YELLOW};

    // global variables
    private static int[][] playField = null;
    private static int[][] tips = null;
    private static int turn = 0;
    private static int pin = 0;
    private static int[] solution = null;

    // initializes all global variables for the game
    private static void initGame() {
        playField = new int[NUMBER_OF_TURNS][CODE_LENGTH];
        tips = new int[NUMBER_OF_TURNS][CODE_LENGTH]; // 1 == red; 2 == white
        turn = 0;
        pin = 0;
        solution = generateCode();
    }

    // generates array with length CODE_LENGTH and random numbers from 1 to COLORS.length
    private static int[] generateCode() {
        int[] randomColors = new int[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            double randomInt = Math.random() * 10;
            randomColors[i] = (int) randomInt;

            while (doesDuplicateExist(randomColors, i, (int) randomInt) || randomColors[i] == 0) {
                randomInt = Math.random() * 9;
                randomColors[i] = (int) randomInt;
            }
        }
        return randomColors;
    }

    private static boolean doesDuplicateExist(int[] randomColors, int index, int randomInt) {
        for (int i = 0; i < randomColors.length; i++) {
            if (randomColors[i] == randomInt && i != index) {
                return true;
            }
        }
        return false;
    }

    // calculates red and white tip pins
    private static void updateTips() {
        int count = 0;
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (playField[turn][i] == solution[i]) {
                tips[turn][count] = 1;
                count++;
            }
            if (sameColor(playField, solution, i)) {
                tips[turn][count] = 2;
                count++;
            }
        }
    }

    private static boolean sameColor(int[][] playField, int[] solution, int index) {
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (playField[turn][index] == solution[i] && i != index) {
                return true;
            }
        }
        return false;
    }

    // draws game to screen
    private static void drawGame(CodeDraw myDrawObj) {
        int height = 800;
        int width = height + height / (COLORS.length + 1);

        myDrawObj.setColor(Color.LIGHT_GRAY);
        myDrawObj.fillRectangle(0, 0, width, height);

        myDrawObj.setLineWidth(2);
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 4; col++) {
                myDrawObj.setColor(Color.white);
                myDrawObj.fillCircle(45 + col * 90, 40 + row * 80, 40);      //weiße Kreise
                myDrawObj.setColor(Color.black);
                myDrawObj.drawCircle(45 + col * 90, 40 + row * 80, 40);      //schwarze Umrandung
            }
        }

        for (int i = 0; i < COLORS.length; i++) {
            myDrawObj.setColor(COLORS[i]);
            myDrawObj.fillRectangle(800, i * 80, 80, 80);          //Farbenpalette rechts
        }

        myDrawObj.drawImage(800, 720, "./src/back_button.png");

        for (int row = 0; row < NUMBER_OF_TURNS; row++) {
            for (int col = 0; col < CODE_LENGTH; col++) {
                if(playField[row][col]!=0) {
                    myDrawObj.setColor(COLORS[playField[row][col] - 1]);
                    myDrawObj.fillCircle(45 + col * 90, 760 - row * 80, 40);
                }
            }
        }

        if (pin==4) {
            updateTips();
        }
        for (int row = 0; row < NUMBER_OF_TURNS; row++) {
            for (int col = 0; col < CODE_LENGTH; col++) {
                if (tips[row][col]==1) {
                 myDrawObj.setColor(Color.red);
                 myDrawObj.fillCircle(400+col*75,760-row*80,20);
                } else if (tips[row][col]==2) {
                    myDrawObj.setColor(Color.white);
                    myDrawObj.fillCircle(400+col*75,760-row*80,20);
                }
            }
        }


        myDrawObj.show();

    }


    private static void processGameStep(CodeDraw myDrawObj, MouseEvent me) {
        int[] clickPos = new int[2];
        clickPos[0] = me.getX();
        clickPos[1] = me.getY();

        int width = myDrawObj.getWidth();
        int height = myDrawObj.getHeight();

        // TODO: Implementieren Sie hier Ihre Lösung für die Methode

        if (clickPos[0] >= (width-80) && pin < 4) {
            if ((clickPos[1] / 80 + 1) == 10) {
                pin--;
                playField[turn][pin] = 0;
            } else if (!sameColor2(clickPos[1] / 80 + 1, pin)){     //falls doch true geschieht nichts
                    playField[turn][pin] = clickPos[1] / 80 + 1;
                    pin++;
            }
        } else {
            pin=0;
            turn++;
        }

        drawGame(myDrawObj);

        if (WON()) {
            myDrawObj.setColor(Color.lightGray);
            myDrawObj.fillRectangle(220,350,440,80);
            myDrawObj.setColor(Color.black);
            myDrawObj.drawRectangle(220,350,440,80);
            myDrawObj.setColor(Color.green);
            myDrawObj.drawText(380,380,"You WON!! Again?..");
            myDrawObj.show(5000);
            clearBoard(myDrawObj);
        } else if (turn==NUMBER_OF_TURNS-1 && pin==4) {
            myDrawObj.setColor(Color.lightGray);
            myDrawObj.fillRectangle(220,350,440,80);
            myDrawObj.setColor(Color.black);
            myDrawObj.drawRectangle(220,350,440,80);
            myDrawObj.setColor(Color.red);
            myDrawObj.drawText(380,380,"You LOST!! Try again...");
            myDrawObj.show(5000);
            clearBoard(myDrawObj);
        }

    }

    private static boolean sameColor2(int num,int index) {      //prüft ob farbe in einer reihe schon vorgekommen ist und gibt boolean zurück

        for (int i = 0; i < CODE_LENGTH; i++) {
            if (playField[turn][i] ==  num && i!=index) {
                return true;
            }
        }
        return false;
    }
    private static boolean WON() {
        for (int col = 0; col < CODE_LENGTH; col++) {
            if (playField[turn][col]!=solution[col]) {
                return false;
            }
        }
        return true;
    }

    // clears game board
    private static void clearBoard(CodeDraw myDrawObj) {
        drawGame(myDrawObj);
        for (int row = NUMBER_OF_TURNS-turn-1; row < NUMBER_OF_TURNS; row++) {        //row=NUMBER_...-turn-1, um nicht unnötigerweise von ganz oben zu starten
            for (int col = 0; col < CODE_LENGTH; col++) {
                myDrawObj.setColor(Color.white);
                myDrawObj.fillCircle(45 + col * 90, 40 + row * 80, 40);
                myDrawObj.setColor(Color.black);
                myDrawObj.drawCircle(45 + col * 90, 40 + row * 80, 40);
            }
            myDrawObj.setColor(Color.LIGHT_GRAY);
            myDrawObj.fillRectangle(380,20+row*80,390,40);
            myDrawObj.show(500);
        }
    }

    public static void main(String[] args) {

        int height = 800;
        int width = height + height / (COLORS.length + 1);

        CodeDraw myDrawObj = new CodeDraw(width, height);
        myDrawObj.setTitle("MasterMind Game");

        initGame();

        //for testing to print the solution
        System.out.println(Arrays.toString(solution));

        drawGame(myDrawObj);


        myDrawObj.onMouseClick(Aufgabe1::processGameStep);

    }
}



