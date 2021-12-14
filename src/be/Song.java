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

    /**
     *get methods for song
     * @return
     */
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
}

