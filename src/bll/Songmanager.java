package bll;

import be.Song;
import bll.util.SearchSong;
import dal.SongDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Songmanager {
     SongDAO songDAO;
     SearchSong filter;

        public Songmanager() throws IOException {
            songDAO = new SongDAO();
            filter = new SearchSong();
        }

    /**
     * gets a list of all songs from database
     * @return
     */
    public List<Song> getAllSongs()
    {
        return songDAO.getAllSongs();
    }

    /**
     * parses the searching method
     * @param songList
     * @param keyChar
     * @return
     */
    public List<Song> getSearchedSongs(List<Song> songList, String keyChar)
    {
        return filter.search(songList, keyChar);
    }

    /**
     * creates a new song in the database
     * @param title
     * @param artist
     * @param genre
     * @param destination
     * @throws SQLException
     */
    public void createSong(String title, String artist, String genre, int playtime, String destination) throws SQLException {
            songDAO.createSong(title, artist, genre, playtime, destination);
    }

    /**
     * updates the song in the database
     * @param songUpdate the song containing the new properties
     * @throws SQLException
     */
    public void updateSong(Song songUpdate) throws SQLException{
            songDAO.updateSong(songUpdate);
    }

    /**
     * removes a song form the database
     * @param songDelete the song containing the properties to be removed
     */
    public void deleteSong(Song songDelete) {
            songDAO.deleteSong(songDelete);
    }

}
