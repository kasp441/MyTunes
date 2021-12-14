package gui.controller;

import be.Playlist;
import gui.model.PlaylistModel;
import gui.model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class NewPlaylistController {

    public TextField enterPlaylistName;
    public PlaylistModel plm;
    public Button cancelPlaylistId;
    public Button savePlaylistId;

    public NewPlaylistController() throws IOException {
        plm = new PlaylistModel();
    }

    public void cancelPlaylistButton(ActionEvent actionEvent) {
        closeWindow();
    }

    public void savePlaylistButton(ActionEvent actionEvent) throws SQLException {
        plm.createPlaylist(enterPlaylistName.getText());
        closeWindow();
    }

    public void closeWindow() {                 //kunne laves i en utility class - evt. andet duplicate code?
        Stage stage = (Stage) savePlaylistId.getScene().getWindow();
        stage.close();
    }

}
