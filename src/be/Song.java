package be;

public class Song {

    private final int playtime;
    private final int ID;
    private String title;
    private String artist;
    private String genre;
    private String destination;

    public Song(int ID, String title, String artist, String genre, String destination,int playtime) {
        this.playtime = playtime;
        this.ID = ID;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.destination = destination;
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

    public String getDestination() {
        return destination;
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

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Song{" +
                "playtime=" + playtime +
                ", ID=" + ID +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}

