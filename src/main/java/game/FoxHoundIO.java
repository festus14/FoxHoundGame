package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FoxHoundIO {

    public static Boolean saveGame(String[] player, char fh, Path path) throws NullPointerException {
        String str = fh + "";
        for (String p : player) {
            str = str + " " + p;
        }

        System.out.println("The string...:" + str);

        Charset charset = Charset.forName("US-ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
            writer.write(str, 0, str.length());
            writer.close();
            return true;
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
            return false;
        }
    }

    public static char loadGame(String[] player, Path path) {
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String str = null;
            while ((str = reader.readLine()) != null) {
                String[] splitStr = str.trim().split("\\s+");
                for (int i = 0; i < player.length; i++) {
                    player[i] = splitStr[i + 1];
                }

                return splitStr[0].charAt(0);
            }
            reader.close();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return '#';
    }
}
