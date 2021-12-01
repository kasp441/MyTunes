package dal;

import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    private DatabaseDAO databaseConnector;

    public SongDAO() throws IOException {
        this.databaseConnector = new DatabaseDAO();
    }

    public List<Song> getAllSongs(){
        ArrayList<Song> allSongs = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sqlStatement = "SELECT * FROM Song";
            Statement statement = connection.createStatement();
            if (statement.execute(sqlStatement)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String artist = resultSet.getString("artist");
                    String genre = resultSet.getString("genre");
                    String destination = resultSet.getString("destination");
                    int playtime = resultSet.getInt("playtime");
                    int ID = resultSet.getInt("ID");

                    Song song = new Song(ID,title, artist, genre, destination, playtime); // Creating a song object from the retrieved values
                    allSongs.add(song); // Adding the song to an ArrayList
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allSongs;
    }

    public Song createSong(String title, String artist,String genre, int playtime, String destination) throws SQLException {
        int newestID = -1;
        String sql = "INSERT INTO Song(Title,Artist,Genre,Playtime,Destination) VALUES (?,?,?,?,?)";
        try(Connection connection = databaseConnector.getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,title);
            ps.setString(2,artist);
            ps.setString(3,genre);
            ps.setInt(4,playtime);
            ps.setString(5,destination);
            ps.addBatch();
            ps.executeBatch();

            sql = "SELECT TOP(1) * FROM Song ORDER by ID desc";
            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                newestID = rs.getInt("id");
            }

            ps.executeBatch();
        }
        return new Song(newestID,title,artist,genre,destination,playtime);

    }

    public static void main(String[] args) throws IOException, SQLException {
        SongDAO songDAO = new SongDAO();
        List<Song> allsongs = songDAO.getAllSongs();
        System.out.println(allsongs.size());
    }

}
