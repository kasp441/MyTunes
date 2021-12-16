package bll.util;

import be.Song;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class SearchSong {

    /**
     * compares songList with a String from query
     * @param songList
     * @param query
     * @return a list of songs containing the String
     */
    public List<Song> search(List<Song> songList, String query)
    {
        List<Song> result = new ArrayList<>();

        for (Song song : songList)
        {
            if (compareToTitle(song, query) || compareToArtist(song, query) || compareToCategory(song, query))
            {
                result.add(song);
            }
        }
        return result;
    }

    /**
     * checks to see if the song contains the String
     * @param song
     * @param query
     * @return true if the song contains all the letters
     */
    private boolean compareToArtist(Song song, String query)
    {
        return song.getArtist().toLowerCase().contains(query.toLowerCase());
    }

    /**
     * checks to see if the song contains the String
     * @param song
     * @param query
     * @return true if the song contains all the letters
     */
    private boolean compareToTitle(Song song, String query)
    {
        return song.getTitle().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToCategory(Song song, String query)
    {
        return song.getGenre().toLowerCase().contains(query.toLowerCase());
    }
}
