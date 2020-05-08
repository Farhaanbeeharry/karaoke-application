import javafx.scene.control.Dialog;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class exportData {

    //method to add a song to the playlist
    public static void addToPlaylist(String songName) {

        String fileName = "playlist.txt";

        File inputFile = new File("data/" + fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, true))) {
            if (checkEmptyFile(fileName) == false) {
                writer.newLine(); //if file is not empty, change line
            }
            writer.write(songName);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    //check if the selected file is empty
    public static boolean checkEmptyFile(String fileName) {

        File inputFile = new File("data/" + fileName);

        if (inputFile.length() == 0) {
            return true;
        }

        return false;

    }

    //re-write the playlist file completely (if the user made any changes)
    public static void updateFile(String fileName, LinkedList<String> playlist) {
        try {
            File outputFile = new File("data/" + fileName);
            try (PrintStream writer = new PrintStream(outputFile)) {
                for (int i = 0; i < playlist.size(); i++) {
                    if (!(playlist.get(i) == null)) {
                        if (i == playlist.size() - 1) {
                            writer.print(playlist.get(i)); //if it's writing the last line, do not change line
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

    //write the chosen song library file to the fileLocation.txt for the import and export functions know what to use
    public static void writeLocation(String fileLocation) {

        try {
            File outputFile = new File("data/fileLocation.txt");
            try (PrintStream writer = new PrintStream(outputFile)) {

                writer.print(fileLocation); //write on the first line

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }

    }

    //add a new line in the song library (separated by tab)
    public static void writeSong(HashFB<String, Song> songs, Song newSong) {

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

    //write the user settings options to the config file (separated by "--")
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
