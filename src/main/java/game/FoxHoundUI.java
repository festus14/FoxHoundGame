package game;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class FoxHoundUI {

    /** Number of main menu entries. */
    private static final int MENU_ENTRIES = 4;
    /** Main menu display string. */
    private static final String MAIN_MENU = "\n1. Move\n2. Save Game\n3. Load Game\n4. Exit\n\nEnter 1 - 4:";

    /** Menu entry to select a move action. */
    public static final int MENU_MOVE = 1;
    /** Menu entry to terminate the program. */

    public static final int MENU_SAVE_GAME = 2;

    public static final int MENU_LOAD_GAME = 3;

    public static final int MENU_EXIT = 4;

    public static void displayBoardFancy(String[] players, int dim) {
        // Initial spaces
        if (dim > 9) {
            System.out.print("   ");
        } else {
            System.out.print("  ");
        }

        // Print the alphabets
        for (int i = 65; i < 65 + dim; i++) {
            System.out.print("  " + (char) i + " ");
        }

        // Trailing spaces
        System.out.println("");
        System.out.print("   ");

        // Print the "="
        for (int i = 0; i < dim * 4; i++) {
            System.out.print("=");
        }
        System.out.println("");

        for (int i = 1; i <= dim; i++) {
            // Check if 0 should be appended to coordintes
            String append = "";
            if (dim > 9 && i < 10) {
                append = "0";
            }

            // Prints the coordinate
            System.out.print(append + i + " ");

            // Print the fox or hound position
            for (int k = 1; k <= dim; k++) {
                String pos = (char) (k + 64) + "" + i;
                boolean exist = false;
                boolean isFox = false;
                for (int j = 0; j < players.length; j++) {
                    if (pos.equals(players[j])) {
                        exist = true;
                        if (j == players.length - 1) {
                            isFox = true;
                        }
                        break;
                    }
                }
                if (exist) {
                    if (isFox) {
                        System.out.print("| F ");
                    } else {
                        System.out.print("| H ");
                    }
                } else {
                    System.out.print("|   ");
                }
            }
            System.out.print("| " + append + i);
            if (i != dim) {
                System.out.println("");
                if (dim > 9) {
                    System.out.print("   ");
                } else {
                    System.out.print("  ");
                }
                for (int z = 0; z < dim * 4; z++) {
                    System.out.print("=");
                }
                System.out.println("");
            }
        }

        System.out.print("\n   ");
        for (int i = 0; i < dim * 4; i++) {
            System.out.print("=");
        }
        System.out.println("");

        if (dim > 9) {
            System.out.print("   ");
        } else {
            System.out.print("  ");
        }
        for (int i = 65; i < 65 + dim; i++) {
            System.out.print("  " + (char) i + " ");
        }
        System.out.println("\n");

    }

    public static void displayBoard(String[] players, int dim) {
        if (dim > 9) {
            System.out.print("   ");
        } else {
            System.out.print("  ");
        }
        for (int i = 65; i < 65 + dim; i++) {
            System.out.print((char) i);
        }
        System.out.println("\n");

        for (int i = 1; i <= dim; i++) {
            String append = "";
            if (dim > 9 && i < 10) {
                append = "0";
            }
            System.out.print(append + i + " ");
            for (int k = 1; k <= dim; k++) {
                String pos = (char) (k + 64) + "" + i;
                boolean exist = false;
                boolean isFox = false;
                for (int j = 0; j < players.length; j++) {
                    if (pos.equals(players[j])) {
                        exist = true;
                        if (j == players.length - 1) {
                            isFox = true;
                        }
                        break;
                    }
                }
                if (exist) {
                    if (isFox) {
                        System.out.print('F');
                    } else {
                        System.out.print('H');
                    }
                } else {
                    System.out.print('.');
                }
            }
            System.out.print(" " + append + i);
            System.out.println("\n");
        }

        if (dim > 9) {
            System.out.print("   ");
        } else {
            System.out.print("  ");
        }
        for (int i = 65; i < 65 + dim; i++) {
            System.out.print((char) i);
        }
        System.out.println("\n");
    }

    public static int mainMenuQuery(char figureToMove, Scanner stdin) {
        Objects.requireNonNull(stdin, "Given Scanner must not be null");
        if (figureToMove != FoxHoundUtils.FOX_FIELD && figureToMove != FoxHoundUtils.HOUND_FIELD) {
            throw new IllegalArgumentException("Given figure field invalid: " + figureToMove);
        }

        String nextFigure = figureToMove == FoxHoundUtils.FOX_FIELD ? "Fox" : "Hounds";

        int input = -1;
        while (input == -1) {
            System.out.println(nextFigure + " to move");
            System.out.println(MAIN_MENU);

            boolean validInput = false;
            if (stdin.hasNextInt()) {
                input = stdin.nextInt();
                validInput = input > 0 && input <= MENU_ENTRIES;
            }

            if (!validInput) {
                System.out.println("Please enter valid number.");
                input = -1; // reset input variable
            }

            stdin.nextLine(); // throw away the rest of the line
        }

        return input;
    }

    public static String[] positionQuery(int dim, Scanner stdin) {
        String positions[] = new String[2];
        System.out.print("Enter player initial position: ");
        positions[0] = stdin.nextLine();

        System.out.print("Enter player new position: ");
        positions[1] = stdin.nextLine();

        try {
            char bChar = positions[0].charAt(0);
            int bAscii = (int) bChar;
            int bNum = Integer.parseInt(positions[0].substring(1));

            char aChar = positions[1].charAt(0);
            int aAscii = (int) aChar;
            int aNum = Integer.parseInt(positions[1].substring(1));

            if ((bAscii > dim + 65) || (bAscii < 65) || (bNum > dim) || (bNum <= 0) || (aAscii > dim + 65)
                    || (aAscii < 65) || (aNum > dim) || (aNum <= 0)) {
                System.out.println("ERROR: Invalid move. Try again!");
                positionQuery(dim, stdin);
            }

        } catch (Exception e) {
            System.err.println(e);
            System.out.println("ERROR: Invalid move. Try again!");
            positionQuery(dim, stdin);
        }

        return positions;
    }

    public static Path fileQuery(Scanner stdin) {
        System.out.println("Enter the path where game is to be saved or loaded from: ");
        String input = stdin.nextLine();

        Path inputPath = Paths.get("data/" + input);
        Path fullPath = inputPath.toAbsolutePath();
        return fullPath;
    }
}
