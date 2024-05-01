package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;
import tileengine.TERenderer;
import utils.FileUtils;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Game {
    private boolean isGameOver;
    TETile[][] world;
    private World worldObj;
    private final TERenderer ter = new TERenderer();
    private Player player;
    private static final String SAVE_FILE = "save_game.txt";
    private long seed;
    private List<Character> keyList;

    public Game(long seed, boolean loadSaveGame) {
        keyList = new ArrayList<Character>();
        this.seed = seed;
        this.isGameOver = false;
        if (loadSaveGame) {
            loadGame();
        } else {
            initializeGame();
        }
    }

    private void initializeGame() {
        worldObj = new World(seed);
        world = worldObj.getTiles();
        Random random = new Random(seed);
        World.Room startingRoom = worldObj.getRooms().get(0);
        int x = 0;
        int y = 0;
        boolean validPosition = false;

        while (!validPosition) {
            // Choose a position within the starting room
            x = random.nextInt(startingRoom.width) + startingRoom.x;
            y = random.nextInt(startingRoom.height) + startingRoom.y;

            if (world[x][y] != Tileset.WALL) {
                validPosition = true;
            }
        }

        player = new Player(world, x, y);
    }


    private boolean isGameOver() {
        if (player == null) {
            return false;
        }
        return player.reachDoor();
    }

    private void renderBoard() {
        ter.drawTiles(world);
    }

    public void runGame() {
        MiniGame miniGameInstance = new MiniGame(seed);
        int miniGameCoins = 0;

        while (!isGameOver()) {
            updatePlayer();
            renderBoard();
            renderHud();
            renderScore();
            renderDateAndTime();
            StdDraw.show();

            if (player.startMiniGame()) {
                miniGameInstance.start();
                player.resetTriggerMiniGame();
                worldObj.changeMiniGameToFloor();
                worldObj.gamePlayed();
                miniGameCoins = miniGameInstance.getCoinScore();
                player.addCoinsToNumCoins(miniGameCoins);
                renderBoard();
            }
        }
        renderGameOver();
    }

    private void renderScore() {
        Font font = new Font("Sans Sarif", Font.BOLD, 16);
        StdDraw.setPenColor(255, 255, 255);
        StdDraw.setFont(font);
        StdDraw.text(80, 49, "Coins collected: " + player.getNumCoins());
    }

    private void renderHud() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        String description = worldObj.getTileDescription(mouseX, mouseY);
        Font font = new Font("Normal", Font.PLAIN, 16);
        StdDraw.setFont(font);
        StdDraw.setPenColor(255, 255, 255);
        StdDraw.text(5, 49, "Tile: " + description);
    }

    private void renderGameOver() {
        Font font = new Font("Normal", Font.PLAIN, 60);
        StdDraw.setFont(font);
        StdDraw.setPenColor(255, 255, 255);
        StdDraw.text(45, 30, "Game Over");
        StdDraw.text(45, 20, "Number of Coins collected: " + this.player.getNumCoins());
        StdDraw.show();
    }

    private void updatePlayer() {
        while (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();
            key = Character.toLowerCase(key);
            keyList.add(key);
            if (key == 'a') {
                player.moveLeft();
            } else if (key == 'd') {
                player.moveRight();
            } else if (key == 'w') {
                player.moveUp();
            } else if (key == 's') {
                player.moveDown();
            } else if (key == 'q') {
                System.out.println("test1");
                char prevKey = keyList.get(keyList.size() - 2);
                if (prevKey == ':') {
                    saveGame();
                    System.exit(0);
                }
            }
            player.hasReachDoor();
            if (isGameOver()) {
                break;
            }
        }
    }

    public TETile[][] getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    private String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    private void renderDateAndTime() {
        Font font = new Font("Normal", Font.BOLD, 16);
        String dateTimeString = getCurrentDateTime();
        StdDraw.setPenColor(255, 255, 255);
        StdDraw.setFont(font);
        StdDraw.text(15, 49, dateTimeString);
    }

    public void saveGame() {
        StringBuilder saveData = new StringBuilder();
        saveData.append(this.seed).append(",");
        saveData.append(this.player.xPos).append(",");
        saveData.append(this.player.yPos).append(",");
        saveData.append(this.player.getNumCoins()).append(",");
        for (Point coinLocation : worldObj.getCoinLocation()) {
            if (world[coinLocation.x][coinLocation.y] == Tileset.COIN) {
                saveData.append(coinLocation.x).append("-").append(coinLocation.y).append(";");
            }
        }

        FileUtils.writeFile(SAVE_FILE, saveData.toString());
    }

    public void loadGame() {
        if (FileUtils.fileExists(SAVE_FILE)) {
            String savedData = FileUtils.readFile(SAVE_FILE);
            String[] parts = savedData.split(",");
            if (parts.length >= 4) {
                this.seed = Long.parseLong(parts[0]);
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int numCoins = Integer.parseInt(parts[3]);

                this.worldObj = new World(this.seed);
                this.world = worldObj.getTiles();
                this.player = new Player(world, x, y);
                this.player.setNumCoins(numCoins);
                worldObj.resetCoinLocations();

                if (parts.length > 4) {
                    String[] coinLocations = parts[4].split(";");
                    for (String loc : coinLocations) {
                        String[] xy = loc.split("-");
                        int coinX = Integer.parseInt(xy[0]);
                        int coinY = Integer.parseInt(xy[1]);
                        world[coinX][coinY] = Tileset.COIN;
                    }
                }
            }
        }
    }
}
