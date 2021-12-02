package gui.controller;

import be.Song;
import gui.model.SongModel;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;


public class MyTunesController {

    public TableColumn<Song, String> songTitle;
    public TableColumn<Song, String> songArtist;
    public TableColumn<Song, String> songCategory;
    public TableColumn<Song, Integer> songTime;
    SongModel songModel;

    public TableView<be.Song> TVSongs;

    public MyTunesController() throws IOException {
        songModel = new SongModel();
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
