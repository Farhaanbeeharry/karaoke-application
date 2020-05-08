import org.junit.Test;
import java.util.LinkedList;
import static org.junit.Assert.*;

//contain a test of each of the required functionalities
public class KaraokeApplicationTester {

    //test read file into a hash table
    @Test
    public void addSongToLibrary() {
        HashFB<String, Song> songs = importData.importSongList();
        assertNotNull("Songs should not be null", songs);
    }

    //search for a song in the song library
    @Test
    public void searchSongInSongLibrary() {
        HashFB<String, Song> songs = importData.importSongList();
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

    //add a song to the playlist
    @Test
    public void addSongToPlaylistTester() {
        exportData.addToPlaylist("Angel");
        LinkedList<String> playlist = importData.getPlaylist();
        assertTrue(playlist.get(playlist.size()-1).equals("Angel"));
    }

    //test the next song function
    @Test
    public void testNextSong() {
        LinkedList<String> playlist = importData.getPlaylist();
        playlist.clear();
        exportData.updateFile("playlist.txt", playlist);
        exportData.addToPlaylist("Angel");
        exportData.addToPlaylist("Slow");
        playlist = importData.getPlaylist();
        String firstSong = playlist.get(0);
        String secondSong = playlist.get(1);
        playlist.remove(0);
        assertTrue(playlist.get(0).equalsIgnoreCase(secondSong));
    }

    //read the playlist file and insert the data into a table
    @Test
    public void retrieveSongFromPlaylist() {
        LinkedList<String> playlist = importData.getPlaylist();
        assertNotNull("Playlist should not be null", playlist);
    }

    //delete a song from the playlist
    @Test
    public void deleteSongFromPlaylist() {
        LinkedList<String> playlist = importData.getPlaylist();
        playlist.clear();
        exportData.updateFile("playlist.txt", playlist);
        exportData.addToPlaylist("Song to delete");
        exportData.addToPlaylist("Slow");
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
