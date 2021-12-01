package bll;


import be.Song;
import dal.SongDAO;

import java.io.IOException;
import java.util.List;

public class Songmanager {
    SongDAO songDAO;

    public Songmanager() throws IOException {
        songDAO = new SongDAO();
    }

    public List<Song> getAllSongs(){
        return songDAO.getAllSongs();
    }

}
