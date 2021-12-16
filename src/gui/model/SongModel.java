package gui.model;

import be.Song;
import bll.Songmanager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
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
        songsToBeViewed.clear();
        songsToBeViewed.addAll(songmanager.getAllSongs());
        return songsToBeViewed;
    }

    public void search(String keyChar)
    {
        List<Song> allSongs = songmanager.getAllSongs();
        List<Song> result = songmanager.getSearchedSongs(allSongs, keyChar);
        songsToBeViewed.clear();
        songsToBeViewed.addAll(result);
    }

    public void deleteSong(Song song) {
        songmanager.deleteSong(song);
    }

    public void updateSong(Song songUpdate) throws SQLException { songmanager.updateSong(songUpdate);}

    public void createSong(String title, String artist, String genre, int playtime, String destination) throws SQLException {
        songmanager.createSong(title, artist, genre, playtime, destination);
    }

}
