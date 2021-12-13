package gui.controller;

import be.Song;
import gui.model.SongModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditSongController implements Initializable {
    public TextField txtFieldTimeEdit;
    public TextField txtFieldFileEdit;
    public TextField txtFieldArtistEdit;
    public TextField txtFieldSongTitleEdit;
    public Button saveButton;
    public ComboBox genreCombobox;
    public Button cancelButton;
    private SongModel songModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        genreCombobox.setItems(FXCollections.observableArrayList("Rock", "Jazz", "Metal", "Rap", "Punk", "Pop", "Techno", "Country", "Blues", "House"));
        genreCombobox.setVisibleRowCount(5);
    }

    public EditSongController() throws IOException {
        songModel = new SongModel();
    }

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

    public void handleSave(ActionEvent actionEvent) throws SQLException {
        if (!txtFieldTimeEdit.getText().isEmpty() && !genreCombobox.getItems().isEmpty() && !txtFieldSongTitleEdit.getText().isEmpty() && !txtFieldFileEdit.getText().isEmpty() && !txtFieldArtistEdit.getText().isEmpty()) {
            songModel.createSong(txtFieldSongTitleEdit.getText(), txtFieldArtistEdit.getText(), String.valueOf(genreCombobox.getSelectionModel().getSelectedItem()), Integer.parseInt(txtFieldTimeEdit.getText()), txtFieldFileEdit.getText());
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

    public void handleMore(ActionEvent actionEvent) {
        Stage newStage = new Stage();
        VBox comp = new VBox();

        Label labelField = new Label("Please enter your genre below");
        TextField genreField = new TextField("");
        Button buttonDone = new Button("Done");
        comp.getChildren().add(labelField);
        comp.getChildren().add(genreField);
        comp.getChildren().add(buttonDone);

        Scene stageScene = new Scene(comp, 300, 75);
        newStage.setScene(stageScene);
        newStage.showAndWait();


    }
}
