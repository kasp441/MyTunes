package dal;

import be.Song;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    private DatabaseDAO databaseConnector;

    public SongDAO() throws IOException {
        this.databaseConnector = new DatabaseDAO();
    }

    /**
     * Creates a list of song objects from the database by making a song object out of every
     * line in the "Song" table.
     * @return a list containing every song
     */
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

    /**
     * Creates a song by writing all the input data it to the database except the id.
     * The id is incremented by the database and returned to the application,
     * and finally used to create a song object
     * @param title The title of your song
     * @param artist The artist of the song
     * @param genre the Genre of the song
     * @param playtime For how long the song plays
     * @param destination Where is the mp3/wav file located
     * @return A song object
     * @throws SQLException
     */
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

    /**
     * overrides an existing song with a new song object
     * @param songUpdate the new song object
     * @throws SQLException
     */
    public void updateSong(Song songUpdate) throws SQLException{
        try(Connection connection = databaseConnector.getConnection()){
            String sql = "UPDATE Song SET Title=?, Artist=?, Genre=?, Playtime=?,Destination=? WHERE ID=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, songUpdate.getTitle());
            preparedStatement.setString(2, songUpdate.getArtist());
            preparedStatement.setString(3, songUpdate.getGenre());
            preparedStatement.setInt(4, songUpdate.getPlaytime());
            preparedStatement.setString(5, songUpdate.getDestination());
            preparedStatement.setInt(6, songUpdate.getID());
            if (preparedStatement.executeUpdate() != 1) {
                throw new Exception("Could not update song");
            }
        } catch (Exception e) {
            e.printStackTrace();
    }
}

    /**
     * deletes a song from the "Song" table in the database where the song ID matches
     * @param songDelete
     */
    public void deleteSong(Song songDelete) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE from Song WHERE ID = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString(1, Integer.toString(songDelete.getID()));
            preparedStmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }


    public static void main(String[] args) throws IOException, SQLException {
        SongDAO songDAO = new SongDAO();
        songDAO.createSong("Take Me Away", "Tobjan","indie",187,"Data/Take Me Away.wav");


        //Test songs for database
        /*
        songDAO.createSong("Adventures-Argofox","A-Himitsu","experimental",420,"data/A-Himitsu-Adventures-Argofox.mp3");
        songDAO.createSong("A new beginning","bensound","experimental",420,"data/bensound-anewbeginning.wav");
        songDAO.createSong("creativeminds","bensound","experimental",420,"data/bensound-creativeminds.wav");
        songDAO.createSong("memories","bensound","experimental",420,"data/bensound-memories.wav");
        songDAO.createSong("Into-Oblivion","Darren Curtis","experimental",420,"data/Into-Oblivion.mp3");
        songDAO.createSong("jingle-bells-violin-main","GoodBMusic","experimental",420,"data/jingle-bells-violin-main.mp3");
         */
    }

}
