package gui.model;

import be.Playlist;
import bll.PlaylistManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

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

}
