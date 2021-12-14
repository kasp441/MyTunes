package dal;

import be.Playlist;
import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

    private DatabaseDAO databaseConnector;

    public PlaylistDAO() throws IOException {
        databaseConnector = new DatabaseDAO(); // Creates the connection
    }

    /**
     * @return All playlists from the database
     */
    public List<Playlist> getAllPlaylists() {
        List<Playlist> allPlaylists = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection()) {
            String sqlStatement = "SELECT * FROM Playlist";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                String name = rs.getString("PlaylistName");
                int id = rs.getInt("Id");
                Playlist pl = new Playlist(id, name);
                for (Song song : getSongsFromPlaylist(pl))
                {
                    pl.addSongToPlaylist(song);
                }
                allPlaylists.add(pl);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return allPlaylists;
    }

    /**
     *
     * @param playlistName input for the name of the new playlist
     * @return  returns the newly made playlist
     * @throws SQLException
     */


    public Playlist createPlaylist(String playlistName) throws SQLException {
        int newID = -1;
        String sql = "INSERT INTO Playlist(PlaylistName, Totallenght, TotalSongs) VALUES (?, ?, ?)";
        try(Connection connection = databaseConnector.getConnection()) {
            PreparedStatement prepstatement = connection.prepareStatement(sql);
            prepstatement.setString(1, playlistName);
            prepstatement.setInt(2, 0);
            prepstatement.setInt(3, 0);
            prepstatement.addBatch();
            prepstatement.executeBatch();

            sql = "SELECT TOP(1) * FROM Playlist ORDER by Id desc";
            prepstatement = connection.prepareStatement(sql);
            ResultSet rs = prepstatement.executeQuery();
            while (rs.next()) {
                newID = rs.getInt("id");
            }
            prepstatement.executeBatch();
        }
            return new Playlist(newID, playlistName);
    }

    /**
     * Updates a playlist by overriding the current object with a new one
     * @param playlistUpdate the new playlist
     * @throws SQLException
     */
    public void updatePlaylist(Playlist playlistUpdate) throws SQLException {
        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Playlist SET PlaylistName=?, Totallenght=?, Totalsongs=? WHERE Id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, playlistUpdate.getPlaylistname());
            ps.setInt(2, playlistUpdate.getTotallenght());
            ps.setInt(3, playlistUpdate.getTotalSongs());
            ps.setInt(4, playlistUpdate.getId());
            if (ps.executeUpdate() != 1) {
                throw new Exception("Could not update playlist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a playlist from the database
     * @param playlistDelete the playlist object to be deleted
     * @return
     */
    public Playlist deletePlaylist(Playlist playlistDelete) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sqLang = "DELETE from PlaylistSongs WHERE PlaylistID = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(sqLang);
            preparedStmt.setString(1, Integer.toString(playlistDelete.getId()));
            preparedStmt.execute();

            String sql = "DELETE from Playlist WHERE Id = ?";
            preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString(1, Integer.toString(playlistDelete.getId()));
            preparedStmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return playlistDelete;
    }

    /**
     * Adds a selected song to a selected playlist by pairing the selected playlist id with the selected song id
     * @param playlist the selected playlist
     * @param song the selected song
     * @throws SQLException
     */
    public void addSongToPlaylist(Playlist playlist, Song song) throws SQLException {
        //Insert into SQL kommando, hvori at playlistID og songID bliver smidt ind
        String sql = "INSERT INTO PlaylistSongs(playlistId, songId, Position) VALUES (?, ?,?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Sætter parametre
            preparedStatement.setInt(1, playlist.getId());
            preparedStatement.setInt(2, song.getID());
            preparedStatement.setInt(3, getNextPosition(playlist));
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        playlist.addSongToPlaylist(song);
        updatePlaylist(playlist);
    }

    /**
     * Finds the next position for a song on a given playlist
     * @param playlist they playlist you want a new position for
     * @return the next position
     * @throws SQLException
     */
    public int getNextPosition(Playlist playlist) throws SQLException {
        int nextPos = 0;
        String sql = "SELECT MAX(Position) AS LastPos FROM PlaylistSongs WHERE PlaylistId = ?";
        try (Connection connection = databaseConnector.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playlist.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                nextPos = resultSet.getInt("LastPos")+1;
            }
        }
        return  nextPos;
    }

    /**
     * Deletes a song from a playlist based on the selected playlists id and songs id and position on the playlist.
     * After a song has been deleted, any song on the playlist with an id higher than the selected song will be adjusted.
     * @param playlist the playlist you want to delete from
     * @param song the song you want to delete
     * @param index the index on the playlist you want to delete
     */
    public void deleteSongFromPlaylist(Playlist playlist, Song song, int index) {
        //delete the selected song
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE FROM PlaylistSongs WHERE playlistId = ? AND songId = ? AND Position = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, playlist.getId());
            ps.setInt(2, song.getID());
            ps.setInt(3, index);
            ps.execute();

            //get resultset of anything with higher position than the deleted song
            String sql2 = "SELECT * FROM PlaylistSongs WHERE Position >= ? AND playlistId = ? ORDER BY Position";
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.setInt(1,index);
            ps2.setInt(2,playlist.getId());
            ResultSet resultSet = ps2.executeQuery();

            //fill the position gap from the deleted song
            String sql3 = "UPDATE PlaylistSongs SET Position = ? WHERE Position = ? AND playlistId = ?";
            PreparedStatement ps3 = connection.prepareStatement(sql3);
            while (resultSet.next()){
                int currentPos = resultSet.getInt("position");
                int newPos = resultSet.getInt("position")-1;
                ps3.setInt(1,newPos);
                ps3.setInt(2,currentPos);
                ps3.setInt(3,playlist.getId());
                ps3.addBatch();
            }
            ps3.executeBatch();
            playlist.removeSongFromPlaylist(song);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Selects a result set containing all the songids on a given playlist.
     * For every line in the result set a new song object will be created from the songid and get added to the return list
     * @param playlist the playlist you want to find the song on
     * @return a list of song objects matching the song ids assigned to the given playlist
     */
    public List<Song> getSongsFromPlaylist(Playlist playlist) {
        List<Song> songsOnPlaylist = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM PlaylistSongs WHERE PlaylistId = ? ORDER BY Position";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, playlist.getId());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("SongId");
                String songSql = "SELECT top(1) * FROM Song WHERE ID = ?";
                PreparedStatement songPs = connection.prepareStatement(songSql);
                songPs.setInt(1, id);
                ResultSet rSet = songPs.executeQuery();

                rSet.next();
                int ID = rSet.getInt("ID");
                String title = rSet.getString("Title");
                String artist = rSet.getString("Artist");
                String genre = rSet.getString("Genre");
                String destination = rSet.getString("Destination");
                int playtime = rSet.getInt("Playtime");
                Song song = new Song(ID, title, artist, genre, destination, playtime);

                songsOnPlaylist.add(song);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return songsOnPlaylist;
    }

    public void moveSongsOnPlaylist(Playlist playlist, int i, int j) throws Exception {
        try (Connection c = databaseConnector.getConnection()) {
            int pId = playlist.getId();
            int index1 = i;
            int index2 = j;

            String sql = "UPDATE PlaylistSongs SET Position = -1 WHERE PlaylistId = ? AND Position = ?";
            String sql1 = "UPDATE PlaylistSongs SET Position = ? WHERE PlaylistId = ? AND Position = ?";
            String sql2 = "UPDATE PlaylistSongs SET Position = ? WHERE PlaylistId = ? AND Position = -1";

            PreparedStatement ps = c.prepareStatement(sql);
            PreparedStatement ps1 = c.prepareStatement(sql1);
            PreparedStatement ps2 = c.prepareStatement(sql2);

            ps.setInt(1, pId);
            ps.setInt(2, index2);

            ps1.setInt(1, index2);
            ps1.setInt(2, pId);
            ps1.setInt(3, index1);

            ps2.setInt(1, index1);
            ps2.setInt(2, pId);

            ps.executeUpdate();
            ps1.executeUpdate();
            ps2.executeUpdate();
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        PlaylistDAO playlistDAO = new PlaylistDAO();
        Playlist playlist = playlistDAO.getAllPlaylists().get(0);
        System.out.println(playlistDAO.getNextPosition(playlist));
    }
}
