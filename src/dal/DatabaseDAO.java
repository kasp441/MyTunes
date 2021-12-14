package dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseDAO
    {
    private SQLServerDataSource dataSource;

        /**
         * Logs into the database with login information from a text document
         * @throws IOException
         */
    public DatabaseDAO() throws IOException {
        Properties props = new Properties();
        props.load(new FileReader("loginInfo.txt"));
        dataSource = new SQLServerDataSource();
        dataSource.setServerName(props.getProperty("Server"));
        dataSource.setDatabaseName(props.getProperty("Database"));
        dataSource.setUser(props.getProperty("User"));
        dataSource.setPassword(props.getProperty("Password"));
    }

        /**
         * creates a connection with the database
         * @return a connection
         * @throws SQLServerException
         */
    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }
    // Test if the connection to the database works
        public static void main(String[] args) throws SQLException, IOException {
        DatabaseDAO databaseConnector = new DatabaseDAO();
        Connection connection = databaseConnector.getConnection();

            System.out.println("Did it open? " + !connection.isClosed());
            connection.close();
        }
}