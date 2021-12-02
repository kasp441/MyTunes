package bll;

import dal.PlaylistDAO;

import java.io.IOException;

public class PlaylistManager {
    private final PlaylistDAO playlistDAO;

    public PlaylistManager() throws IOException {
    playlistDAO = new PlaylistDAO(); //This creates a new object


}
    public void addSongToPlaylist(int playlistId, int songId) {
        playlistDAO.addSongToPlaylist(playlistId, songId);
    }
}
