package bll;

import be.Song;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class SearchSong {

    public List<Song> searchSong(List<Song> songList, String query)
    {
        List<Song> result = new ArrayList<>();

        for (Song song : songList)
        {
            if (compareToTitle(song, query) || compareToArtist(song, query))
            {
                result.add(song);
            }
        }
        return result;
    }

    private boolean compareToArtist(Song song, String query)
    {
        return song.getArtist().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToTitle(Song song, String query)
    {
        return song.getTitle().toLowerCase().contains(query.toLowerCase());
    }
}
