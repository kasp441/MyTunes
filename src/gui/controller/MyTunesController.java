package gui.controller;

import be.Song;
import be.Playlist;
import gui.model.SongModel;
import gui.model.PlaylistModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;


public class MyTunesController {

    public TableColumn<Song, String> songTitle;
    public TableColumn<Song, String> songArtist;
    public TableColumn<Song, String> songCategory;
    public TableColumn<Song, Integer> songTime;
    public Label currentlyPlayingLabel;
    public TextField filterInput;
    public Slider volumeSlider;
    public TableView<be.Playlist> TVPlaylist;
    public TableColumn<Playlist, String> playlistName;
    public TableColumn<Playlist, Integer> playlistSongCount;
    public TableColumn<Playlist, Integer> playlistTime;
    public ListView<Song> LVSongsOnPlaylist;
    private SongModel songModel;
    private PlaylistModel playlistModel;


    public TableView<be.Song> TVSongs;

    boolean playing;
    MediaPlayer player;
    int currentSongIndex;

    public MyTunesController() throws IOException {
        songModel = new SongModel();
        playlistModel = new PlaylistModel();
        currentSongIndex = 0;

    }

    public void initialize() {
        songTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        songArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        songCategory.setCellValueFactory(new PropertyValueFactory<>("genre"));
        songTime.setCellValueFactory(new PropertyValueFactory<>("playtime"));
        TVSongs.setItems(songModel.getObservableSongs());
        TVPlaylist.setItems(playlistModel.getObservablePlaylists());
        playlistName.setCellValueFactory(new PropertyValueFactory<>("playlistname"));
        playlistTime.setCellValueFactory(new PropertyValueFactory<>("totallenght"));
        playlistSongCount.setCellValueFactory(new PropertyValueFactory<>("totalSongs"));

        //Song search
        filterInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                songModel.searchSwitch(newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //Volume slider
        volumeSlider.setValue(25); //starting volume
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                player.setVolume(volumeSlider.getValue() / 100);
            }
        });
    }

    public void DeletePlaylistButton(ActionEvent actionEvent) throws SQLException {
        Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
        playlistModel.deletePlaylist(playlist);
        TVPlaylist.getItems().remove(playlist);
    }

    public void EditPlaylistButton(ActionEvent actionEvent) {

    }

    public void NewPlaylistButton(ActionEvent actionEvent) throws IOException {
        Parent mainWindowParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/view/EditPlaylist.fxml"))); // The FXML path
        Scene mainWindowScene = new Scene(mainWindowParent); // Scene supposed to be viewed
        Stage newSongStage = new Stage();
        newSongStage.setScene(mainWindowScene); // Sets the new scene
        newSongStage.showAndWait(); // This shows the new scene
        TVPlaylist.getItems().clear();
        TVPlaylist.setItems(playlistModel.getObservablePlaylists());
    }

    public void DownButton(ActionEvent actionEvent) {

    }

    public void UpButton(ActionEvent actionEvent) {
        
    }

    public void AddSongToPlaylistButton(ActionEvent actionEvent) throws SQLException {
        Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
        Song song = TVSongs.getSelectionModel().getSelectedItem();
        if (song != null && playlist != null){
            playlistModel.addSongToPlaylist(playlist,song);
            LVSongsOnPlaylist.getItems().add(song);
        }

    }

    public void DeleteSongFromPlaylistButton(ActionEvent actionEvent) throws SQLException {
        Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
        Song song = LVSongsOnPlaylist.getSelectionModel().getSelectedItem();
        int index = LVSongsOnPlaylist.getSelectionModel().getSelectedIndex()+1; // test
        if (song != null && playlist != null) {
            playlistModel.deleteSongFromPlaylist(playlist, song, index);
            LVSongsOnPlaylist.getItems().remove(song);
        }
    }

    public void BackButton(ActionEvent actionEvent) {
        currentSongIndex--;
        if (currentSongIndex >= 0) {
            player.stop();
            playMusic();
        } else {
            currentSongIndex++;
        }
    }

    public void SkipButton(ActionEvent actionEvent) {
        currentSongIndex++;
        if (currentSongIndex <= TVSongs.getItems().size() - 1) {
            player.stop();
            playMusic();
        } else {
            currentSongIndex--;
        }
    }

    public void PlayPauseButton(ActionEvent actionEvent) {
        if (!playing) {
            playMusic();
        } else {
            player.pause();
            playing = false;
            currentlyPlayingLabel.setText("(none)... is playing");
        }
    }

    private void playMusic() {
        playing = true;
        Song currentSong = TVSongs.getItems().get(currentSongIndex);
        String path = currentSong.getDestination();
        File file = new File(path);
        Media media = new Media(file.toURI().toString());
        player = new MediaPlayer(media);
        player.setVolume(volumeSlider.getValue() / 100);
        currentlyPlayingLabel.setText(currentSong.getTitle());
        player.play();
    }

    public void getClickedSong(MouseEvent mouseEvent) {
        currentSongIndex = TVSongs.getSelectionModel().getSelectedIndex();
    }

    public void CloseApplicationButton(ActionEvent actionEvent) {
        System.exit(1);
    }

    public void DeleteSongButton(ActionEvent actionEvent) {
        Song song = TVSongs.getSelectionModel().getSelectedItem();
        songModel.deleteSong(song);
        TVSongs.getItems().remove(song);
    }



    public void EditSongButton(javafx.event.ActionEvent event) throws SQLException, IOException {
        Parent mainWindowParent = FXMLLoader.load(getClass().getResource("/gui/view/EditSong.fxml")); // The FXML path
        Scene mainWindowScene = new Scene(mainWindowParent); // Scene supposed to be viewed
        Stage newSongStage = new Stage();
        newSongStage.setScene(mainWindowScene); // Sets the new scene
        newSongStage.show(); // This shows the new scene
        }

    public void NewSongButton (ActionEvent actionEvent)
    {

    }

    public void HandleSpecificPlaylistClicked(MouseEvent mouseEvent) {
        Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
        if (playlist != null)
        {
            LVSongsOnPlaylist.getItems().setAll(playlistModel.getSongsFromPlaylist(playlist));
        }
    }
}
