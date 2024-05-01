package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(90, 50);
        prompt();
        String userInput = getUserInput();

        if (userInput.equals("LOAD_GAME")) {
            Game loadedGame = new Game(0, true);
            loadedGame.runGame();
        } else if (userInput.equals("q")) {
            System.exit(0);
        } else if (userInput.equals("")) {
            Game newGame = new Game(10, false);
            newGame.runGame();
        } else {
            Long seed = Long.parseLong(userInput);
            Game newGame = new Game(seed, false);
            newGame.runGame();
        }
    }

    /**
     * Creates a prompt for a user input
     * The user will type a seed number
     * That will be used for generating the world
     *
     */
    private static void prompt() {
        StdDraw.setPenColor(255, 255, 255);
        Font fontTitle = new Font("Sans Serif", Font.BOLD, 60);
        Font textFont = new Font("Sans Serif", Font.PLAIN, 20);
        StdDraw.setFont(textFont);
        StdDraw.text(45, 30, "Enter a Seed number (N****S) : ");
        StdDraw.text(44, 28, "Load Game (L)");
        StdDraw.text(45, 26, "Quit (Q)");
        StdDraw.setFont(fontTitle);
        StdDraw.text(42, 40, "CS 61B: THA GAME");
        StdDraw.show();
    }

    /**
     * Helper method to help obtaining user input
     *Returns the string of the user input
     */
    private static String getUserInput() {
        StringBuilder input = new StringBuilder();
        double xCord = 55.0;
        double yCord = 30;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                Font textFont = new Font("Sans Serif", Font.PLAIN, 20);
                StdDraw.setFont(textFont);
                //StdDraw.text(xCord, yCord, String.valueOf(key));
                key = Character.toLowerCase(key);
                //StdDraw.show();
                if (key == 's') {
                    break;
                } else if (key == 'l') {
                    return "LOAD_GAME";
                } else if (key == 'q') {
                    return "q";
                } else if (key != 'n') {
                    input.append(key);
                    StdDraw.text(xCord, yCord, String.valueOf(key));
                    StdDraw.show();
                }
                xCord += 0.75;
            }
        }
        return input.toString();
    }

}
