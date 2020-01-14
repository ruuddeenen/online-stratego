package connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {
    private static Connection connection;
    private static Logger logger = Logger.getLogger(ConnectionManager.class.getName());

    private ConnectionManager() {
        throw new IllegalStateException("Utility class");
    }

    public static Connection getConnection() throws IOException {
        try (FileInputStream input = new FileInputStream("db.prop")) {
            Properties prop = new Properties();

            try {
                prop.load(input);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }

            String host = prop.getProperty("jdbc.host");
            String username = prop.getProperty("jdbc.username");
            String password = prop.getProperty("jdbc.password");

            try {
                connection = DriverManager.getConnection(host, username, password);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, null, e);
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return connection;
    }
}
