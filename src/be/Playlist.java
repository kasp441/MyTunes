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

    /**
     *get metods for playlist
     * @return
     */
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

    public int getTotalSongs()
    {
        updateTotalSongs();
        return totalSongs;
    }

    /**
     * adds a song to the list of songs in the playlist
     * @param song
     */
    public void addSongToPlaylist(Song song)
    {
        playlist.add(song);
        updateTotallenght();
        updateTotalSongs();
    }

    /**
     * removes a song from the list of songs in the playlist
     * @param song
     */
    public void removeSongFromPlaylist(Song song)
    {
        playlist.remove(song);
        updateTotallenght();
        updateTotalSongs();
    }

    /**
     * refreshes the property @totalSongs
     */
    private void updateTotalSongs()
    {
        totalSongs = 0;
        for(Song song : this.playlist)
        {
            totalSongs++;
        }
    }

    /**
     * refreshes the property @totallengh
     */
    private void updateTotallenght()
    {
        totallenght = 0;
        for(Song song : this.playlist)
        {
            totallenght += song.getPlaytime();
        }
    }
}
