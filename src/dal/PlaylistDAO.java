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
                allPlaylists.add(pl);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return allPlaylists;
    }

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

    public void deletePlaylist(Playlist playlistDelete) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE from Playlist WHERE Id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString(1, Integer.toString(playlistDelete.getId()));
            preparedStmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void addSongToPlaylist(int playlistId, int songId)
    {
        //Insert into SQL kommando, hvori at playlistID og songID bliver smidt ind
        String sql = "INSERT INTO PlaylistSongs(playlistId, songId) VALUES (?, ?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Sætter parametre
            preparedStatement.setInt(1, playlistId);
            preparedStatement.setInt(2, songId);
            preparedStatement.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        PlaylistDAO playlistDAO = new PlaylistDAO();
        playlistDAO.addSongToPlaylist(1, 42);
    }

    public List<Song> getSongsFromPlaylist(Playlist playlist) {
        List<Song> songsOnPlaylist = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM PlaylistSongs WHERE PlaylistId = ?";
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

}
