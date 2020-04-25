import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class exportData {

    public static void addToPlaylist(String songName) {

        String fileName = "playlist.txt";

        File inputFile = new File("/home/cst2550/IdeaProjects/KaraokeApplication/src/data/" + fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, true))) {
            if (checkEmptyFile(fileName) == false) {
                writer.newLine();
            }
            writer.write(songName);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static boolean checkEmptyFile(String fileName) {

        File inputFile = new File("/home/cst2550/IdeaProjects/KaraokeApplication/src/data/" + fileName);

        if (inputFile.length() == 0) {
            return true;
        }

        return false;

    }

    public static void updateFile(String fileName, LinkedList<String> playlist) {
        try {
            File outputFile = new File("/home/cst2550/IdeaProjects/KaraokeApplication/src/data/" + fileName);
            try (PrintStream writer = new PrintStream(outputFile)) {
                for (int i = 0; i < playlist.size(); i++) {
                    if (!(playlist.get(i) == null)) {
                        if (i == playlist.size() - 1) {
                            writer.print(playlist.get(i));
                        } else {
                            writer.println(playlist.get(i));
                        }
                    }

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

}
