import java.io.*;
import java.util.LinkedList;

public class importData {

    public static HashFB<String, Song> importSongList() {

        HashFB<String, Song> songs = new HashFB<String, Song>();

        try {
            FileReader inputFile = new FileReader(importFileLocation());

            try (BufferedReader inputBuffer = new BufferedReader(inputFile)) {

                String data;
                data = inputBuffer.readLine();

                while (data != null) {
                    String[] songPerLine = data.split("\t");
                    Song readSong = new Song(songPerLine[0], songPerLine[1], Double.parseDouble(songPerLine[2]), songPerLine[3]);
                    songs.put(songPerLine[0], readSong);
                    data = inputBuffer.readLine();
                }
            } catch (Exception e) {
                System.out.println("File is not compatible with this app!");
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println("Invalid Location");
            System.exit(0);
        }

        return songs;

    }

    public static LinkedList<String> getPlaylist() {

        String fileName = "playlist.txt";

        LinkedList<String> playlist = new LinkedList<String>();

        try {
            FileReader inputFile = new FileReader("data/" + fileName);

            try (BufferedReader inputBuffer = new BufferedReader(inputFile)) {
                String data;
                data = inputBuffer.readLine();

                while (data != null) {
                    playlist.add(data);
                    data = inputBuffer.readLine();
                }

            }

        } catch (IOException e) {
            System.out.println(e);
        }

        return playlist;

    }

    public static int getPlaylistCount() {
        LinkedList<String> playlist = getPlaylist();
        return playlist.size();
    }

    public static String importFileLocation() {

        String fileName = "fileLocation.txt";
        String data = "";

        try {
            FileReader inputFile = new FileReader("data/" + fileName);

            try (BufferedReader inputBuffer = new BufferedReader(inputFile)) {


                data = inputBuffer.readLine();

            }
        } catch (IOException e) {
            System.out.println(e);
        }

        return data;

    }

    public static String[] importConfig() {

        String fileName = "config.txt";
        String data = "";
        String[] config = null;

        try {
            FileReader inputFile = new FileReader("data/" + fileName);

            try (BufferedReader inputBuffer = new BufferedReader(inputFile)) {

                data = inputBuffer.readLine();
                config = data.split("--");

            }
        } catch (IOException e) {
            System.out.println(e);
        }

        return config;

    }

}