package bll;

import be.Song;
import dal.SongDAO;
import javafx.collections.ObservableList;

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

        public List<Song> getAllSongs(){
            return songDAO.getAllSongs();
        }
        public ObservableList<Song> getSearchedSong(ObservableList<Song> songList, String keyChar)
        {
            return filter.searchSong(songList, keyChar);
        }

    public void createSong(String title, String artist, String genre, int playtime, String destination) throws SQLException {
            songDAO.createSong(title, artist, genre, playtime, destination);
    }

    public void updateSong(Song songUpdate) throws SQLException{
            songDAO.updateSong(songUpdate);
    }

    public void deleteSong(Song songDelete) {
            songDAO.deleteSong(songDelete);
    }

}
