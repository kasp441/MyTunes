package gui.model;

import be.Playlist;
import be.Song;
import bll.PlaylistManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PlaylistModel {
    PlaylistManager playlistManager;
    private ObservableList<Playlist> playlistToBeViewed;

    public PlaylistModel() throws IOException {
        playlistManager = new PlaylistManager();
        playlistToBeViewed = FXCollections.observableArrayList();
    }

    public ObservableList<Playlist> getObservablePlaylists()
    {
        playlistToBeViewed.addAll(playlistManager.getAllPlaylists());
        return playlistToBeViewed;
    }

    public Playlist createPlaylist (String playlistName) throws SQLException {
        return playlistManager.createPlaylist(playlistName);
    }

    public List<Song> getSongsFromPlaylist(Playlist playlist){
        return playlistManager.getSongsFromPlaylist(playlist);
    }

    public void addSongToPlaylist(Playlist playlist,Song song) throws SQLException {
        playlistManager.addSongToPlaylist(playlist,song);
    }

    public void deleteSongFromPlaylist(Playlist playlist, Song song, int index) throws SQLException {
        playlistManager.deleteSongFromPlaylist(playlist, song, index);
    }

    public Playlist deletePlaylist (Playlist playlistDelete) throws SQLException {
        return playlistManager.deletePlaylist(playlistDelete);
    }

    public void updatePlaylist(Playlist playlist) throws SQLException {
         playlistManager.updatePlaylist(playlist);
    }

    public void moveSongsOnPlaylist(Playlist playlist, List<Song>songsOnPlaylist, int i, int j) throws Exception {
        Collections.swap(songsOnPlaylist, i-1, j-1);
        playlistManager.moveSongsOnPlaylist(playlist, i, j);
    }

}
