package game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FoxHoundUtilsBasicTest {

    private String[] defaultPlayers;

    @Before
    public void setup() {
        defaultPlayers = new String[]{"B1","D1","F1","H1","E8"};
    }

    // ------------------------- initialisePositions --------------------

    @Test
    public void testInitialisePositionsDefaultDim() {
        int dimension = FoxHoundUtils.DEFAULT_DIM;
        String[] expected = defaultPlayers;

        String[] result = FoxHoundUtils.initialisePositions(dimension);
        
        assertNotNull("Returned position array not expected to be null.", result);
        
        assertArrayEquals("Returned positions not as expected.", expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialisePositionsNegativeDim() {
        int dimension = -5;
        FoxHoundUtils.initialisePositions(dimension);
    }

    // ------------------------- isFoxWin -------------------------------

    @Test
    public void testIsFoxWinPositive() {
        String foxPos = "B1";
        boolean result = FoxHoundUtils.isFoxWin(foxPos);

        assertTrue("Fox is expected to win in position: " + foxPos, result);
    }

    @Test
    public void testIsFoxWinNegative() {
        String foxPos = "A2";
        boolean result = FoxHoundUtils.isFoxWin(foxPos);

        assertFalse("Fox is not expected to win in position: " + foxPos, result);

        foxPos = "E8";
        result = FoxHoundUtils.isFoxWin(foxPos);

        assertFalse("Fox is not expected to win in position: " + foxPos, result);
    }

    @Test(expected = NullPointerException.class)
    public void testIsFoxWinNull() {
        FoxHoundUtils.isFoxWin(null);
    }

    // ------------------------- isHoundWin -------------------------------

    @Test
    public void testIsHoundWinPositive() {
        String[] players = {"C4","E4","C6","E6","D5"};
        int dimension = FoxHoundUtils.DEFAULT_DIM;

        boolean result = FoxHoundUtils.isHoundWin(players, dimension);

        assertTrue("Hounds are expected to win in given positions.", result);
    }

    @Test
    public void testIsHoundWinNegative() {
        String[] players = {"B3","E4","C6","E6","D5"};
        int dimension = FoxHoundUtils.DEFAULT_DIM;

        boolean result = FoxHoundUtils.isHoundWin(players, dimension);

        assertFalse("Hounds are not expected to win in given positions.", result);

        result = FoxHoundUtils.isHoundWin(defaultPlayers, dimension);

        assertFalse("Hounds are not expected to win in given positions.", result);
    }

    @Test(expected = NullPointerException.class)
    public void testIsHoundWinPNull() {
        FoxHoundUtils.isHoundWin(null, FoxHoundUtils.DEFAULT_DIM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsHoundWinDInvalid() {
        FoxHoundUtils.isHoundWin(defaultPlayers, -5);
    }

    // ------------------------- isValidMove -------------------------------

    @Test
    public void testIsValidMovePositive() {

        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = players[players.length - 1];
        String dest = "D7"; // move fox up

        boolean result = FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);

        assertTrue("Move is expected to be valid.", result);

        players = new String[]{"B1","D1","F1","H1","E8"};
        figure = FoxHoundUtils.HOUND_FIELD;
        origin = players[1];
        dest = "E2"; // move hound down

        result = FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);

        assertTrue("Move is expected to be valid.", result);

        players = new String[]{"B1","D1","F1","H1","D5"};
        figure = FoxHoundUtils.FOX_FIELD;
        origin = players[players.length - 1];
        dest = "C6"; // move fox down

        result = FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);

        assertTrue("Move is expected to be valid.", result);
    }

    @Test
    public void testIsValidMoveInvalidFigureAtOrigin() {

        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = players[0]; // select a hound
        String dest = "A2";

        boolean result = FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);

        assertFalse("Move is expected to be invalid if the " + 
            "figure in the origin does not match the given figure type.", result);

        players = new String[]{"B1","D1","F1","H1","E8"};
        figure = FoxHoundUtils.HOUND_FIELD;
        origin = players[players.length - 1]; // select the fox
        dest = "D7";

        result = FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);

        assertFalse("Move is expected to be invalid if the " + 
            "figure in the origin does not match the given figure type.", result);
    }

    @Test
    public void testIsValidMoveEmptyOrigin() {

        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        char figure = FoxHoundUtils.FOX_FIELD;
        String origin = "C4";
        String dest = "B3";

        boolean result = FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);

        assertFalse("Move is expected to be invalid if the " + 
            "given origin is empty.", result);
    }

    @Test
    public void testIsValidMoveDestOccupied() {

        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        players[1] = "C2";
        char figure = FoxHoundUtils.HOUND_FIELD;
        String origin = "B1";
        String dest = "C2";

        boolean result = FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);

        assertFalse("Move is expected to be invalid if the " + 
            "given destination is already occupied.", result);

        players = new String[]{"B1","D1","F1","H1","C2"};
        figure = FoxHoundUtils.FOX_FIELD;
        origin = "C2";
        dest = "B1";

        result = FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);

        assertFalse("Move is expected to be invalid if the " + 
            "given destination is already occupied.", result);

        players = new String[]{"B1","D1","F1","H1","C2"};
        figure = FoxHoundUtils.HOUND_FIELD;
        origin = "B1";
        dest = "C2";

        result = FoxHoundUtils.isValidMove(dim, players, figure, origin, dest);

        assertFalse("Move is expected to be invalid if the " + 
            "given destination is already occupied.", result);
    }

}
