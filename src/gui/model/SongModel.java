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

    public void searchSwitch(String keyChar)
    {
        List<Song> allSongs = songmanager.getAllSongs();
        List<Song> result = songmanager.getSearchedSong(allSongs, keyChar);
        songsToBeViewed.clear();
        songsToBeViewed.addAll(result);
    }

    public void deleteSong(Song song) {
        songmanager.deleteSong(song);
    }

}
