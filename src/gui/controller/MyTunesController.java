package gui.controller;

import be.Song;
import be.Playlist;
import bll.util.Jukebox;
import gui.model.SongModel;
import gui.model.PlaylistModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class MyTunesController {

    @FXML
    private TableColumn<Song, String> songTitle;
    @FXML
    private TableColumn<Song, String> songArtist;
    @FXML
    private TableColumn<Song, String> songCategory;
    @FXML
    private TableColumn<Song, Integer> songTime;
    @FXML
    private Label currentlyPlayingLabel;
    @FXML
    private TextField filterInput;
    @FXML
    private Slider volumeSlider;
    @FXML
    private TableView<be.Playlist> TVPlaylist;
    @FXML
    private TableColumn<Playlist, String> playlistName;
    @FXML
    private TableColumn<Playlist, Integer> playlistSongCount;
    @FXML
    private TableColumn<Playlist, Integer> playlistTime;
    @FXML
    private ListView<Song> LVSongsOnPlaylist;
    private SongModel songModel;
    private PlaylistModel playlistModel;
    private Jukebox jukebox;
    double volume;


    public TableView<be.Song> TVSongs;
    
    public MyTunesController() throws IOException {
        jukebox = new Jukebox();
        songModel = new SongModel();
        playlistModel = new PlaylistModel();
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
                songModel.search(newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //Volume slider
        volumeSlider.setValue(25); //starting volume
        volume = volumeSlider.getValue() / 100;
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                volume = volumeSlider.getValue() / 100;
                jukebox.setVolume(volume);
            }
        });
    }

    public void DeletePlaylistButton(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
            playlistModel.deletePlaylist(playlist);
            TVPlaylist.getItems().remove(playlist);
        }
    }

    public void EditPlaylistButton(ActionEvent actionEvent) throws IOException {
        Playlist selectedPlaylist = TVPlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/gui/view/EditPlaylist.fxml"));
            Scene mainWindowScene = null;

            try {
                mainWindowScene = new Scene(root.load());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Stage editPlaylistStage = new Stage();
            editPlaylistStage.setScene(mainWindowScene);
            EditPlaylistController editPlaylistController = root.getController();
            editPlaylistController.setPlaylist(selectedPlaylist);
            editPlaylistStage.showAndWait();
            TVPlaylist.getItems().clear();
            TVPlaylist.setItems(playlistModel.getObservablePlaylists());
        }

    }

    public void NewPlaylistButton(ActionEvent actionEvent) throws IOException {
        Parent mainWindowParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/view/NewPlaylist.fxml"))); // The FXML path
        Scene mainWindowScene = new Scene(mainWindowParent); // Scene supposed to be viewed
        Stage newPlaylistStage = new Stage();
        newPlaylistStage.setScene(mainWindowScene); // Sets the new scene
        newPlaylistStage.showAndWait(); // This shows the new scene
        TVPlaylist.getItems().clear();
        TVPlaylist.setItems(playlistModel.getObservablePlaylists());
    }

    public void DownButton(ActionEvent actionEvent) throws Exception {
        if (LVSongsOnPlaylist.getSelectionModel().getSelectedIndex()+1 != LVSongsOnPlaylist.getItems().size()){
            songMove(1);
        }
    }

    public void UpButton(ActionEvent actionEvent) throws Exception {
        if (LVSongsOnPlaylist.getSelectionModel().getSelectedIndex() !=0){
            songMove(-1);
        }
    }

    public void AddSongToPlaylistButton(ActionEvent actionEvent) throws SQLException {
        Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
        Song song = TVSongs.getSelectionModel().getSelectedItem();
        if (song != null && playlist != null){
            playlistModel.addSongToPlaylist(playlist,song);
            LVSongsOnPlaylist.getItems().add(song);
            TVPlaylist.refresh();
        }

    }

    public void DeleteSongFromPlaylistButton(ActionEvent actionEvent) throws SQLException {
        Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
        Song song = LVSongsOnPlaylist.getSelectionModel().getSelectedItem();
        int index = LVSongsOnPlaylist.getSelectionModel().getSelectedIndex()+1; // test
        if (song != null && playlist != null) {
            playlistModel.deleteSongFromPlaylist(playlist, song, index);
            LVSongsOnPlaylist.getItems().remove(song);
            TVPlaylist.getItems().clear();
            TVPlaylist.setItems(playlistModel.getObservablePlaylists());
        }
    }

    public void BackButton(ActionEvent actionEvent) {
        jukebox.backSong();
        jukebox.setVolume(volume);
        updateCurrentlyPlayinglabel();
    }

    public void SkipButton(ActionEvent actionEvent) {
        jukebox.skipSong();
        jukebox.setVolume(volume);
        updateCurrentlyPlayinglabel();
    }

    public void PlayPauseButton(ActionEvent actionEvent) {
        //for playing from playlist
        if (LVSongsOnPlaylist.getSelectionModel().getSelectedItem() != null && !jukebox.isPlaying()){
            int songsIndex = LVSongsOnPlaylist.getSelectionModel().getSelectedIndex();
            Song selectedSong = jukebox.getSongList().get(songsIndex);
            if (selectedSong != jukebox.getCurrentSong()){
                jukebox.setCurrentSong(selectedSong);
                LVSongsOnPlaylist.getSelectionModel().clearSelection();
            }
        }

        //for if the songlist is selected
        if (TVSongs.getSelectionModel().getSelectedItem() != null && !jukebox.isPlaying()){
            int songsIndex = TVSongs.getSelectionModel().getFocusedIndex();
            Song selectedSong = jukebox.getSongList().get(songsIndex);
                if (selectedSong != jukebox.getCurrentSong()){
                    jukebox.setCurrentSong(selectedSong);
                    TVSongs.getSelectionModel().clearSelection();
                }
        }

        jukebox.playPause();
        jukebox.setVolume(volume);
        updateCurrentlyPlayinglabel();
    }


    public void getClickedSong(MouseEvent mouseEvent) {
        //// rename metode til "handleClickTVSongs"!
        LVSongsOnPlaylist.getSelectionModel().clearSelection();

        int selectedIndex = TVSongs.getSelectionModel().getSelectedIndex();
        List<Song> selectedSongList = TVSongs.getItems();

        if (selectedSongList != jukebox.getSongList()){
            jukebox.setSongList(selectedSongList);
        }

        jukebox.setCurrentSongIndex(selectedIndex);
    }

    public void handleLVSongsOnPlaylistClicked(MouseEvent mouseEvent) {
        TVSongs.getSelectionModel().clearSelection();

        int selectedIndex = LVSongsOnPlaylist.getSelectionModel().getSelectedIndex();
        List<Song> selectedSongList = LVSongsOnPlaylist.getItems();

        if (selectedSongList != jukebox.getSongList()){
            jukebox.setSongList(selectedSongList);
        }
        jukebox.setCurrentSongIndex(selectedIndex);
    }

    private void updateCurrentlyPlayinglabel(){
        currentlyPlayingLabel.setText(jukebox.getCurrentSongTitle());
    }

    public void CloseApplicationButton(ActionEvent actionEvent) {
        System.exit(1);
    }

    public void DeleteSongButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Song song = TVSongs.getSelectionModel().getSelectedItem();
            songModel.deleteSong(song);
            TVSongs.getItems().remove(song);
        }
    }


    public void EditSongButton(javafx.event.ActionEvent event) throws SQLException, IOException {
        Song selectedSong = TVSongs.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/gui/view/EditSong.fxml"));
            Scene mainWindowScene = null;

            try {
                mainWindowScene = new Scene(root.load());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Stage editSongStage = new Stage();
            editSongStage.setScene(mainWindowScene);
            EditSongController editSongController = root.getController();
            editSongController.setSong(selectedSong);
            editSongStage.showAndWait();
            TVSongs.getItems().clear();
            TVSongs.setItems(songModel.getObservableSongs());
        }
    }

    public void NewSongButton (ActionEvent actionEvent) throws IOException {
        Parent mainWindowParent = FXMLLoader.load(getClass().getResource("/gui/view/NewSong.fxml")); // The FXML path
        Scene mainWindowScene = new Scene(mainWindowParent); // Scene supposed to be viewed
        Stage newSongStage = new Stage();
        newSongStage.setScene(mainWindowScene); // Sets the new scene
        newSongStage.showAndWait(); // This shows the new scene
        TVSongs.getItems().clear();
        TVSongs.setItems(songModel.getObservableSongs());
    }

    public void HandleSpecificPlaylistClicked(MouseEvent mouseEvent) {
        Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
        if (playlist != null)
        {
            LVSongsOnPlaylist.getItems().setAll(playlistModel.getSongsFromPlaylist(playlist));
        }
    }


    public void handleMouseMove(MouseEvent mouseEvent) {
        updateCurrentlyPlayinglabel();
        jukebox.setVolume(volume);
    }


    private void songMove (int upOrDown) throws Exception {
        if (LVSongsOnPlaylist.getSelectionModel().getSelectedItem() != null) {
        Playlist pl = TVPlaylist.getSelectionModel().getSelectedItem();
        int i = LVSongsOnPlaylist.getSelectionModel().getSelectedIndex() +1;
        int j = LVSongsOnPlaylist.getSelectionModel().getSelectedIndex() +1 + (upOrDown) ;
        List<Song> ls = LVSongsOnPlaylist.getItems();


        playlistModel.moveSongsOnPlaylist(pl, ls, i, j);
        LVSongsOnPlaylist.getSelectionModel().clearSelection();

        }
    }

}
