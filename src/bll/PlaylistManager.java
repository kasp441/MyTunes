package bll;

import be.Playlist;
import dal.PlaylistDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PlaylistManager {
    private final PlaylistDAO playlistDAO;

    public PlaylistManager() throws IOException {
    playlistDAO = new PlaylistDAO(); //This creates a new object
}
    public List<Playlist> getAllPlaylists()
    {
    return playlistDAO.getAllPlaylists();
    }

    public void addSongToPlaylist(int playlistId, int songId)
    {
        playlistDAO.addSongToPlaylist(playlistId, songId);
    }

    public void createPlaylist(String playlistName, int totallenght, int totalsongs) throws SQLException
    {
        playlistDAO.createPlaylist(playlistName, totallenght, totalsongs);
    }

    public void updatePlaylist(Playlist playlistUpdate) throws SQLException {
        playlistDAO.updatePlaylist(playlistUpdate);
    }

    public void deletePlaylist(Playlist playlistDelete) {
        playlistDAO.deletePlaylist(playlistDelete);
    }

}
