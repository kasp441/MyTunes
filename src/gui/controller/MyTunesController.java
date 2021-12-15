package gui.controller;

import be.Song;
import be.Playlist;
import bll.util.Jukebox;
import gui.model.SongModel;
import gui.model.PlaylistModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
            Stage editPlaylistStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            editPlaylistStage.setScene(mainWindowScene);
            EditPlaylistController editPlaylistController = root.getController();
            editPlaylistController.setPlaylist(selectedPlaylist);
            editPlaylistStage.show();
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
        songMove(1);
    }

    public void UpButton(ActionEvent actionEvent) throws Exception {
        songMove(-1);
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
        jukebox.setVolume(volumeSlider.getValue()/100);
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
        int j = LVSongsOnPlaylist.getSelectionModel().getSelectedIndex() + (upOrDown) +1;
        List<Song> ls = LVSongsOnPlaylist.getItems();

        //if (i > 0 || i < ls.size()) {
            playlistModel.moveSongsOnPlaylist(pl, ls, i, j);
            LVSongsOnPlaylist.getSelectionModel().clearSelection();
            //}
        }
    }

}
