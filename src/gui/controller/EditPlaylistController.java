package gui.controller;

import gui.model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditPlaylistController {

    public TextField enterPlaylistName;
    public Button cancelPlaylistId;
    public Button savePlaylistId;
    public PlaylistModel plm;

    public EditPlaylistController() throws IOException {
        plm = new PlaylistModel();
    }
    public void cancelPlaylistButton(ActionEvent actionEvent) { closeWindow();
    }

    public void savePlaylistButton(ActionEvent actionEvent)
    {
        //TODO
    }

    public void closeWindow() {                 //kunne laves i en utility class - evt. andet duplicate code?
        Stage stage = (Stage) savePlaylistId.getScene().getWindow();
        stage.close();
    }
}
