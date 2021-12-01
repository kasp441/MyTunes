package dal;

import be.Playlist;

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
                int totallenght = rs.getInt("totallenght");
                Playlist pl = new Playlist(id, name, totallenght);
                allPlaylists.add(pl);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return allPlaylists;
    }

    public Playlist createPlaylist(String name) {
        String sql = "INSERT INTO Playlist(name) VALUES (?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setString(1, name);
            prepStatement.addBatch();
            prepStatement.executeBatch();
        }catch (SQLException ex) {
            System.out.println(ex);
        }
        Playlist playlist = new Playlist(0, name);
        return playlist;
    }

}
