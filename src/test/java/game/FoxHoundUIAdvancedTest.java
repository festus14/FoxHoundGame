package game;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Scanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoxHoundUIAdvancedTest {

    private String[] defaultPlayers;
    private String defaultOutput;

    private boolean stdCapStarted;
    private OutputStream tmpStdOut;
    private OutputStream tmpStdErr;
    private PrintStream originalStdOut;
    private PrintStream originalStdErr;

    public static final char COLUMN_START_COORD = 'A';

    private static final String POS_QUERY_FORMAT = 
        "Provide origin and destination coordinates.\n" + 
        "Enter two positions between A1-%s%d:";
    private static final String POS_QUERY_ERROR = 
        "ERROR: Please enter valid coordinate pair separated by space.";

    /**
     * Reroute standard output and standard error into temporary output streams.
     * @throws RuntimeException if capturing has already started.
     */
    private void stdCaptureStart() {
        if (stdCapStarted)
            throw new RuntimeException("Capture needs to be stopped before it can be started");

        stdCapStarted = true;
        tmpStdOut = new ByteArrayOutputStream();
        originalStdOut = System.out;
        System.setOut(new PrintStream(tmpStdOut));

        tmpStdErr = new ByteArrayOutputStream();
        originalStdErr = System.err;
        System.setErr(new PrintStream(tmpStdErr));
    }

    /**
     * Reset the standard output and standard error to their original streams.
     * @throws RuntimeException if capturing has not yet started.
     */
    private void stdCaptureStop() {
        if (!stdCapStarted)
            throw new RuntimeException("Capture needs to be started before it can be stopped");

        stdCapStarted = false;
        System.setOut(originalStdOut);
        originalStdOut = null;
        tmpStdOut = null;

        System.setErr(originalStdErr);
        originalStdErr = null;
        tmpStdErr = null;
    }

    /**
     * Return the result of the captured standard output.
     * @return the standard output since rerouting started.
     * @throws RuntimeException if capturing is not activated.
     */
    private String getCapturedStdOut() {
        if (tmpStdOut == null)
            throw new RuntimeException("Nothing captured.");

        return tmpStdOut.toString();
    }

    /**
     * Return the result of the captured standard error.
     * @return the standard error since rerouting started.
     * @throws RuntimeException if capturing is not activated.
     */
    private String getCapturedStdErr() {
        if (tmpStdErr == null)
            throw new RuntimeException("Nothing captured.");

        return tmpStdErr.toString();
    }

    @Before
    public void setup() {
        defaultPlayers = new String[]{"B1","D1","F1","H1","E8"};
        defaultOutput =
        "  ABCDEFGH  \n" +
        "\n" +
        "1 .H.H.H.H 1\n" +
        "2 ........ 2\n" +
        "3 ........ 3\n" +
        "4 ........ 4\n" +
        "5 ........ 5\n" +
        "6 ........ 6\n" +
        "7 ........ 7\n" +
        "8 ....F... 8\n" +
        "\n" +
        "  ABCDEFGH  ";
    }    

    // ------------------------- displayBoard --------------------

    private void checkDisplay(String[] players, int dim, String expected) {
        stdCaptureStart();
        try {
            try {
                FoxHoundUI.displayBoard(players, dim);
            } catch (Exception e) {
                fail("Error executing displayBoard " + 
                    " for console output check:" + e);
            }
            // ignore leading and trailing white spaces
            assertEquals("Console output not as expected in displayBoard.", 
                expected.replaceAll("\r","").trim(), getCapturedStdOut().replaceAll("\r","").trim());
        } finally {
            stdCaptureStop();
        } 
    }

    @Test
    public void testDisplayBoardSmallDim() {
        int dim = 5;
        String[] players = {"B1", "D1", "D5"};
        String expected =
            "  ABCDE  \n" +
            "\n" +
            "1 .H.H. 1\n" +
            "2 ..... 2\n" +
            "3 ..... 3\n" +
            "4 ..... 4\n" +
            "5 ...F. 5\n" +
            "\n" +
            "  ABCDE  ";

        checkDisplay(players, dim, expected);
    }

    @Test
    public void testDisplayBoardLargeDim() {
        int dim = 9;
        String[] players = {"B1", "D1", "F1", "H1", "F9"};
        String expected =
            "  ABCDEFGHI  \n" +
            "\n" +
            "1 .H.H.H.H. 1\n" +
            "2 ......... 2\n" +
            "3 ......... 3\n" +
            "4 ......... 4\n" +
            "5 ......... 5\n" +
            "6 ......... 6\n" +
            "7 ......... 7\n" +
            "8 ......... 8\n" +
            "9 .....F... 9\n" +
            "\n" +
            "  ABCDEFGHI  ";

        checkDisplay(players, dim, expected);
    }

    @Test
    public void testDisplayBoardLeadingZero() {
        int dim = 10;
        String[] players = {"B1", "D1", "F1", "H1", "J1", "E10"};
        String expected =
            "   ABCDEFGHIJ   \n" +
            "\n" +
            "01 .H.H.H.H.H 01\n" +
            "02 .......... 02\n" +
            "03 .......... 03\n" +
            "04 .......... 04\n" +
            "05 .......... 05\n" +
            "06 .......... 06\n" +
            "07 .......... 07\n" +
            "08 .......... 08\n" +
            "09 .......... 09\n" +
            "10 ....F..... 10\n" +
            "\n" +
            "   ABCDEFGHIJ   ";      

        checkDisplay(players, dim, expected);
    }

    @Test(expected = NullPointerException.class)
    public void testDisplayBoardPlayersNull() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = null;

        FoxHoundUI.displayBoard(players, dim);
    }

    @Test(expected = NullPointerException.class)
    public void testDisplayBoardPlayersInvalidBoardCoord1() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        players[1] = null;

        FoxHoundUI.displayBoard(players, dim);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisplayBoardPlayersInvalidBoardCoord2() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        players[1] = "invalid";

        FoxHoundUI.displayBoard(players, dim);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisplayBoardPlayersInvalidBoardCoord3() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        players[1] = "235";

        FoxHoundUI.displayBoard(players, dim);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisplayBoardPlayersInvalidBoardCoord4() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        players[1] = "XX1";

        FoxHoundUI.displayBoard(players, dim);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisplayBoardInvalidBoardSetup() {
        int dim = 5;
        String[] players = defaultPlayers;
 
        FoxHoundUI.displayBoard(players, dim);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisplayBoardInvalidDimension1() {
        int dim = 2;
        String[] players = defaultPlayers;
 
        FoxHoundUI.displayBoard(players, dim);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisplayBoardInvalidDimension2() {
        int dim = 0;
        String[] players = defaultPlayers;
 
        FoxHoundUI.displayBoard(players, dim);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisplayBoardInvalidDimension3() {
        int dim = -1;
        String[] players = defaultPlayers;
 
        FoxHoundUI.displayBoard(players, dim);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisplayBoardInvalidDimension4() {
        int dim = FoxHoundUtils.MAX_DIM + 10;
        String[] players = defaultPlayers;
 
        FoxHoundUI.displayBoard(players, dim);
    }

    // ------------------------- positionQuery --------------------

    private void checkPosQueryOutput(int dim, String expectedStdOut, String expectedStdErr, String inputLine) {
        Scanner TEST_IN = new Scanner(new ByteArrayInputStream(inputLine.getBytes()));

        stdCaptureStart();
        try {
            try {
                FoxHoundUI.positionQuery(dim, TEST_IN);
            } catch (Exception e) {
                fail("Error executing positionQuery " + 
                    "for console output check:" + e);
            }
            // ignore leading and trailing white spaces
            // and use unix line endings
            assertEquals("Console output stdout not as expected in positionQuery: " + inputLine,
                    expectedStdOut.trim().replaceAll("\r", ""), getCapturedStdOut().trim().replaceAll("\r", ""));
            assertEquals("Console output stderr not as expected in positionQuery: " + inputLine,
                    expectedStdErr.trim().replaceAll("\r", ""), getCapturedStdErr().trim().replaceAll("\r", ""));

        } finally {
            stdCaptureStop();
        }
        TEST_IN.close();
    }



    @Test
    public void testPositionQueryInvalidInput1() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String end = "" + (char)(COLUMN_START_COORD + dim - 1);
        // repeated message expected due to invalid input
        String msg = String.format(POS_QUERY_FORMAT, end, dim);
        String expectedStdOut = msg + "\n\n" + msg;
        String expectedStdErr = POS_QUERY_ERROR;
        // enter invalid move first, then valid move
        String inputLine = "E1 abs" + "\n" + "E8 F7" + "\n";

        checkPosQueryOutput(dim, expectedStdOut, expectedStdErr, inputLine);
    }

    @Test
    public void testPositionQueryInvalidInput2() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String end = "" + (char)(COLUMN_START_COORD + dim - 1);
        // repeated message expected due to invalid input
        String msg = String.format(POS_QUERY_FORMAT, end, dim);
        String expectedStdOut = msg + "\n\n" + msg;
        String expectedStdErr = POS_QUERY_ERROR;
        // enter invalid move first, then valid move
        String inputLine = "E1 F9" + "\n" + "E8 F7" + "\n";

        checkPosQueryOutput(dim, expectedStdOut, expectedStdErr, inputLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPositionQueryInvalidDimension1() {
        int dim = 2;
        String inputLine = "E8 D7\n";
        Scanner TEST_IN = new Scanner(new ByteArrayInputStream(inputLine.getBytes()));
        
        FoxHoundUI.positionQuery(dim, TEST_IN);
        TEST_IN.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPositionQueryInvalidDimension2() {
        int dim = 0;
        String inputLine = "E8 D7\n";
        Scanner TEST_IN = new Scanner(new ByteArrayInputStream(inputLine.getBytes()));
        
        FoxHoundUI.positionQuery(dim, TEST_IN);
        TEST_IN.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPositionQueryInvalidDimension3() {
        int dim = -1;
        String inputLine = "E8 D7\n";
        Scanner TEST_IN = new Scanner(new ByteArrayInputStream(inputLine.getBytes()));
        
        FoxHoundUI.positionQuery(dim, TEST_IN);
        TEST_IN.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPositionQueryInvalidDimension4() {
        int dim = FoxHoundUtils.MAX_DIM + 10;
        String inputLine = "E8 D7\n";
        Scanner TEST_IN = new Scanner(new ByteArrayInputStream(inputLine.getBytes()));
        
        FoxHoundUI.positionQuery(dim, TEST_IN);
        TEST_IN.close();
    }

    @Test(expected = NullPointerException.class)
    public void testPositionQueryScannerNull() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String inputLine = "E8 D7\n";
        
        FoxHoundUI.positionQuery(dim, null);
    }

    @Test
    public void testPositionQueryLargeBoardMessage() {
        int dim = FoxHoundUtils.MAX_DIM;
        String end = "" + (char)(COLUMN_START_COORD + dim - 1);
        String expectedStdOut = String.format(POS_QUERY_FORMAT, end, dim);
        String expectedStdErr = "";
        String inputLine = "F1 Z25\n";

        checkPosQueryOutput(dim, expectedStdOut, expectedStdErr, inputLine);
    }

    @Test
    public void testPositionQuerySmallBoardMessage() {
        int dim = FoxHoundUtils.MIN_DIM;
        String end = "" + (char)(COLUMN_START_COORD + dim - 1);
        String expectedStdOut = String.format(POS_QUERY_FORMAT, end, dim);
        String expectedStdErr = "";
        String inputLine = "B1 A2\n";

        checkPosQueryOutput(dim, expectedStdOut, expectedStdErr, inputLine);
    }

    // ------------------------- fileQuery --------------------

    @Test(expected = NullPointerException.class)
    public void testFileQueryScannerNull() {
        FoxHoundUI.fileQuery(null);
    }

}
