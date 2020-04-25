import javafx.scene.control.Dialog;

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

    public static void writeLocation(String fileLocation) {


        try {
            File outputFile = new File("/home/cst2550/IdeaProjects/KaraokeApplication/src/data/fileLocation.txt");
            try (PrintStream writer = new PrintStream(outputFile)) {

                writer.print(fileLocation);

                }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }


    }

    public static void writeSong(Song newSong) {
        try {
            File inputFile = new File(importData.importFileLocation());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, true))) {
                writer.newLine();
                writer.write(newSong.getSongName() + "\t" + newSong.getArtistName() + "\t" + newSong.getDuration() + "\t" + newSong.getFileName());
            }

        } catch (IOException e) {
            DialogBox.box("File not found!");
        }
    }

}
