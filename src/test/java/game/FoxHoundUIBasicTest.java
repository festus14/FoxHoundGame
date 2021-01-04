package game;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Paths;
import java.util.Scanner;
import java.nio.file.Path;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoxHoundUIBasicTest {

    private boolean stdCapStarted;
    private OutputStream tmpStdOut;
    private OutputStream tmpStdErr;
    private PrintStream originalStdOut;
    private PrintStream originalStdErr;


    private String[] defaultPlayers;
    private String defaultOutput;

    private static final String POS_QUERY_MSG = 
        "Provide origin and destination coordinates.\n" + 
        "Enter two positions between A1-H8:";
    private static final String POS_QUERY_ERROR = 
        "ERROR: Please enter valid coordinate pair separated by space.";
    private static final String VALID_MOVE = "E8 F7";
    private static final String INVALID_MOVE = "124 asd";

    private static final String FILE_QUERY_MSG = "Enter file path:";


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

    // ------------------------- displayBoard --------------------

    @Test
    public void testDisplayBoard() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] players = defaultPlayers;
        String expected = defaultOutput;

        stdCaptureStart();
        try {
            try {
                FoxHoundUI.displayBoard(players, dim);
            } catch (Exception e) {
                System.err.println("Error executing displayBoard " + 
                    " for console output check:" + e);
            }
            // ignore leading and trailing white spaces
            assertEquals("Console output not as expected in displayBoard.", 
                expected.replaceAll("\r","").trim(), getCapturedStdOut().replaceAll("\r","").trim());
        } finally {
            stdCaptureStop();
        } 
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
    public void testPositionQueryMessage() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String expectedStdOut = POS_QUERY_MSG;
        // no error message expected
        String expectedStdErr = ""; 
        String inputLine = VALID_MOVE + "\n";

        checkPosQueryOutput(dim, expectedStdOut, expectedStdErr, inputLine);
    }

    @Test
    public void testPositionQueryError() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        // repeated message expected due to invalid input
        String expectedStdOut = POS_QUERY_MSG + "\r\n\r\n" + POS_QUERY_MSG;
        String expectedStdErr = POS_QUERY_ERROR;
        // enter invalid move first, then valid move
        String inputLine = INVALID_MOVE + "\n" + VALID_MOVE + "\n";

        checkPosQueryOutput(dim, expectedStdOut, expectedStdErr, inputLine);
    }

    @Test
    public void testPositionQueryReturn() {
        int dim = FoxHoundUtils.DEFAULT_DIM;
        String[] expected = {"E8","D7"};
        String inputLine = expected[0] + " " + expected[1] + "\n";
        Scanner TEST_IN = new Scanner(new ByteArrayInputStream(inputLine.getBytes()));
        
        String[] result = FoxHoundUI.positionQuery(dim, TEST_IN);
        TEST_IN.close();

        assertArrayEquals("Query return coordinates not as expected.", expected, result);
    }

    // ------------------------- fileQuery --------------------

    private void checkFileQueryOutput(String expectedStdOut, String expectedStdErr, String inputLine) {
        Scanner TEST_IN = new Scanner(new ByteArrayInputStream(inputLine.getBytes()));

        stdCaptureStart();
        try {
            try {
                FoxHoundUI.fileQuery(TEST_IN);
            } catch (Exception e) {
                fail("Error executing fileQuery " + 
                    "for console output check:" + e);
            }
            // ignore leading and trailing white spaces
            assertEquals("Console output stdout not as expected in fileQuery: " + inputLine, 
                expectedStdOut.replace("\r","").trim(), getCapturedStdOut().replaceAll("\r","").trim());
            assertEquals("Console output stderr not as expected in fileQuery: " + inputLine,
                expectedStdErr.replace("\r","").trim(), getCapturedStdErr().replaceAll("\r","").trim());
        } finally {
            stdCaptureStop();
        }
        TEST_IN.close();
    }

    @Test
    public void testFileQueryMessage() {
        String expectedStdOut = FILE_QUERY_MSG;
        // no error message expected
        String expectedStdErr = ""; 
        String inputLine = Paths.get("path","to","file","game01.txt").toString();

        checkFileQueryOutput(expectedStdOut, expectedStdErr, inputLine);
    }

    @Test
    public void testFileQueryReturn() {
        String inputLine = Paths.get("path","to","file","game01.txt").toString();
        String expected = inputLine;
        Scanner TEST_IN = new Scanner(new ByteArrayInputStream(inputLine.getBytes()));
        Path result = FoxHoundUI.fileQuery(TEST_IN);
        TEST_IN.close();

        assertEquals("Resulting path not as expected.", expected, result.toString());
    }
}
