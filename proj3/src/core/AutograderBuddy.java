package core;

import tileengine.TETile;
import tileengine.Tileset;

public class AutograderBuddy {

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {
        Game game;
        System.out.println("Before any parsing input is:" +  input);
        if (input.startsWith("l")) {
            game = new Game(0, true);
        } else {
            // Extract seed
            String seedStr = input.replaceAll("[^0-9]", ""); // Remove non-numeric characters
            System.out.println("This is the seed " + seedStr);
            if (seedStr.isEmpty()) {
                //throw new IllegalArgumentException("Invalid input: Seed is missing.");
                game = new Game(10, false);
            } else {
                long seed = Long.parseLong(seedStr);
                game = new Game(seed, false);
            }
        }
        System.out.println("This is the input after getting seed " +  input);
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            c = Character.toLowerCase(c);
            if (c == 's' || c == 'l') {
                for (int j = i + 1; j < input.length(); j++) {
                    char command = input.charAt(j);
                    if (command == ':') {
                        if (game != null) {
                            game.saveGame();
                        }
                        break;
                    }
                    processCommand(game, command);
                }
                break;
            }
        }
        return game.getWorld();
    }

    private static void processCommand(Game game, char command) {
        command = Character.toLowerCase(command);
        switch (command) {
            case 'w':
                game.getPlayer().moveUp();
                break;
            case 'a':
                game.getPlayer().moveLeft();
                break;
            case 's':
                game.getPlayer().moveDown();
                break;
            case 'd':
                game.getPlayer().moveRight();
                break;
            default:
                System.out.println("Unrecognized command: '" + command + "'");
                break;
        }
    }


    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character()
                || t.character() == Tileset.COIN.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
