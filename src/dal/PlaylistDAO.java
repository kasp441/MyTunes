package dal;

import be.Playlist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}
