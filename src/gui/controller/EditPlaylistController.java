package gui.controller;

import gui.model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class EditPlaylistController {

    public TextField enterPlaylistName;
    public PlaylistModel plm;
    public Button cancelPlaylistId;
    public Button savePlaylistId;

    public EditPlaylistController() throws IOException {
        plm = new PlaylistModel();
    }

    public void cancelPlaylistButton(ActionEvent actionEvent) {
    }

    public void savePlaylistButton(ActionEvent actionEvent) throws SQLException {
        plm.createPlaylist(enterPlaylistName.getText());
        Stage stage = (Stage) savePlaylistId.getScene().getWindow();
        stage.close();
    }


}
