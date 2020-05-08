public class Song {

    private String songName;
    private String artistName;
    private double duration;
    private String fileName;

    //default constructor
    public Song() {
        this.songName = "";
        this.artistName = "";
        this.duration = 0.0;
        this.fileName = "";
    }

    //user-defined constructor
    public Song(String songName) {
        this.songName = songName;
    }

    //user-defined constructor
    public Song(String songName, String artistName, double duration) {
        this.songName = songName;
        this.artistName = artistName;
        this.duration = duration;
    }

    //user-defined constructor
    public Song(String songName, String artistName, double duration, String fileName) {
        this.songName = songName;
        this.artistName = artistName;
        this.duration = duration;
        this.fileName = fileName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
