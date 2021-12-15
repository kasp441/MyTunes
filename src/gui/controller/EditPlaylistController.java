package gui.controller;

import be.Playlist;
import be.Song;
import gui.model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class EditPlaylistController {

    public TextField enterPlaylistName;
    public Button cancelPlaylistButton;
    public Button savePlaylistButton;
    public PlaylistModel plm;
    private int Id;

    public EditPlaylistController() throws IOException {
        plm = new PlaylistModel();
    }
    public void cancelPlaylistButton(ActionEvent actionEvent) { closeWindow();
    }

    public void savePlaylistButton(ActionEvent actionEvent) throws SQLException {
        if (!enterPlaylistName.getText().isEmpty()) {
            Playlist playlistUpdate = new Playlist(Id, enterPlaylistName.getText());

            plm.updatePlaylist(playlistUpdate);

            Stage stage = (Stage) savePlaylistButton.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("fill out all textfields");
            alert.showAndWait();
        }
    }

    public void setPlaylist(Playlist playlist)
    {
        Id = playlist.getId();
        enterPlaylistName.setText(playlist.getPlaylistname());
    }

    public void closeWindow() {                 //kunne laves i en utility class - evt. andet duplicate code?
        Stage stage = (Stage) savePlaylistButton.getScene().getWindow();
        stage.close();
    }
}
