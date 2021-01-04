package game;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class FoxHoundUtilsAdvancedTest {

    private static final boolean DEBUG = false;

    private String[] defaultPlayers;

    @Before
    public void setup() {
        defaultPlayers = new String[] { "B1", "D1", "F1", "H1", "E8" };
    }

    // ------------------------- initialisePositions --------------------

    /**
     * Set hound coordinates and allocate one extra for the fox which is kept at
     * null.
     * 
     * @param dimension dimension of the board
     * @return string array with hound positions in board coordinates and one extra
     *         field for the fox
     */
    private String[] setHounds(int dimension) {
        String[] hounds = new String[dimension / 2 + 1];
        char column = 'B';
        for (int i = 0; i < hounds.length - 1; i++) {
            hounds[i] = column + "1";
            column += 2;
        }
        return hounds;
    }

    private void setFox(String[] players, int dim) {
        int foxStartCol = (dim / 2) + (dim % 2) - ((dim / 2) % 2);
        players[players.length - 1] = (char) ('A' + foxStartCol) + Integer.toString(dim);
    }

    private void checkInitPositions(int dimension) {
        String[] expected = setHounds(dimension);
        setFox(expected, dimension);

        String[] result = FoxHoundUtils.initialisePositions(dimension);

        if (DEBUG) {
            System.out.println("=========================");
            System.out.println("E: " + Arrays.toString(expected));
            System.out.println("A: " + Arrays.toString(result));
        }
        assertNotNull("Returned position array not expected to be null.", result);

        assertArrayEquals("Returned positions not as expected for dimension " + dimension, expected, result);
    }

    @Test
    public void testInitialisePositionsMinField() {
        checkInitPositions(FoxHoundUtils.MIN_DIM);
    }

    @Test
    public void testInitialisePositionsMaxField() {
        checkInitPositions(FoxHoundUtils.MAX_DIM);
    }

    @Test
    public void testInitialisePositionsOddDimEvenHalf() {
        for (int dim = FoxHoundUtils.MIN_DIM; dim < FoxHoundUtils.MAX_DIM; dim++) {
            if (dim % 2 != 0 && (dim / 2) % 2 == 0)
                checkInitPositions(dim);
        }
    }

    @Test
    public void testInitialisePositionsOddDimOddHalf() {
        for (int dim = FoxHoundUtils.MIN_DIM; dim < FoxHoundUtils.MAX_DIM; dim++) {
            if (dim % 2 != 0 && (dim / 2) % 2 != 0)
                checkInitPositions(dim);
        }
    }

    @Test
    public void testInitialisePositionsEvenDimEvenHalf() {
        for (int dim = FoxHoundUtils.MIN_DIM; dim < FoxHoundUtils.MAX_DIM; dim++) {
            if (dim % 2 == 0 && (dim / 2) % 2 == 0)
                checkInitPositions(dim);
        }
    }

    @Test
    public void testInitialisePositionsEvenDimOddHalf() {
        for (int dim = FoxHoundUtils.MIN_DIM; dim < FoxHoundUtils.MAX_DIM; dim++) {
            if (dim % 2 == 0 && (dim / 2) % 2 != 0)
                checkInitPositions(dim);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialisePositionsTooSmallDim() {
        int dimension = 2;
        String[] result = FoxHoundUtils.initialisePositions(dimension);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialisePositionsZeroDim() {
        int dimension = 0;
        String[] result = FoxHoundUtils.initialisePositions(dimension);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialisePositionsTooLargeDim() {
        int dimension = FoxHoundUtils.MAX_DIM + 10;
        String[] result = FoxHoundUtils.initialisePositions(dimension);
    }

    // ------------------------- isFoxWin -------------------------------

    @Test
    public void testIsFoxWinPositiveNonDefaultDim() {
        String foxPos = "J1";
        boolean result = FoxHoundUtils.isFoxWin(foxPos);

        assertTrue("Fox is expected to win in position: " + foxPos, result);
    }

    @Test
    public void testIsFoxWinNegativeNonDefaultDim() {
        String foxPos = "L21";
        boolean result = FoxHoundUtils.isFoxWin(foxPos);

        assertFalse("Fox is not expected to win in position: " + foxPos, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsFoxWinInvalidBoardCoord1() {
        String foxPos = "invalid";
        FoxHoundUtils.isFoxWin(foxPos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsFoxWinInvalidBoardCoord2() {
        String foxPos = "235";
        FoxHoundUtils.isFoxWin(foxPos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsFoxWinInvalidBoardCoord3() {
        String foxPos = "XX1";
        FoxHoundUtils.isFoxWin(foxPos);
    }

    // ------------------------- isHoundWin -------------------------------

    @Test
    public void testIsHoundWinPositiveWallBlockLeft() {
        int dimension = FoxHoundUtils.DEFAULT_DIM;
        /*
         * .H... F.... .H...
         */
        defaultPlayers[1] = "B3";
        defaultPlayers[4] = "A2";

        boolean result = FoxHoundUtils.isHoundWin(defaultPlayers, dimension);

        assertTrue("Hounds are expected to win in given positions: " + Arrays.toString(defaultPlayers), result);
    }

    @Test
    public void testIsHoundWinPositiveWallBlockRight() {
        int dimension = FoxHoundUtils.DEFAULT_DIM;
        /*
         * ...H. ....F ...H.
         */
        defaultPlayers[3] = "G2";
        defaultPlayers[2] = "G4";
        defaultPlayers[4] = "H3";

        boolean result = FoxHoundUtils.isHoundWin(defaultPlayers, dimension);

        assertTrue("Hounds are expected to win in given positions: " + Arrays.toString(defaultPlayers), result);

    }

    @Test
    public void testIsHoundWinPositiveWallBlockBottom() {
        int dimension = FoxHoundUtils.DEFAULT_DIM;
        /*
         * ..... .H.H. ..F..
         */
        defaultPlayers[1] = "D7";
        defaultPlayers[2] = "F7";

        boolean result = FoxHoundUtils.isHoundWin(defaultPlayers, dimension);

        assertTrue("Hounds are expected to win in given positions: " + Arrays.toString(defaultPlayers), result);
    }

    @Test
    public void testIsHoundWinPositiveCornerBlockBottomLeft() {
        int dimension = FoxHoundUtils.DEFAULT_DIM;
        /*
         * ..... .H... F....
         */
        defaultPlayers[1] = "B7";
        defaultPlayers[4] = "A8";

        boolean result = FoxHoundUtils.isHoundWin(defaultPlayers, dimension);

        assertTrue("Hounds are expected to win in given positions: " + Arrays.toString(defaultPlayers), result);
    }

    @Test
    public void testIsHoundWinPositiveNonDefaultDim() {
        String[] players = { "C8", "E8", "G8", "C10", "E10", "D9" };
        int dimension = 10;

        boolean result = FoxHoundUtils.isHoundWin(players, dimension);

        assertTrue("Hounds are expected to win in given positions: " + Arrays.toString(players), result);
    }

    @Test
    public void testIsHoundWinNegativeNonDefaultDim() {
        String[] players = { "B1", "D1", "F1", "H1", "J1", "E10" };
        int dimension = 10;

        boolean result = FoxHoundUtils.isHoundWin(players, dimension);

        assertFalse("Hounds are not expected to win in given positions: " + Arrays.toString(players), result);
    }

    @Test(expected = NullPointerException.class)
    public void testIsHoundWinInvalidBoardCoord1() {
        defaultPlayers[0] = null;
        int dimension = FoxHoundUtils.DEFAULT_DIM;

        FoxHoundUtils.isHoundWin(defaultPlayers, dimension);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsHoundWinInvalidBoardCoord2() {
        defaultPlayers[0] = "invalid";
        int dimension = FoxHoundUtils.DEFAULT_DIM;

        FoxHoundUtils.isHoundWin(defaultPlayers, dimension);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsHoundWinInvalidBoardCoord3() {
        defaultPlayers[0] = "235";
        int dimension = FoxHoundUtils.DEFAULT_DIM;

        FoxHoundUtils.isHoundWin(defaultPlayers, dimension);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsHoundWinInvalidBoardCoord4() {
        defaultPlayers[0] = "XX1";
        int dimension = FoxHoundUtils.DEFAULT_DIM;

        FoxHoundUtils.isHoundWin(defaultPlayers, dimension);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsHoundWinInvalidBoardSetup() {
        int dimension = 5;

        FoxHoundUtils.isHoundWin(defaultPlayers, dimension);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsHoundWinInvalidDimension1() {
        int dimension = 2;
        FoxHoundUtils.isHoundWin(defaultPlayers, dimension);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsHoundWinInvalidDimension2() {
        int dimension = 0;
        FoxHoundUtils.isHoundWin(defaultPlayers, dimension);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsHoundWinInvalidDimension3() {
        int dimension = FoxHoundUtils.MAX_DIM + 10;
        FoxHoundUtils.isHoundWin(defaultPlayers, dimension);
    }

    // ------------------------- isValidMove -------------------------------

    private void checkMove(int dim, String[] players, char figure, String origin, String dest, boolean expected) {

        boolean result = FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);

        String message = "Move (" + origin + " " + dest + ") is expected to be " + (expected ? "" : "in")
                + "valid for setup: " + Arrays.toString(players);

        if (expected)
            assertTrue(message, result);
        else
            assertFalse(message, result);
    }

    @Test
    public void testIsValidMovePositiveNonDefaultDimension() {
        int dim = 10;
        String[] players = { "B1", "D1", "F1", "H1", "J1", "E10" };
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = players[players.length - 1];
        String dest = "F9"; // move fox up

        checkMove(dim, players, figure, origin, dest, true);

        players = new String[] { "B1", "D1", "F1", "H1", "J1", "E10" };
        figure = FoxHoundUtils.HOUND_FIELD;
        origin = players[4];
        dest = "I2"; // move hound down

        checkMove(dim, players, figure, origin, dest, true);

        players = new String[] { "B1", "D1", "F1", "H1", "J1", "J9" };
        figure = FoxHoundUtils.FOX_FIELD;
        origin = players[players.length - 1];
        dest = "I10"; // move fox down

        checkMove(dim, players, figure, origin, dest, true);
    }

    @Test
    public void testIsValidMoveDestTooFar() {

        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = players[players.length - 1];
        String dest = "C6"; // move fox too far

        checkMove(dim, players, figure, origin, dest, false);

        players = new String[] { "B1", "D1", "F1", "H1", "E8" };
        figure = FoxHoundUtils.HOUND_FIELD;
        origin = players[1];
        dest = "F3"; // move hound down too far

        checkMove(dim, players, figure, origin, dest, false);

        players = new String[] { "B1", "D1", "F1", "H1", "D5" };
        figure = FoxHoundUtils.FOX_FIELD;
        origin = players[players.length - 1];
        dest = "B7"; // move fox down too far

        checkMove(dim, players, figure, origin, dest, false);
    }

    @Test
    public void testIsValidMoveHoundBackwards() {

        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        players[0] = "B3";
        char figure = FoxHoundUtils.HOUND_FIELD;
        String origin = players[0];
        String dest = "A2"; // move hound backwards

        checkMove(dim, players, figure, origin, dest, false);
    }

    @Test
    public void testIsValidMoveSameOriginAndDest() {

        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = players[players.length - 1];
        String dest = origin;

        checkMove(dim, players, figure, origin, dest, false);
    }

    @Test(expected = NullPointerException.class)
    public void testIsValidMovePlayersNull() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = null;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = NullPointerException.class)
    public void testIsValidMovePlayersInvalidBoardCoord1() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        players[1] = null;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMovePlayersInvalidBoardCoord2() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        players[1] = "invalid";
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMovePlayersInvalidBoardCoord3() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        players[1] = "235";
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMovePlayersInvalidBoardCoord4() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        players[1] = "XX1";
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = NullPointerException.class)
    public void testIsValidMoveOriginNull() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = null;
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveOriginInvalidBoardCoord1() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "invalid";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveOriginInvalidBoardCoord2() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "235";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveOriginInvalidBoardCoord3() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "XX1";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = NullPointerException.class)
    public void testIsValidMoveDestNull() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = null;

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveDestInvalidBoardCoord1() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "invalid";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveDestInvalidBoardCoord2() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "235";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveDestInvalidBoardCoord3() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "XX1";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveInvalidBoardSetup() {
        int dim = 5;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveInvalidDimension1() {
        int dim = 2;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveInvalidDimension2() {
        int dim = 0;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveInvalidDimension3() {
        int dim = -1;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveInvalidDimension4() {
        int dim = FoxHoundUtils.MAX_DIM + 10;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "E8";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMoveInvalidFigure() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = 'x';
        String origin = "E8";
        String dest = "D7";

        FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);
    }

}
