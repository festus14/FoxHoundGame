package game;

import java.nio.file.Path;
import java.util.Scanner;

public class FoxHoundGame {

    private static final Scanner STDIN_SCAN = new Scanner(System.in);

    private static char swapPlayers(char currentTurn) {
        if (currentTurn == FoxHoundUtils.FOX_FIELD) {
            return FoxHoundUtils.HOUND_FIELD;
        } else {
            return FoxHoundUtils.FOX_FIELD;
        }
    }

    private static void gameLoop(int dim, String[] players) {

        char turn = FoxHoundUtils.FOX_FIELD;
        boolean exit = false;

        while (!exit) {
            System.out.println("\n#################################");
            FoxHoundUI.displayBoard(players, dim);
            // Print the board

            int choice = FoxHoundUI.mainMenuQuery(turn, STDIN_SCAN);

            switch (choice) {
                case FoxHoundUI.MENU_MOVE:
                    while (true) {
                        String[] step = FoxHoundUI.positionQuery(dim, STDIN_SCAN);
                        // Get the input step
                        String before = step[0];
                        String after = step[1];

                        if (FoxHoundUtils.isValidMove(dim, players, turn, before, after)) {
                            players[FoxHoundUtils.searchKey(players, before)] = after;
                            // Change original player to new one
                            break;
                        } else {
                            System.out.println("Invalid move");
                        }
                    }
                    turn = swapPlayers(turn);

                    if (FoxHoundUtils.isFoxWin(players[players.length - 1])) {
                        System.out.println("The Fox wins!");
                        exit = true;
                    }
                    if (FoxHoundUtils.isHoundWin(players, dim)) {
                        System.out.println("The Hound wins!");
                        exit = true;
                    }
                    // Check if any players win
                    break;

                case FoxHoundUI.MENU_SAVE_GAME:
                    Path path = FoxHoundUI.fileQuery(STDIN_SCAN);

                    boolean isSaved = FoxHoundIO.saveGame(players, turn, path);
                    if (isSaved == false) {
                        System.out.println("ERROR: Saving file failed.");
                    }
                    break;
                case FoxHoundUI.MENU_LOAD_GAME:
                    Path paths = FoxHoundUI.fileQuery(STDIN_SCAN);

                    char next = FoxHoundIO.loadGame(players, paths);
                    if (next == '#') {
                        System.out.println("ERROR: Loading from file failed.");
                    }else{
                        turn = next;
                    }
                    break;
                case FoxHoundUI.MENU_EXIT:
                    exit = true;
                    break;

                default:
                    System.err.println("ERROR: invalid menu choice: " + choice);
            }
        }
    }

    public static void main(String[] args) {
        // Request for dimension from console.
        Scanner dimObj = new Scanner(System.in); // Create a Scanner object
        System.out.println("Enter board dimension");

        int dimension = dimObj.nextInt();
        if (dimension < 4 || dimension > 26) {
            dimension = 8;
            System.out.println("Dimension entered is invalid, default value will be used: " + dimension);
        } else
            System.out.println("Dimension entered is: " + dimension);

        String[] players = FoxHoundUtils.initialisePositions(dimension);
        // Initialise player
        gameLoop(dimension, players);
        dimObj.close();
        STDIN_SCAN.close();
    }
}
