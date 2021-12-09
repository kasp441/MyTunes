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
            String sql2 = "SELECT * FROM PlaylistSongs WHERE Position >= ? AND playlistId = ?";
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.setInt(1,index);
            ps2.setInt(2,playlist.getId());
            ResultSet resultSet = ps2.executeQuery();

            //fill the position gap from the deleted song
            String sql3 = "UPDATE PlaylistSongs SET Position = ? WHERE Position = ?";
            PreparedStatement ps3 = connection.prepareStatement(sql3);
            while (resultSet.next()){
                int currentPos = resultSet.getInt("position");
                int newPos = resultSet.getInt("position")-1;
                ps3.setInt(1,newPos);
                ps3.setInt(2,currentPos);
                ps3.addBatch();
            }
            ps3.executeBatch();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
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

    public static void main(String[] args) throws IOException, SQLException {
        PlaylistDAO playlistDAO = new PlaylistDAO();
        Playlist playlist = playlistDAO.getAllPlaylists().get(0);
        System.out.println(playlistDAO.getNextPosition(playlist));
    }
}
