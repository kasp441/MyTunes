package bll;

import be.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchSong {
    public ObservableList<Song> searchSong(ObservableList<Song> songList, String keyChar)
    {
        ObservableList<Song> result = FXCollections.observableArrayList();

        for (Song song : songList)
        {
            if (keyChar.toLowerCase().contains(song.getTitle().toLowerCase()))
            {
                result.add(song);
            }
        }
        return result;
    }
}
