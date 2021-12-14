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

    /**
     *
     * @return a list of songs from the database
     */
    public List<Playlist> getAllPlaylists()
    {
    return playlistDAO.getAllPlaylists();
    }

    /**
     * adds songs to a playlist in the database
     * @param playlist
     * @param song
     * @throws SQLException
     */
    public void addSongToPlaylist(Playlist playlist, Song song) throws SQLException {
        playlistDAO.addSongToPlaylist(playlist, song);
    }

    /**
     * deletes a song from a playlist in the database
     * @param playlist
     * @param song
     * @param index
     * @throws SQLException
     */
    public void deleteSongFromPlaylist(Playlist playlist, Song song, int index) throws SQLException {
        playlistDAO.deleteSongFromPlaylist(playlist, song, index);
    }

    /**
     * creates a playlist
     * @param playlistName
     * @return
     * @throws SQLException
     */
    public Playlist createPlaylist(String playlistName) throws SQLException
    {
        return playlistDAO.createPlaylist(playlistName);
    }

    /**
     * updates a specif playlist with new values in the database
     * @param playlistUpdate playlist with the new values
     * @throws SQLException
     */
    public void updatePlaylist(Playlist playlistUpdate) throws SQLException {
        playlistDAO.updatePlaylist(playlistUpdate);
    }

    /**
     * deletes a playlist from the database
     * @param playlistDelete the specific playlist to be removed
     * @return
     */
    public Playlist deletePlaylist(Playlist playlistDelete) {
        return playlistDAO.deletePlaylist(playlistDelete);
    }

    /**
     *
     * @param playlist the playlist to get the song list from
     * @return returns a list of songs from playlist
     */
    public List<Song> getSongsFromPlaylist(Playlist playlist){
        return playlistDAO.getSongsFromPlaylist(playlist);
    }

    public void moveSongsOnPlaylist(Playlist playlist, int i, int j) throws Exception {
        playlistDAO.moveSongsOnPlaylist(playlist, i, j);
    }
}
