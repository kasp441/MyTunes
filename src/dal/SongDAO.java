package dal;

import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SongDAO {
    private DatabaseDAO databaseConnector;

    public SongDAO() throws IOException {
        this.databaseConnector = new DatabaseDAO();
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
        Song song = songDAO.createSong("the new test","the testmen","punk",420,"/data/nowhere");
        System.out.println(song);
    }

}
