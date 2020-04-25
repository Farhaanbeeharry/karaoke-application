import java.io.*;
import java.util.LinkedList;

public class importData {

    public static HashST<String, Song> importSongList() {

        HashST<String, Song> songs = new HashST<String, Song>();

        try {
            FileReader inputFile = new FileReader(importFileLocation());

            try (BufferedReader inputBuffer = new BufferedReader(inputFile)) {

                String data;
                data = inputBuffer.readLine();

                while (data != null) {
                    String[] songPerLine = data.split("\t");
                    Song readSong = new Song(songPerLine[0], songPerLine[1], Integer.parseInt(songPerLine[2]), songPerLine[3]);
                    songs.put(songPerLine[0], readSong);
                    data = inputBuffer.readLine();
                }
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
            FileReader inputFile = new FileReader("/home/cst2550/IdeaProjects/KaraokeApplication/src/data/" + fileName);

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
            FileReader inputFile = new FileReader("/home/cst2550/IdeaProjects/KaraokeApplication/src/data/" + fileName);

            try (BufferedReader inputBuffer = new BufferedReader(inputFile)) {


                data = inputBuffer.readLine();

            }
        } catch (IOException e) {
            System.out.println(e);
        }

        return data;

    }

}