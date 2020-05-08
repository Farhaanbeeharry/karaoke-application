import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class KaraokeApplicationTester {

    @Test
    public void addSongToLibrary() {
        HashST<String, Song> songs = importData.importSongList();
        assertNotNull("Songs should not be null", songs);
    }

    @Test
    public void searchSongInSongLibrary() {
        HashST<String, Song> songs = importData.importSongList();
        String searchKey = "Angel";
        Song shouldMatchSong = songs.get("Angel");
        Song matchedSong = null;
        for (String songKey : songs.keys()) {
            if (searchKey.equalsIgnoreCase(songKey)) {
                matchedSong = songs.get(songKey);
            }
        }
        assertTrue(shouldMatchSong.equals(matchedSong));
    }

    @Test
    public void addSongToPlaylistTester() {
        exportData.addToPlaylist("Song Test");
        LinkedList<String> playlist = importData.getPlaylist();
        assertTrue(playlist.get(playlist.size()-1).equals("Song Test"));
    }

    @Test
    public void testNextSong() {
        LinkedList<String> playlist = importData.getPlaylist();
        playlist.clear();
        exportData.updateFile("playlist.txt", playlist);
        exportData.addToPlaylist("Song Test");
        exportData.addToPlaylist("Next Song");
        playlist = importData.getPlaylist();
        String firstSong = playlist.get(0);
        String secondSong = playlist.get(1);
        playlist.remove(0);
        assertTrue(playlist.get(0).equalsIgnoreCase(secondSong));
    }

    @Test
    public void retrieveSongFromPlaylist() {
        LinkedList<String> playlist = importData.getPlaylist();
        assertNotNull("Playlist should not be null", playlist);
    }

    @Test
    public void deleteSongFromPlaylist() {
        LinkedList<String> playlist = importData.getPlaylist();
        playlist.clear();
        exportData.updateFile("playlist.txt", playlist);
        exportData.addToPlaylist("Song to delete");
        exportData.addToPlaylist("A Song");
        playlist = importData.getPlaylist();
        int index = -1;
        for (int i = 0; i < playlist.size(); i++) {
            if (playlist.get(i).equalsIgnoreCase("Song to delete")) {
                index = i;
            }
        }
        playlist.remove(index);
        assertTrue(!playlist.contains("Song to delete"));
    }

}
