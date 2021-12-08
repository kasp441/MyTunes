package gui.controller;

import be.Song;
import gui.model.SongModel;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EditSongController {
    public TextField txtFieldTimeEdit;
    public TextField txtFieldFileEdit;
    public TextField txtFieldArtistEdit;
    public TextField txtFieldSongTitleEdit;
    SongModel songModel;



    public EditSongController() throws IOException {
        songModel = new SongModel();
    }

    public void setSong(Song song) {
        txtFieldSongTitleEdit.setText(song.getTitle());
        txtFieldArtistEdit.setText(song.getArtist());
        txtFieldTimeEdit.setText(Integer.toString(song.getPlaytime()));
        txtFieldFileEdit.setText(song.getDestination());
    }
}
