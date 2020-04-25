import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class importData {

    public static HashST<String, Song> importSongList() {

        String fileName = "songList.txt";
        HashST<String, Song> songs = new HashST<String, Song>();

        try {
            FileReader inputFile = new FileReader("/home/cst2550/IdeaProjects/KaraokeApplication/src/data/" + fileName);

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
            System.out.println(e);
        }

        return songs;

    }

    public static ArrayList<String> getPlaylist() {

        String fileName = "playlist.txt";
        ArrayList<String> playlist = new ArrayList<String>();

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
        ArrayList<String> playlist = getPlaylist();
        return playlist.size();
    }

}