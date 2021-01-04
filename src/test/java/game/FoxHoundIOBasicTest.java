package game;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FoxHoundIOBasicTest {

    private static final char LOAD_ERROR = '#';
    private String[] defaultPlayers;

    @Before
    public void setup() {
        defaultPlayers = new String[]{"B1","D1","F1","H1","E8"};
    }

    // ATTENTION:
    // In order for some of this tests to work, you need to make sure
    // that the game files are located in the working directory or 
    // adapt the tests accordingly!

    // ------------------------- loadGame --------------------

    private void checkLoadedData(Path input, String[] expectedPlayers, char expectedFigure) {
        String[] players = defaultPlayers;
        char nextFigure = FoxHoundIO.loadGame(players, input);

        assertArrayEquals("Loaded player array not as expected.", expectedPlayers, players);

        assertEquals("Loaded next move not as expected.", expectedFigure, nextFigure);
    }
    

    @Test
    public void testLoadGameValidInput() {
        Path input = Paths.get("data/game01.txt");
        String[] expectedPlayers = {"C2", "D1", "F1", "H1", "D7"};
        char expectedFigure = FoxHoundUtils.FOX_FIELD;

        checkLoadedData(input, expectedPlayers, expectedFigure);


        input = Paths.get("data/game02.txt");
        expectedPlayers = new String[]{"B3", "E4", "C6", "E6", "D5"};
        expectedFigure = FoxHoundUtils.HOUND_FIELD;

        checkLoadedData(input, expectedPlayers, expectedFigure);


        input = Paths.get("data/game03.txt");
        expectedPlayers = new String[]{"B3", "D3", "F1", "H3", "A2"};
        expectedFigure = FoxHoundUtils.FOX_FIELD;

        checkLoadedData(input, expectedPlayers, expectedFigure);
    }

    @Test
    public void testLoadGameInvalidFileContent() {
        Path input = Paths.get("data/invalidGame01.txt");
        String[] expectedPlayers = defaultPlayers.clone();
        char expectedFigure = LOAD_ERROR;

        // invalid order
        checkLoadedData(input, expectedPlayers, expectedFigure);

        // invalid number of elements
        input = Paths.get("data/invalidGame02.txt");
        checkLoadedData(input, expectedPlayers, expectedFigure);

        // invalid number of lines
        input = Paths.get("data/invalidGame03.txt");
        checkLoadedData(input, expectedPlayers, expectedFigure);

        // invalid coordinate format
        input = Paths.get("data/invalidGame04.txt");
        checkLoadedData(input, expectedPlayers, expectedFigure);

        // invalid next move format
        input = Paths.get("data/invalidGame05.txt");
        checkLoadedData(input, expectedPlayers, expectedFigure);

        // coordinate out of range
        input = Paths.get("data/invalidGame06.txt");
        checkLoadedData(input, expectedPlayers, expectedFigure);
    }

    @Test
    public void testLoadGameInvalidFile() {
        Path input = Paths.get("path","to","unlikely","file.txt");
        String[] expectedPlayers = defaultPlayers.clone();
        char expectedFigure = LOAD_ERROR;

        // invalid File
        checkLoadedData(input, expectedPlayers, expectedFigure);
    }

    @Test(expected = NullPointerException.class)
    public void testLoadGamePathNull() {
        FoxHoundIO.loadGame(defaultPlayers, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadGamePlayersNonDefaultDim() {
        Path input = Paths.get("game01.txt");
        String[] players = {"B1", "D1", "F1", "H1", "J1", "E10"}; 
        FoxHoundIO.loadGame(players, input);
    }

    // ------------------------- saveGame --------------------

    private Path getTmpPath() throws IOException{
        try {
            File tmpFile = File.createTempFile("foxHoundGameSave", ".txt");
            Path tmpPath = tmpFile.toPath(); 
            // remove it again to avoid interference with saveGame test
            tmpFile.delete(); 
            return tmpPath;
        } catch(IOException e) {
            throw new IOException("Saving file test failed.", e);
        }
    }

    @Test
    public void testSaveGameValidInput() throws IOException{
        String[] expected = {"B3", "C2", "F1", "H3", "F3"};
        String[] players = expected.clone();
        char expectedMove = FoxHoundUtils.FOX_FIELD;
        char nextMove = expectedMove;
        Path saveFile = getTmpPath();

        boolean result = FoxHoundIO.saveGame(players, nextMove, saveFile);

        assertTrue("Save expected to be successful.", result);
        assertArrayEquals("Players array not expected to be modified.", expected, players);

        // check correct file content
        // NOTE: at this point you want to load the file content
        // using Java libraries and not another function to be implemented.
        // Since that would, however, reveal the solution, we are 
        // doing it this way.
        nextMove = FoxHoundIO.loadGame(players, saveFile);
        assertArrayEquals("Saved players array not as expected.", expected, players);
        assertEquals("Saved next move not as expected.", expectedMove, nextMove);
    }

    @Test
    public void testSaveGameInvalidPath() {
        String[] expected = defaultPlayers;
        String[] players = expected.clone();
        char nextMove = FoxHoundUtils.FOX_FIELD;
        Path saveFile = Paths.get("path","to","a","file","that","does","not","exist","game.txt");
        boolean result = FoxHoundIO.saveGame(players, nextMove, saveFile);

        assertFalse("Save not expected to be successful.", result);
        assertArrayEquals("Players array not expected to be modified.", expected, players);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveGamePathNull() {
        FoxHoundIO.saveGame(defaultPlayers, FoxHoundUtils.FOX_FIELD, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveGamePlayersNonDefaultDim() throws IOException{
        String[] players = {"B1", "D1", "F1", "H1", "J1", "E10"}; 
        char nextMove = FoxHoundUtils.FOX_FIELD;
        Path saveFile = getTmpPath();
        FoxHoundIO.saveGame(players, nextMove, saveFile);
    }
}
