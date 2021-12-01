package gui.model;

import be.Playlist;
import be.Song;
import bll.PlaylistManager;
import bll.Songmanager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class SongModel {
    Songmanager songmanager;
    private ObservableList<Song> songsToBeViewed;

    public SongModel() throws IOException {
        songmanager = new Songmanager();
        songsToBeViewed = FXCollections.observableArrayList();
        songsToBeViewed.addAll(songmanager.getAllSongs());
    }

    public ObservableList<Song> getObservableSongs(){
        return songsToBeViewed;
    }

}
