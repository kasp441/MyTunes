package gui.model;

import be.Playlist;
import be.Song;
import bll.PlaylistManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
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

    public void addSongToPlaylist(Playlist playlist,Song song){
        playlistManager.addSongToPlaylist(playlist,song);
    }
}
