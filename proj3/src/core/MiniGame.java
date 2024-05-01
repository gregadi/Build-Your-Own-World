package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.Random;

public class MiniGame {
    private final TERenderer ter;
    private static final int  WIDTH = 90;
    private static final int HEIGHT = 50;
    private static final int GAME_WIDTH = 30;
    private static final int GAME_HEIGHT = 15;
    private TETile[][] board;
    private Random random;
    private Player player;
    private final long gameDuration = 15000;

    public MiniGame(long seed) {
        ter = new TERenderer();
        random = new Random(seed);
        board = new TETile[WIDTH][HEIGHT];
        createBase();
        createRoom();
        addWalls();
        placeCoins();
    }

    private void createBase() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = Tileset.WATER;
            }
        }
    }
    private void createRoom() {
        for (int x = 30; x <= 30 + GAME_WIDTH; x++) {
            for (int y = 1; y <= GAME_HEIGHT; y++) {
                board[x][y] = Tileset.FLOOR;
            }
        }
    }
    private void addWalls() {
        for (int x = 29; x <= 30 + GAME_WIDTH + 1; x++) {
            for (int y = 0; y <= GAME_HEIGHT + 1; y++) {
                if (board[x][y] == Tileset.WATER) {
                    board[x][y] = Tileset.SAND;
                }
            }
        }
    }
    private void placeCoins() {
        int coins = 0;
        while (coins < 10) {
            int x = random.nextInt(GAME_WIDTH) + 30;
            int y = random.nextInt(GAME_HEIGHT) + 1;
            if (board[x][y] != Tileset.COIN && board[x][y] == Tileset.FLOOR) {
                board[x][y] = Tileset.COIN;
                coins++;
            }
        }
    }

    public void start() {
        ter.initialize(WIDTH, HEIGHT);
        renderIntroduction();

        long startTime = System.currentTimeMillis();
        long endGameTime = startTime + 10000;
        long endTime = startTime + gameDuration;
        initializePlayer();
        ter.drawTiles(board);

        while (System.currentTimeMillis() < endTime) {
            renderScore();

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                handlePlayerMovement(key);
            }
            ter.drawTiles(board);
        }
        renderGameOver();
    }

    public void initializePlayer() {
        int startX = 35;
        int startY = 8;
        player = new Player(board, startX, startY);
    }

    public int getCoinScore() {
        return player.getNumCoins();
    }

    private void handlePlayerMovement(char key) {
        if (key == 'a') {
            player.moveLeft();
        } else if (key == 'd') {
            player.moveRight();
        } else if (key == 'w') {
            player.moveUp();
        } else if (key == 's') {
            player.moveDown();
        }
    }
    private void renderScore() {
        Font font = new Font("Normal", Font.BOLD, 16);
        StdDraw.setPenColor(255, 255, 255);
        StdDraw.setFont(font);
        StdDraw.text(80, 48, "Coins collected: " + player.getNumCoins());
        StdDraw.show();
    }

    private void renderIntroduction() {
        Font font = new Font("Normal", Font.BOLD, 32);
        StdDraw.setPenColor(255, 255, 255);
        StdDraw.setFont(font);

        long startTime = System.currentTimeMillis();
        long endTimeFirstMessage = startTime + 3000;
        long endTimeSecondMessage = endTimeFirstMessage + 3000;

        while (System.currentTimeMillis() < endTimeSecondMessage) {
            if (System.currentTimeMillis() < endTimeFirstMessage) {
                StdDraw.text(45, 40, "You have 10 Seconds to collect all the coins!");
            } else {
                StdDraw.text(45, 30, "Good Luck!");
            }
            StdDraw.show();
        }

    }
    private void renderGameOver() {
        Font font = new Font("Normal", Font.PLAIN, 60);
        StdDraw.setFont(font);
        StdDraw.setPenColor(255, 255, 255);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + 3000;
        while (System.currentTimeMillis() < endTime) {
            if (player.getNumCoins() == 10) {
                StdDraw.text(45, 40, "Good Job :) you collected all the coins! Amazing!");
            } else {
                StdDraw.text(45, 40, "Oh no :( you didn't collect all the coins");
            }
            StdDraw.show();
        }
    }
}
