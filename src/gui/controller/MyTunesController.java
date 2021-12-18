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

    /**
     * Initializes cell factories and listeners for volume slider and search field
     */
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

    /**
     * Event handler for the delete playlist button
     * @param actionEvent
     * @throws SQLException
     */
    public void DeletePlaylistButton(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
            playlistModel.deletePlaylist(playlist);
            TVPlaylist.getItems().remove(playlist);
        }
    }

    /**
     * Event handler for edit playlist button. Opens the playlist name editing window and
     * autofills the window with current name
     * @param actionEvent
     * @throws IOException
     */
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

    /**
     * Event handler for creating a new playlist. Opens the new playlist window and refreshes the playlist tableview
     * @param actionEvent
     * @throws IOException
     */
    public void NewPlaylistButton(ActionEvent actionEvent) throws IOException {
        Parent mainWindowParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/view/NewPlaylist.fxml"))); // The FXML path
        Scene mainWindowScene = new Scene(mainWindowParent); // Scene supposed to be viewed
        Stage newPlaylistStage = new Stage();
        newPlaylistStage.setScene(mainWindowScene); // Sets the new scene
        newPlaylistStage.showAndWait(); // This shows the new scene
        TVPlaylist.getItems().clear();
        TVPlaylist.setItems(playlistModel.getObservablePlaylists());
    }

    /**
     * Event handler for moving a song down on a playlist. will only allow you to move a song down if it is not already at the bottom
     * @param actionEvent
     * @throws Exception
     */
    public void DownButton(ActionEvent actionEvent) throws Exception {
        if (LVSongsOnPlaylist.getSelectionModel().getSelectedIndex()+1 != LVSongsOnPlaylist.getItems().size()){
            songMove(1);
        }
    }

    /**
     * Event handler for moving a song up on a playlist. Will only allow you to move the song if it is not already at the top.
     * @param actionEvent
     * @throws Exception
     */
    public void UpButton(ActionEvent actionEvent) throws Exception {
        if (LVSongsOnPlaylist.getSelectionModel().getSelectedIndex() !=0){
            songMove(-1);
        }
    }

    /**
     * Method for getting the selected song and playlist and moving the song.
     * @param upOrDown 1 to move down by one. -1 to move up by one.
     * @throws Exception
     */
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

    /**
     * Event handler for adding a song to a playlist
     * @param actionEvent
     * @throws SQLException
     */
    public void AddSongToPlaylistButton(ActionEvent actionEvent) throws SQLException {
        Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
        Song song = TVSongs.getSelectionModel().getSelectedItem();
        if (song != null && playlist != null){
            playlistModel.addSongToPlaylist(playlist,song);
            LVSongsOnPlaylist.getItems().add(song);
            TVPlaylist.refresh();
        }
    }

    /**
     * Event handler for deleting a song from a playlist.
     * @param actionEvent
     * @throws SQLException
     */
    public void DeleteSongFromPlaylistButton(ActionEvent actionEvent) throws SQLException {
        Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
        Song song = LVSongsOnPlaylist.getSelectionModel().getSelectedItem();
        int index = LVSongsOnPlaylist.getSelectionModel().getSelectedIndex()+1;
        if (song != null && playlist != null) {
            playlistModel.deleteSongFromPlaylist(playlist, song, index);
            LVSongsOnPlaylist.getItems().remove(song);
            TVPlaylist.getItems().clear();
            TVPlaylist.setItems(playlistModel.getObservablePlaylists());
        }
    }

    /**
     * Event handler for going back to the previous song
     * @param actionEvent
     */
    public void BackButton(ActionEvent actionEvent) {
        jukebox.backSong();
        jukebox.setVolume(volume);
        updateCurrentlyPlayinglabel();
    }

    /**
     * Event handler for skipping a song
     * @param actionEvent
     */
    public void SkipButton(ActionEvent actionEvent) {
        jukebox.skipSong();
        jukebox.setVolume(volume);
        updateCurrentlyPlayinglabel();
    }

    /**
     * Event handler for playing or pausing what is currently selected.
     * @param actionEvent
     */
    public void PlayPauseButton(ActionEvent actionEvent) {
        //for playing from playlist
        if (LVSongsOnPlaylist.getSelectionModel().getSelectedItem() != null && !jukebox.isPlaying()){
            int songIndex = LVSongsOnPlaylist.getSelectionModel().getSelectedIndex();
            Song selectedSong = jukebox.getSongList().get(songIndex);
            if (selectedSong != jukebox.getCurrentSong()){
                jukebox.setCurrentSong(selectedSong);
                LVSongsOnPlaylist.getSelectionModel().clearSelection();
            }
        }
        //for if the songlist is selected
        if (TVSongs.getSelectionModel().getSelectedItem() != null && !jukebox.isPlaying()){
            int songIndex = TVSongs.getSelectionModel().getFocusedIndex();
            Song selectedSong = jukebox.getSongList().get(songIndex);
                if (selectedSong != jukebox.getCurrentSong()){
                    jukebox.setCurrentSong(selectedSong);
                    TVSongs.getSelectionModel().clearSelection();
                }
        }
        jukebox.playPause();
        jukebox.setVolume(volume);
        updateCurrentlyPlayinglabel();
    }

    /**
     *Event handler for clicking the Song list. sets the song index when a song is clicked
     * @param mouseEvent
     */
    public void handleClickTVSongs(MouseEvent mouseEvent) {
        LVSongsOnPlaylist.getSelectionModel().clearSelection();
        int selectedIndex = TVSongs.getSelectionModel().getSelectedIndex();
        List<Song> selectedSongList = TVSongs.getItems();
        if (selectedSongList != jukebox.getSongList()){
            jukebox.setSongList(selectedSongList);
        }
        jukebox.setCurrentSongIndex(selectedIndex);
    }

    /**
     * Event handler for clicking the Songs on playlist listview.
     * @param mouseEvent
     */
    public void handleLVSongsOnPlaylistClicked(MouseEvent mouseEvent) {
        TVSongs.getSelectionModel().clearSelection();
        int selectedIndex = LVSongsOnPlaylist.getSelectionModel().getSelectedIndex();
        List<Song> selectedSongList = LVSongsOnPlaylist.getItems();
        if (selectedSongList != jukebox.getSongList()){
            jukebox.setSongList(selectedSongList);
        }
        jukebox.setCurrentSongIndex(selectedIndex);
    }

    /**
     * display what song is being played currently
     */
    private void updateCurrentlyPlayinglabel(){
        currentlyPlayingLabel.setText(jukebox.getCurrentSongTitle());
    }

    /**
     * Event handler for closing the program
     * @param actionEvent
     */
    public void CloseApplicationButton(ActionEvent actionEvent) {
        System.exit(1);
    }

    /**
     * Event handler for deleting a song from the song list. Opens a confirmation window before deleting song
     * @param actionEvent
     */
    public void DeleteSongButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Song song = TVSongs.getSelectionModel().getSelectedItem();
            songModel.deleteSong(song);
            TVSongs.getItems().remove(song);
        }
    }

    /**
     * Event Handler for editing an already existing song. opens the editing window and autofills the current information
     * @param event
     * @throws SQLException
     * @throws IOException
     */
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

    /**
     * Event handler for creating a new song. Opens the new song window
     * @param actionEvent
     * @throws IOException
     */
    public void NewSongButton (ActionEvent actionEvent) throws IOException {
        Parent mainWindowParent = FXMLLoader.load(getClass().getResource("/gui/view/NewSong.fxml")); // The FXML path
        Scene mainWindowScene = new Scene(mainWindowParent); // Scene supposed to be viewed
        Stage newSongStage = new Stage();
        newSongStage.setScene(mainWindowScene); // Sets the new scene
        newSongStage.showAndWait(); // This shows the new scene
        TVSongs.getItems().clear();
        TVSongs.setItems(songModel.getObservableSongs());
    }

    /**
     * event handler for clicking the playlist tableview. Displays all the songs from the clicked playlist
     * onto the Songs on playlist listview
     * @param mouseEvent
     */
    public void HandleTVPlaylistClicked(MouseEvent mouseEvent) {
        Playlist playlist = TVPlaylist.getSelectionModel().getSelectedItem();
        if (playlist != null)
        {
            LVSongsOnPlaylist.getItems().setAll(playlistModel.getSongsFromPlaylist(playlist));
        }
    }

    /**
     * event handler for moving the mouse. Updates the volume and what song is currently playing
     * @param mouseEvent
     */
    public void handleMouseMove(MouseEvent mouseEvent) {
        updateCurrentlyPlayinglabel();
        jukebox.setVolume(volume);
    }
}
