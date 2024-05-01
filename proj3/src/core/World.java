package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    public static final int WIDTH = 90;
    public static final int HEIGHT = 50;
    private TETile[][] tiles;
    private static final int MIN_ROOM_WIDTH = 3;
    private static final int MIN_ROOM_HEIGHT = 3;
    private static final int MAX_ROOM_WIDTH = 15;
    private static final int MAX_ROOM_HEIGHT = 15;
    private Random random;
    private List<Room> roomsList;
    private List<int[]> suitableDoorPositions;
    private List<Point> coinLocation;
    private Point minigameCoorA;
    private Point minigameCoorB;
    private boolean playedGame;

    /**
     * Constructor for World.
     * Initializes the world grid.
     * Then generates rooms, hallways and connects them.
     *
     * @param seed The seed is a random number to ensure different worlds.
     */
    public World(long seed) {
        this.tiles = new TETile[WIDTH][HEIGHT];
        this.random = new Random(seed);
        this.roomsList = new ArrayList<>();
        this.coinLocation = new ArrayList<>();
        this.playedGame = false;
        createBase();
        generateRooms();
        connectRooms();
        createDoor();
        placeCoinInRoom();
        if (!playedGame) {
            createMiniGame();
        }
    }

    /**
     * Creates the world with empty tiles.
     * Size of base: X by Y.
     */
    private void createBase() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Generates a specified number of rooms.
     * Checks that rooms do not overlap with each other.
     * If rooms do not overlap, add to roomsList, a ArrayList.
     */
    private void generateRooms() {
        int numberOfRooms = RandomUtils.uniform(random, 200);

        for (int i = 0; i < numberOfRooms; i++) {
            int width = RandomUtils.uniform(random, MIN_ROOM_WIDTH, MAX_ROOM_WIDTH);
            int height = RandomUtils.uniform(random, MIN_ROOM_HEIGHT, MAX_ROOM_HEIGHT);
            int x = RandomUtils.uniform(random, 0, WIDTH - width);
            int y = RandomUtils.uniform(random, 0, HEIGHT - height);

            Room room = new Room(x, y, width, height);

            if (noOverlap(room)) {
                createRoom(room);
                roomsList.add(room);
            }
        }

        while (roomsList.size() < 3) {
            int width = RandomUtils.uniform(random, MIN_ROOM_WIDTH, MAX_ROOM_WIDTH);
            int height = RandomUtils.uniform(random, MIN_ROOM_HEIGHT, MAX_ROOM_HEIGHT);
            int x = RandomUtils.uniform(random, 0, WIDTH - width);
            int y = RandomUtils.uniform(random, 0, HEIGHT - height);

            Room room = new Room(x, y, width, height);

            if (noOverlap(room)) {
                createRoom(room);
                roomsList.add(room);
            }
        }
    }

    /**
     * Checks if the newly generated room overlaps with any of the existing rooms.
     *
     * @param newRoom The room to be checked for overlap.
     * @return 'true' if there is no overlap, 'false' otherwise.
     */
    private boolean noOverlap(Room newRoom) {
        for (Room otherRoom : roomsList) {
            if (newRoom.x - 1 < otherRoom.x + otherRoom.width + 1
                    && newRoom.x + newRoom.width + 1 > otherRoom.x - 1
                    && newRoom.y - 1 < otherRoom.y + otherRoom.height + 1
                    && newRoom.y + newRoom.height + 1 > otherRoom.y - 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a room in the world grid.
     * Then add walls to the rooms.
     *
     * @param room The room to be created.
     */
    private void createRoom(Room room) {
        for (int x = 0; x < room.width; x++) {
            for (int y = 0; y < room.height; y++) {
                int worldX = room.x + x;
                int worldY = room.y + y;
                if (tiles[worldX][worldY] != Tileset.WALL && tiles[worldX][worldY] != Tileset.FLOOR) {
                    tiles[worldX][worldY] = Tileset.FLOOR;
                }
            }
        }
        addWalls(room);
    }

    /**
     * Adds walls around a specified room.
     *
     * @param room The room around which walls are to be added.
     */
    private void addWalls(Room room) {
        for (int x = room.x - 1; x <= room.x + room.width; x++) {
            for (int y = room.y - 1; y <= room.y + room.height; y++) {
                if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                    if (tiles[x][y] == Tileset.NOTHING) {
                        tiles[x][y] = Tileset.WALL;
                    }
                    if (x == 0 || x == WIDTH - 1 || y == 0 || y == HEIGHT - 1) {
                        tiles[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    /**
     * Connects all rooms with hallways.
     * Iterates through the list of rooms.
     *  connects each room to the next one with hallways.
     */
    private void connectRooms() {
        for (int i = 0; i < roomsList.size() - 1; i++) {
            Room currentRoom = roomsList.get(i);
            Room nextRoom = roomsList.get(i + 1);
            createHallway(currentRoom, nextRoom);
        }
    }

    /**
     * Creates a hallway between two rooms.
     * The hallway is composed of a horizontal and a vertical part,
     * ensuring that it connects the center points of both rooms.
     *
     * @param a The first room to connect.
     * @param b The second room to connect.
     */
    private void createHallway(Room a, Room b) {
        int randomAX = random.nextInt(a.width - 1) + a.x + 1;
        int randomAY = random.nextInt(a.height - 1) + a.y + 1;
        int randomBX = random.nextInt(b.width - 1) + b.x + 1;
        int randomBY = random.nextInt(b.height - 1) + b.y + 1;

        // Check the boundaries for the horizontal part of the hallway
        if (randomAY >= 1 && randomAY < HEIGHT - 1) {
            for (int x = Math.min(randomAX, randomBX); x <= Math.max(randomAX, randomBX); x++) {
                if (x >= 1 && x < WIDTH - 1) {
                    tiles[x][randomAY] = Tileset.FLOOR;
                    if (tiles[x][randomAY - 1] != Tileset.FLOOR) {
                        tiles[x][randomAY - 1] = Tileset.WALL;
                    }
                    if (tiles[x][randomAY + 1] != Tileset.FLOOR) {
                        tiles[x][randomAY + 1] = Tileset.WALL;
                    }
                }
            }
        }

        // Check the boundaries for the vertical part of the hallway
        if (randomBX >= 1 && randomBX < WIDTH - 1) {
            for (int y = Math.min(randomAY, randomBY); y <= Math.max(randomAY, randomBY); y++) {
                if (y >= 1 && y < HEIGHT - 1) {
                    tiles[randomBX][y] = Tileset.FLOOR;
                    if (tiles[randomBX - 1][y] != Tileset.FLOOR) {
                        tiles[randomBX - 1][y] = Tileset.WALL;
                    }
                    if (tiles[randomBX + 1][y] != Tileset.FLOOR) {
                        tiles[randomBX + 1][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    /**
     * Creates a door in a random location in a wall of a room.
     * Get the first room from the roomList,
     * Then finds a suitable position in the room, which is a wall to place the door.
     *
     */
    private void createDoor() {
        if (!roomsList.isEmpty()) {
            Room firstRoom = roomsList.get(0);
            suitableDoorPositions = new ArrayList<>();

            // Top and bottom walls (excluding corners)
            for (int x = firstRoom.x; x < firstRoom.x + firstRoom.width; x++) {
                suitableDoorPositions.add(new int[]{x, firstRoom.y - 1}); // Top wall
                suitableDoorPositions.add(new int[]{x, firstRoom.y + firstRoom.height}); // Bottom wall
            }
            // Left and right walls (excluding corners)
            for (int y = firstRoom.y; y < firstRoom.y + firstRoom.height; y++) {
                suitableDoorPositions.add(new int[]{firstRoom.x - 1, y}); // Left wall
                suitableDoorPositions.add(new int[]{firstRoom.x + firstRoom.width, y}); // Right wall
            }

            // Filter out positions that are on the edge of the map or not adjacent to Tileset.NOTHING
            suitableDoorPositions.removeIf(pos -> !isSuitableForDoor(pos[0], pos[1]));

            if (!suitableDoorPositions.isEmpty()) {
                int[] doorPosition = suitableDoorPositions.get(random.nextInt(suitableDoorPositions.size()));
                tiles[doorPosition[0]][doorPosition[1]] = Tileset.LOCKED_DOOR;
            }
        }
    }
    /**
     * A helper method that checks if the location is suitable for a door.
     *
     * @param x The x-coordinate of the location to be checked.
     * @param y The y-coordinate of the location to be checked.
     */
    private boolean isSuitableForDoor(int x, int y) {
        if (x > 0 && x < WIDTH - 1 && y > 0 && y < HEIGHT - 1
                && tiles[x][y] == Tileset.WALL
                && (tiles[x - 1][y] == Tileset.NOTHING
                || tiles[x + 1][y] == Tileset.NOTHING
                || tiles[x][y - 1] == Tileset.NOTHING
                || tiles[x][y + 1] == Tileset.NOTHING)) {
            return true;
        }
        return false;
    }
    /**
     * A method that returns a list of room instances that is in our world.
     */
    public List<Room> getRooms() {
        return roomsList;
    }
    /**
     * Creates a MiniGame tile in the map.
     * finds the coordinate of a room, where in the corner the tile will be changed to miniGame,
     * Adds the coordinate of the minigame as an instance variable of Wrold as minigameCoorA and minigameCoorB.
     *
     */
    public void createMiniGame() {
        Room room = findSuitableRoomForMiniGame();
        if (room != null) {
            int xCor = room.x;
            int yCor = room.y;

            tiles[xCor + 2][yCor] = Tileset.FLOWER;
            tiles[xCor + 1][yCor] = Tileset.FLOWER;
            minigameCoorA = new Point(xCor + 2, yCor);
            minigameCoorB = new Point(xCor + 1, yCor);
        }
    }
    /**
     * Changes game tile back to a floor tile.
     * Called after minigame is played to change it back to floor, so minigame is only played once
     *
     */
    public void changeMiniGameToFloor() {
        tiles[(int) minigameCoorA.getX()][(int) minigameCoorA.getY()] = Tileset.FLOOR;
        tiles[(int) minigameCoorB.getX()][(int) minigameCoorB.getY()] = Tileset.FLOOR;
    }

    private Room findSuitableRoomForMiniGame() {
        List<Room> validRooms = new ArrayList<>();
        for (Room room : roomsList) {
            if (room.width >= 2 && room.height >= 1) {
                validRooms.add(room);
            }
        }
        if (!validRooms.isEmpty()) {
            return validRooms.get(random.nextInt(validRooms.size()));
        }
        return null;
    }

    private void placeCoin(Room room) {
        int x = random.nextInt(room.width) + room.x;
        int y = random.nextInt(room.height) + room.y;
        if (tiles[x][y] == Tileset.FLOOR && tiles[x][y] != Tileset.AVATAR) {
            tiles[x][y] = Tileset.COIN;
            coinLocation.add(new Point(x, y));
        }
    }

    private void placeCoinInRoom() {
        for (Room room : roomsList) {
            placeCoin(room);
        }
    }

    public void resetCoinLocations() {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                if (tiles[x][y] == Tileset.COIN) {
                    tiles[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    public List<Point> getCoinLocation() {
        return coinLocation;
    }

    public String getTileDescription(int x, int y) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            if (tiles[x][y] == Tileset.FLOOR) {
                return "Floor";
            } else if (tiles[x][y] == Tileset.WALL) {
                return "Wall";
            } else if (tiles[x][y] == Tileset.AVATAR) {
                return "You";
            } else if (tiles[x][y] == Tileset.COIN) {
                return "Coin";
            } else if (tiles[x][y] == Tileset.LOCKED_DOOR) {
                return "Door";
            } else if (tiles[x][y] == Tileset.FLOWER) {
                return  "Minigame";
            }
        }
        return "Nothing";
    }

    public void gamePlayed() {
        this.playedGame = true;
    }

    /**
     * Represents a room in the world.
     * Stores the position and dimensions of the room.
     */
    public class Room {
        int x, y, width, height;

        /**
         * Constructor for Room.
         *
         * @param x The x-coordinate of the room's top left corner.
         * @param y The y-coordinate of the room's top left corner.
         * @param width The width of the room.
         * @param height The height of the room.
         */
        public Room(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    /**
     * Provides access to the tile grid of the world.
     *
     * @return A 2D array of TETile representing the world.
     */
    public TETile[][] getTiles() {
        return tiles;
    }
}
