package core;

import tileengine.TETile;
import tileengine.Tileset;

public class Player {
    private final TETile[][] world;
    int xPos;
    int yPos;
    private static final TETile ICON = Tileset.AVATAR;
    private boolean reachDoor;
    private int numCoins;
    private boolean triggerMinigame = false;


    Player(TETile[][] world, int xPos, int yPos) {
        this.world = world;
        this.xPos = xPos;
        this.yPos = yPos;
        world[xPos][yPos] = ICON;
        reachDoor = false;
    }

    public void move(int newX, int newY) {
        world[xPos][yPos] = Tileset.FLOOR;
        xPos = newX;
        yPos = newY;
        if (!reachDoor && world[newX][newY] != Tileset.LOCKED_DOOR) {
            world[xPos][yPos] = ICON;
        }
    }

    public void moveDown() {
        int newX = xPos;
        int newY = yPos - 1;
        if (!reachDoor && yPos > 0 && canMove(0, -1)) {
            if (isMiniGame(newX, newY)) {
                triggerMinigame = true;
            } else {
                checkForCoin(newX, newY);
                move(newX, newY);
            }
        }
    }

    public void moveUp() {
        int newX = xPos;
        int newY = yPos + 1;
        if (!reachDoor && yPos < World.HEIGHT - 1 && canMove(0, 1)) {
            if (isMiniGame(newX, newY)) {
                triggerMinigame = true;
            } else {
                checkForCoin(newX, newY);
                move(newX, newY);
            }
        }
    }

    public void moveLeft() {
        int newX = xPos - 1;
        int newY = yPos;
        if (!reachDoor && xPos > 0 && canMove(-1, 0)) {
            if (isMiniGame(newX, newY)) {
                triggerMinigame = true;
            } else {
                checkForCoin(newX, newY);
                move(newX, newY);
            }
        }
    }

    public void moveRight() {
        int newX = xPos + 1;
        int newY = yPos;
        if (!reachDoor && xPos < World.WIDTH - 1 && canMove(1, 0)) {
            if (isMiniGame(newX, newY)) {
                triggerMinigame = true;
            } else {
                checkForCoin(newX, newY);
                move(newX, newY);
            }
        }
    }

    public boolean canMove(int deltaX, int deltaY) {
        if (this.world[this.xPos + deltaX][this.yPos + deltaY] != Tileset.WALL
                && this.world[this.xPos + deltaX][this.yPos + deltaY] != Tileset.SAND) {
            return true;
        }
        return false;
    }

    public void hasReachDoor() {
        if (world[xPos][yPos] == Tileset.LOCKED_DOOR) {
            reachDoor = true;
        }
    }

    public boolean reachDoor() {
        return this.reachDoor;
    }

    private void checkForCoin(int newX, int newY) {
        if (world[newX][newY] == Tileset.COIN) {
            numCoins++;
        }
    }

    public int getNumCoins() {
        return numCoins;
    }

    public void setNumCoins(int numCoins) {
        this.numCoins = numCoins;
    }
    public boolean startMiniGame() {
        return triggerMinigame;
    }

    private boolean isMiniGame(int newX, int newY) {
        return world[newX][newY] == Tileset.FLOWER;
    }

    public void resetTriggerMiniGame() {
        triggerMinigame = false;
    }

    public void addCoinsToNumCoins(int coins) {
        this.numCoins += coins;
    }

}
