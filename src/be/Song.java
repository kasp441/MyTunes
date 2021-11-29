package be;

public class Song {

    private final int playtime;
    private final int ID;
    private String title;
    private String artist;
    private String genre;
    private String location;

    public Song(int playtime, int ID, String title, String artist, String genre, String location) {
        this.playtime = playtime;
        this.ID = ID;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.location = location;
    }

    public int getPlaytime() {
        return playtime;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public String getLocation() {
        return location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

