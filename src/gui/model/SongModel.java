package gui.model;

import be.Playlist;
import be.Song;
import bll.PlaylistManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongModel {
    private ObservableList<Song> allSongs = FXCollections.observableArrayList();
}
