package gui.controller;

import be.Song;
import gui.model.SongModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditSongController implements Initializable {
    @FXML
    private TextField txtFieldSongTitleEdit;
    public TextField txtFieldArtistEdit;
    public TextField txtFieldTimeEdit;
    public TextField txtFieldFileEdit;
    public Button cancelButton;
    public Button saveButton;
    public ComboBox genreCombobox;
    private SongModel songModel;
    private int Id;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genreCombobox.setItems(FXCollections.observableArrayList("Rock", "Jazz", "Metal", "Rap", "Punk", "Pop", "Techno", "Country", "Blues", "House"));
        genreCombobox.setVisibleRowCount(5);
    }

    public EditSongController() throws IOException {
        songModel = new SongModel();
    }

    public void handleSave(ActionEvent actionEvent) throws SQLException {

        if (!txtFieldTimeEdit.getText().isEmpty() && !genreCombobox.getItems().isEmpty() && !txtFieldSongTitleEdit.getText().isEmpty() && !txtFieldFileEdit.getText().isEmpty() && !txtFieldArtistEdit.getText().isEmpty()) {

            Song updateSong = new Song(Id,txtFieldSongTitleEdit.getText(),txtFieldArtistEdit.getText(), String.valueOf(genreCombobox.getSelectionModel().getSelectedItem()), txtFieldFileEdit.getText(), Integer.parseInt(txtFieldTimeEdit.getText()));

            songModel.updateSong(updateSong);

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("fill out all textfields");
            alert.showAndWait();        }


    }

    public void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void handleChoose(ActionEvent actionEvent) {
    }

    public void handleMore(ActionEvent actionEvent) {
    }


    public void setSong(Song song)
    {
    Id = song.getID();
    txtFieldSongTitleEdit.setText(song.getTitle());
    txtFieldArtistEdit.setText(song.getArtist());

    txtFieldFileEdit.setText(song.getDestination());
    txtFieldTimeEdit.setText(Integer.toString(song.getPlaytime()));
    }


}
