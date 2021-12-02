package be;

import java.util.List;

public class Playlist {

    private List<Song> playlist;
    private String playlistname;
    private final int ID;
    private int totallenght;
    private int totalSongs;


    public Playlist(int id, String playlistname, int totallenght, int totalSongs){
        this.ID=id;
        this.playlistname=playlistname;
        this.totallenght=totallenght;
        this.totalSongs=totalSongs;

    }

    public List<Song> getPlaylist() {
        return playlist;
    }

    public String getPlaylistname() {
        return playlistname;
    }

    public int getId() {
        return ID;
    }

    public int getTotallenght() { return totallenght;}

    public void setPlaylistname(String playlistname) {
        this.playlistname = playlistname;
    }

    public int getTotalSongs() {return totalSongs;}
}
