package game;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FoxHoundIOAdvancedTest {

    private static final char LOAD_ERROR = '#';
    private String[] defaultPlayers;

    @Before
    public void setup() {
        defaultPlayers = new String[]{"B1","D1","F1","H1","E8"};
    }

    // ------------------------- loadGame --------------------

    private void checkLoadedData(Path input, String[] expectedPlayers, char expectedFigure) {
        String[] players = defaultPlayers;
        char nextFigure = FoxHoundIO.loadGame(players, input);

        assertArrayEquals("Loaded player array not as expected.", expectedPlayers, players);

        assertEquals("Loaded next move not as expected.", expectedFigure, nextFigure);
    }

    @Test
    public void testLoadGameFileIsFolder() {
        Path input = Paths.get("testFolder");
        String[] expectedPlayers = defaultPlayers.clone();
        char expectedFigure = LOAD_ERROR;

        // invalid File
        checkLoadedData(input, expectedPlayers, expectedFigure);
    }

    @Test(expected = NullPointerException.class)
    public void testLoadGamePlayersNull() {
        Path input = Paths.get("data/game01.txt");

        FoxHoundIO.loadGame(null, input);    
    }

    @Test(expected = NullPointerException.class)
    public void testLoadGameInvalidBoardCoord1() {
        Path input = Paths.get("data/game01.txt");
        String[] players = defaultPlayers;
        players[0] = null;

        FoxHoundIO.loadGame(players, input);    
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadGameInvalidBoardCoord2() {
        Path input = Paths.get("data/game01.txt");
        String[] players = defaultPlayers;
        players[0] = "invalid";

        FoxHoundIO.loadGame(players, input);    
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadGameInvalidBoardCoord3() {
        Path input = Paths.get("data/game01.txt");
        String[] players = defaultPlayers;
        players[0] = "235";

        FoxHoundIO.loadGame(players, input);    
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadGameInvalidBoardCoord4() {
        Path input = Paths.get("data/game01.txt");
        String[] players = defaultPlayers;
        players[0] = "XX1";

        FoxHoundIO.loadGame(players, input);    
    }

    // ------------------------- saveGame --------------------

    private Path getTmpPath() {
        try {
            File tmpFile = File.createTempFile("foxHoundGameSave", ".txt");
            Path tmpPath = tmpFile.toPath(); 
            // remove it again to avoid interference with saveGame test
            tmpFile.delete(); 
            return tmpPath;
        } catch(IOException e) {
            System.out.println("Saving file test failed.");
            return null;
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSaveGamePlayersNull() {
        Path saveFile = getTmpPath();
        char nextMove = FoxHoundUtils.FOX_FIELD;
        String[] players = null;

        FoxHoundIO.saveGame(players, nextMove, saveFile);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveGameInvalidBoardCoord1() {
        Path saveFile = getTmpPath();
        char nextMove = FoxHoundUtils.FOX_FIELD;
        String[] players = defaultPlayers;
        players[0] = null;

        FoxHoundIO.saveGame(players, nextMove, saveFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveGameInvalidBoardCoord2() {
        Path saveFile = getTmpPath();
        char nextMove = FoxHoundUtils.FOX_FIELD;
        String[] players = defaultPlayers;
        players[0] = "invalid";

        FoxHoundIO.saveGame(players, nextMove, saveFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveGameInvalidBoardCoord3() {
        Path saveFile = getTmpPath();
        char nextMove = FoxHoundUtils.FOX_FIELD;
        String[] players = defaultPlayers;
        players[0] = "235";

        FoxHoundIO.saveGame(players, nextMove, saveFile); 
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveGameInvalidBoardCoord4() {
        Path saveFile = getTmpPath();
        char nextMove = FoxHoundUtils.FOX_FIELD;
        String[] players = defaultPlayers;
        players[0] = "XX1";

        FoxHoundIO.saveGame(players, nextMove, saveFile); 
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveGameInvalidMove() {
        Path saveFile = getTmpPath();
        char nextMove = '@';
        String[] players = defaultPlayers;

        FoxHoundIO.saveGame(players, nextMove, saveFile); 
    }

    @Test
    public void testSaveGamePathExists() throws IOException{
        String[] expected = defaultPlayers;
        String[] players = expected.clone();
        char nextMove = FoxHoundUtils.FOX_FIELD;
        Path saveFile = Paths.get("path","to","a","file","that","does","not","exist","game.txt");
        try {
            File tmpFile = File.createTempFile("foxHoundGameSave", ".txt");
            saveFile = tmpFile.toPath(); 
        } catch(IOException e) {
            throw new IOException("Saving file test failed.", e);
        }

        boolean result = FoxHoundIO.saveGame(players, nextMove, saveFile);

        assertFalse("Save not expected to be successful.", result);
        assertArrayEquals("Players array not expected to be modified.", expected, players);
    }
}
