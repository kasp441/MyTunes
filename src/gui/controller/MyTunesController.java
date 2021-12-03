package gui.controller;

import be.Song;
import gui.model.SongModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;


public class MyTunesController {

    public TableColumn<Song, String> songTitle;
    public TableColumn<Song, String> songArtist;
    public TableColumn<Song, String> songCategory;
    public TableColumn<Song, Integer> songTime;
    public Label currentlyPlayingLabel;
    public TextField filterInput;
    private SongModel songModel;

    public TableView<be.Song> TVSongs;

    boolean playing;
    MediaPlayer player;
    int currentSongIndex;

    public MyTunesController() throws IOException {
        songModel = new SongModel();
        currentSongIndex = 0;

    }

    public void initialize() {
        songTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        songArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        songCategory.setCellValueFactory(new PropertyValueFactory<>("genre"));
        songTime.setCellValueFactory(new PropertyValueFactory<>("playtime"));
        TVSongs.setItems(songModel.getObservableSongs());
    }

    public void DeletePlaylistButton(ActionEvent actionEvent)
    {

    }

    public void EditPlaylistButton(ActionEvent actionEvent)
    {

    }
    
    public void NewPlaylistButton(ActionEvent actionEvent)
    {

    }

    public void DownButton(ActionEvent actionEvent)
    {

    }

    public void UpButton(ActionEvent actionEvent)
    {

    }

    public void FilterButton(ActionEvent actionEvent)
    {
        songModel.searchSwitch(filterInput.toString());
        TVSongs.refresh();
    }

    public void AddSongToPlaylistButton(ActionEvent actionEvent)
    {

    }

    public void DeleteSongFromPlaylistButton(ActionEvent actionEvent)
    {

    }

    public void BackButton(ActionEvent actionEvent)
    {
        currentSongIndex--;
        if (currentSongIndex >= 0){
            player.stop();
            playMusic();
        }
        else{
            currentSongIndex--;
        }


    }

    public void SkipButton(ActionEvent actionEvent)
    {
        currentSongIndex++;
        if (currentSongIndex <= TVSongs.getItems().size()-1){
            player.stop();
            playMusic();
        }
        else{
            currentSongIndex--;
        }
    }

    public void PlayPauseButton(ActionEvent actionEvent)
    {
        if (!playing){
            playMusic();
        }else{
            player.stop();
            playing = false;
            currentlyPlayingLabel.setText("(none)... is playing");
        }
    }

    private void playMusic(){
        playing = true;
        Song currentSong = TVSongs.getItems().get(currentSongIndex);
        String path = currentSong.getDestination();
        File file = new File(path);
        Media media = new Media(file.toURI().toString());
        player = new MediaPlayer(media);
        currentlyPlayingLabel.setText(currentSong.getTitle());
        player.play();
    }

    public void getClickedSong(MouseEvent mouseEvent) {
        currentSongIndex = TVSongs.getSelectionModel().getSelectedIndex();
    }

    public void CloseApplicationButton(ActionEvent actionEvent)
    {

    }

    public void DeleteSongButton(ActionEvent actionEvent)
    {

    }

    public void EditSongButton(ActionEvent actionEvent)
    {

    }

    public void NewSongButton(ActionEvent actionEvent)
    {

    }



}
