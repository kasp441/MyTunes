package bll;

import be.Song;
import javafx.collections.ObservableList;

public class SearchSong {

    public ObservableList<Song> searchSong(ObservableList<Song> songList, String keyChar)
    {
        ObservableList<Song> resultSet = null;
        for (Song song : songList)
        {
            if (keyChar.toLowerCase().contains(song.getTitle().toLowerCase()))
            {
                resultSet.add(song);
            }
        }
        return resultSet;
    }
}
