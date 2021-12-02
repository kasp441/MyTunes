package gui.controller;

import be.Song;
import gui.model.SongModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    SongModel songModel;

    public TableView<be.Song> TVSongs;

    boolean playing;
    MediaPlayer player;

    public MyTunesController() throws IOException {
        songModel = new SongModel();
        playing = false;
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

    }

    public void AddSongToPlaylistButton(ActionEvent actionEvent)
    {

    }

    public void DeleteSongFromPlaylistButton(ActionEvent actionEvent)
    {

    }

    public void BackButton(ActionEvent actionEvent)
    {

    }

    public void PlayPauseButton(ActionEvent actionEvent)
    {
        if (!playing){
            playMusic();
            playing = true;
        }else{
            player.stop();
            playing = false;
        }
    }

    private void playMusic(){
        Song selectedSong = TVSongs.getSelectionModel().getSelectedItem();
        String path = selectedSong.getDestination();
        File file = new File(path);
        Media media = new Media(file.toURI().toString());
        player = new MediaPlayer(media);

        currentlyPlayingLabel.setText(selectedSong.getTitle());
        player.play();
    }


    public void SkipButton(ActionEvent actionEvent)
    {

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
