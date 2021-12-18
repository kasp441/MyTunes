package gui.controller;

import gui.model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class NewPlaylistController {

    @FXML
    private TextField enterPlaylistName;
    @FXML
    private PlaylistModel plm;

    public Button cancelPlaylistId;
    @FXML
    private Button savePlaylistId;

    public NewPlaylistController() throws IOException {
        plm = new PlaylistModel();
    }

    /**
     * Event handler for cancel button. Closes the window
     * @param actionEvent
     */
    public void cancelPlaylistButton(ActionEvent actionEvent) {
        closeWindow();
    }

    /**
     * Creates a new playlist from the input in the textbox
     * @param actionEvent
     * @throws SQLException
     */
    public void savePlaylistButton(ActionEvent actionEvent) throws SQLException {
        plm.createPlaylist(enterPlaylistName.getText());
        closeWindow();
    }

    /**
     * closes the window
     */
    public void closeWindow() {
        Stage stage = (Stage) savePlaylistId.getScene().getWindow();
        stage.close();
    }

}
