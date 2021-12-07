package gui.model;

import be.Playlist;
import bll.PlaylistManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

public class PlaylistModel {
    PlaylistManager playlistManager;
    private ObservableList<Playlist> playlistToBeViewed;

    public PlaylistModel() throws IOException {
        playlistManager = new PlaylistManager();
        playlistToBeViewed = FXCollections.observableArrayList();
        playlistToBeViewed.addAll(playlistManager.getAllPlaylists());
    }

    public ObservableList<Playlist> getObservablePlaylists()
    {
        return playlistToBeViewed;
    }
}
