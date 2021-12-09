package bll;

import be.Playlist;
import be.Song;
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

    public void addSongToPlaylist(Playlist playlist, Song song)
    {
        playlistDAO.addSongToPlaylist(playlist, song);
    }

    public Playlist createPlaylist(String playlistName) throws SQLException
    {
        return playlistDAO.createPlaylist(playlistName);
    }

    public void updatePlaylist(Playlist playlistUpdate) throws SQLException {
        playlistDAO.updatePlaylist(playlistUpdate);
    }

    public Playlist deletePlaylist(Playlist playlistDelete) {
        return playlistDAO.deletePlaylist(playlistDelete);
    }

    public List<Song> getSongsFromPlaylist(Playlist playlist){
        return playlistDAO.getSongsFromPlaylist(playlist);
    }

}
