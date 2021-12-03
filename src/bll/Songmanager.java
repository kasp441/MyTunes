package bll;

import be.Song;
import dal.SongDAO;
import javafx.collections.ObservableList;

import java.io.IOException;
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

    }
