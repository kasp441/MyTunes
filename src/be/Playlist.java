package be;

import javafx.collections.FXCollections;

import java.util.List;

public class Playlist {

    private List<Song> playlist;
    private String playlistname;
    private final int ID;
    private int totallenght;
    private int totalSongs;


    public Playlist(int id, String playlistname){
        playlist = FXCollections.observableArrayList();
        this.ID=id;
        this.playlistname=playlistname;
        this.totallenght=0;
        this.totalSongs=0;
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

    public int getTotallenght()
    {
        updateTotallenght();
        return totallenght;
    }

    public void updateTotallenght()
    {
        totallenght = 0;
        for(Song song : this.playlist)
        {
            totallenght += song.getPlaytime();
        }
    }

    public void updateTotalSongs()
    {
        totalSongs = 0;
        for(Song song : this.playlist)
        {
            totalSongs++;
        }
    }

    public void setPlaylistname(String playlistname) {
        this.playlistname = playlistname;
    }

    public int getTotalSongs()
    {
        updateTotalSongs();
        return totalSongs;
    }

    public void addSongToPlaylist(Song song)
    {
        playlist.add(song);
        updateTotallenght();
        updateTotalSongs();
    }

    public void removeSongFromPlaylist(Song song)
    {
        playlist.remove(song);
        updateTotallenght();
        updateTotalSongs();
    }
}
