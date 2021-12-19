package gui.controller;

import be.Song;
import gui.model.SongModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditSongController implements Initializable {
    @FXML
    private TextField txtFieldSongTitleEdit;
    @FXML
    private TextField txtFieldArtistEdit;
    @FXML
    private TextField txtFieldTimeEdit;
    @FXML
    private TextField txtFieldFileEdit;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox genreCombobox;
    private SongModel songModel;
    private int Id;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genreCombobox.setItems(FXCollections.observableArrayList("Rock", "Jazz", "Metal", "Rap", "Punk", "Pop", "Techno", "Country", "Blues", "House","Lofi", "Melodic", "Indie"));
        genreCombobox.setVisibleRowCount(5);
    }

    public EditSongController() throws IOException {
        songModel = new SongModel();
    }

    /**
     * Event handler for save button. Updates the database with the new song info
     * @param actionEvent
     * @throws SQLException
     */
    public void handleSave(ActionEvent actionEvent) throws SQLException {

        if (!txtFieldTimeEdit.getText().isEmpty() && genreCombobox.getSelectionModel().getSelectedItem() != null && !txtFieldSongTitleEdit.getText().isEmpty() && !txtFieldFileEdit.getText().isEmpty() && !txtFieldArtistEdit.getText().isEmpty()) {
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

    /**
     * event handler for cancel button. Closes the window
     * @param actionEvent
     */
    public void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Event handler for choose button. Opens a filechooser window and autofills textfield with info from the
     * chosen file.
     * @param actionEvent
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    public void handleChoose(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file resource");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 File", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV File", "*.wav")
        );
        Node source = (Node) actionEvent.getSource();
        File file = fileChooser.showOpenDialog(source.getScene().getWindow());
        if (file != null) {
            String filePath = file.getPath();
            txtFieldFileEdit.setText(filePath.replace("\\","/").split("MyTunes/")[1]);
            String fileName = file.getName();
            txtFieldSongTitleEdit.setText(fileName);
            javafx.scene.media.Media media = new javafx.scene.media.Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    double songlengt = media.getDuration().toSeconds();
                    txtFieldTimeEdit.setText((int) songlengt+"");
                }
            });
        }
    }

    /**
     * updates the chosen song object with the new info
     * @param song
     */
    public void setSong(Song song)
    {
        Id = song.getID();
        txtFieldSongTitleEdit.setText(song.getTitle());
        txtFieldArtistEdit.setText(song.getArtist());

        txtFieldFileEdit.setText(song.getDestination());
        txtFieldTimeEdit.setText(Integer.toString(song.getPlaytime()));
    }
}
