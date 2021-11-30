package dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseDAO
    {
    private SQLServerDataSource dataSource;


    public DatabaseDAO() {
        dataSource = new SQLServerDataSource();
        dataSource.setServerName("10.176.111.31");
        dataSource.setDatabaseName("VoresMyTunesDatabase");
        dataSource.setUser("CSe21A_22");
        dataSource.setPassword("Rackreaver");
        dataSource.setPortNumber(1433);
    }
    //Creating a connection to the database
    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }
    // Test if the connection to the database works
        public static void main(String[] args) throws SQLException {
        DatabaseDAO databaseConnector = new DatabaseDAO();
        Connection connection = databaseConnector.getConnection();

            System.out.println("Did it open? " + !connection.isClosed());
            connection.close();
        }
}

/*


Bedre måde at connect på, da man ikke smider sine oplysninger ud på github.


public class DatabaseDAO {


    private static final String PROP_FILE = "src/sample/dal/database.settings";
    private SQLServerDataSource ds;

    public DatabaseConnector() throws IOException
    {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream(PROP_FILE));
        ds = new SQLServerDataSource();
        ds.setServerName(databaseProperties.getProperty("Server"));
        ds.setDatabaseName(databaseProperties.getProperty("Database"));
        ds.setUser(databaseProperties.getProperty("User"));
        ds.setPassword(databaseProperties.getProperty("Password"));
    }

    public Connection getConnection() throws SQLServerException
    {
        return ds.getConnection();
    }

}
Server=10.176.111.31
Database=MovieProjectExam2021
User=CSe20A_19
Password=ThiimErDejlig
 */