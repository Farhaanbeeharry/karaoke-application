import javafx.scene.control.Dialog;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class exportData {

    public static void addToPlaylist(String songName) {

        String fileName = "playlist.txt";

        File inputFile = new File("data/" + fileName);

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

        File inputFile = new File("data/" + fileName);

        if (inputFile.length() == 0) {
            return true;
        }

        return false;

    }

    public static void updateFile(String fileName, LinkedList<String> playlist) {
        try {
            File outputFile = new File("data/" + fileName);
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
            File outputFile = new File("data/fileLocation.txt");
            try (PrintStream writer = new PrintStream(outputFile)) {

                writer.print(fileLocation);

                }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }


    }

  
    public static void writeSong(HashST<String, Song> songs, Song newSong) {

        LinkedList<Song> songsArray = new LinkedList<Song>();

        for (String songName : songs.keys()) {

            songsArray.add(songs.get(songName));

        }

        songsArray.add(newSong);

        try {
            File outputFile = new File(importData.importFileLocation());
            try (PrintStream writer = new PrintStream(outputFile)) {
                for (int i = 0; i < songsArray.size(); i++) {
                    if (!(songsArray.get(i).getSongName() == null)) {
                        writer.print(songsArray.get(i).getSongName());
                        writer.print("\t");
                        writer.print(songsArray.get(i).getArtistName());
                        writer.print("\t");
                        writer.print(songsArray.get(i).getDuration());
                        writer.print("\t");
                        if (i == songsArray.size() - 1) {
                            writer.print(songsArray.get(i).getFileName());
                        } else {
                            writer.println(songsArray.get(i).getFileName());
                        }
                    }

                }
            }
        } catch (FileNotFoundException e) {
            DialogBox.box("File not found!");

        }


    }

public static void exportConfig(String[] config) {


        try {
            File outputFile = new File("data/config.txt");
            try (PrintStream writer = new PrintStream(outputFile)) {

                writer.print(config[0]);
                writer.print("--");
                writer.print(config[1]);

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }


    }

}
