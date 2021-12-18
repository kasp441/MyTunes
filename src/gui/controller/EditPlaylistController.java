package gui.controller;

import be.Playlist;
import gui.model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class EditPlaylistController {

    @FXML
    private TextField enterPlaylistName;
    public Button cancelPlaylistButton;
    @FXML
    private Button savePlaylistButton;
    public PlaylistModel plm;
    private int Id;

    public EditPlaylistController() throws IOException {
        plm = new PlaylistModel();
    }

    /**
     * event handler for cancel button. closes the window
     * @param actionEvent
     */
    public void cancelPlaylistButton(ActionEvent actionEvent) { closeWindow();
    }

    /**
     * Event handler for save button. Updates the title of the chosen playlist
     * @param actionEvent
     * @throws SQLException
     */
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

    /**
     * closes current window
     */
    public void closeWindow() {
        Stage stage = (Stage) savePlaylistButton.getScene().getWindow();
        stage.close();
    }
}
