package game;

import java.util.Arrays;

public class FoxHoundUtils {

    /** Default dimension of the game board in case none is specified. */
    public static final int DEFAULT_DIM = 8;
    /** Minimum possible dimension of the game board. */
    public static final int MIN_DIM = 4;
    /** Maximum possible dimension of the game board. */
    public static final int MAX_DIM = 26;

    /** Symbol to represent a hound figure. */
    public static final char HOUND_FIELD = 'H';
    /** Symbol to represent the fox figure. */
    public static final char FOX_FIELD = 'F';

    public static String[] initialisePositions(int dimension) {
        // Check if dmension is valid
        if (dimension < 4 || dimension > 26)
            dimension = 8;

        // Calculate the number of players
        int houndsNum = Math.round(dimension / 2);
        String[] initialPosition = new String[houndsNum + 1];

        int a = 66;

        // Generate array of strings of initial positions
        for (int j = 0; j < initialPosition.length; j++) {
            if (j == initialPosition.length - 1) {
                if (dimension % 2 == 0) {
                    int pos = houndsNum + 65;
                    if (pos % 2 == 0) {
                        initialPosition[j] = (char) (pos + 1) + "" + dimension;
                    } else {
                        initialPosition[j] = (char) (pos) + "" + dimension;
                    }
                } else {
                    double pos = Math.floor(dimension / 2) + 65;
                    if (pos % 2 == 0) {
                        initialPosition[j] = (char) (pos) + "" + dimension;
                    } else {
                        initialPosition[j] = (char) (pos + 1) + "" + dimension;
                    }
                }
                break;
            }

            initialPosition[j] = (char) (a) + "1";
            a += 2;
        }

        return initialPosition;
    }

    public static boolean isValidMove(int dim, String[] players, char fh, String before, String after) {
        char bChar = before.charAt(0);
        int bAscii = (int) bChar;
        int bNum = Integer.parseInt(before.substring(1));

        char aChar = after.charAt(0);
        int aAscii = (int) aChar;
        int aNum = Integer.parseInt(after.substring(1));

        if ((bAscii > dim + 65) || (bAscii < 65) || (bNum > dim) || (bNum <= 0) || (aAscii > dim + 65) || (aAscii < 65)
                || (aNum > dim) || (aNum <= 0) || (!Arrays.asList(players).contains(before))
                || (Arrays.asList(players).contains(after))) {
            return false;
        }

        switch (fh) {
            case 'F':
                if ((!before.equals(players[players.length - 1])) || (aAscii != bAscii + 1 && aAscii != bAscii - 1)
                        || (aAscii == bAscii) || (aNum == bNum) || (aNum != bNum + 1 && aNum != bNum - 1)) {
                    return false;
                }
                break;
            case 'H':
                if ((before.equals(players[players.length - 1])) || (aAscii != bAscii + 1 && aAscii != bAscii - 1)
                        || (aAscii == bAscii) || (aNum == bNum) || (aNum != bNum + 1)) {
                    return false;
                }
                break;
            default:
                return false;
        }

        return true;
    }

    public static boolean isHoundWin(String[] players, int dim) {
        String before = players[players.length - 1];

        char bChar = before.charAt(0);
        int bAscii = (int) bChar;
        int bNum = Integer.parseInt(before.substring(1));

        String topLeft = (char) (bAscii - 1) + "" + (bNum - 1);
        String topRight = (char) (bAscii + 1) + "" + (bNum - 1);
        String bottomRight = (char) (bAscii + 1) + "" + (bNum + 1);
        String bottomLeft = (char) (bAscii - 1) + "" + bNum + 1;

        if ((bAscii - 1 < 65) || (bNum - 1 <= 0)) {
            topLeft = before;
        }

        if ((bAscii + 1 > 65 + dim) || (bNum - 1 <= 0)) {
            topRight = before;
        }

        if ((bAscii + 1 > 65 + dim) || (bNum + 1 <= 0)) {
            bottomRight = before;
        }

        if ((bAscii - 1 > 65 + dim) || (bNum + 1 <= 0)) {
            bottomLeft = before;
        }

        if ((Arrays.asList(players).contains(topLeft)) && (Arrays.asList(players).contains(topRight))
                && (Arrays.asList(players).contains(bottomRight)) && (Arrays.asList(players).contains(bottomLeft))) {
            return true;
        }

        return false;
    }

    public static boolean isFoxWin(String foxPosition) {
        int posNum = Integer.parseInt(foxPosition.substring(1));
        if (posNum == 1) {
            return true;
        }
        return false;
    }

    public static int searchKey(String[] keys, String key) {
        int flag = 0;
        for (String theKey : keys) {
            if (theKey.equals(key)) {
                return flag;
            }
            flag++;
        }
        return -1;
        // helper function to find the position of same keys exist, return first same
        // keys's position

    }
}
