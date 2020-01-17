package repositories;

import connection.ConnectionManager;
import interfaces.IUserRepository;
import responses.UserResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepositoryMySQL implements IUserRepository {
    private static Logger logger = Logger.getLogger(UserRepositoryMySQL.class.getName());
    @Override
    public UserResponse register(String username, String password) {
        String uuid = UUID.randomUUID().toString();
        String query = "insert into dbi366275.users (id, username, passwordHash) values (?,?,?)";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);

            preparedStatement.execute();
            return login(username, password);
        } catch (SQLException | IOException e) {
            logger.log(Level.SEVERE, null, e);
            UserResponse response = new UserResponse();
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @Override
    public UserResponse login(String username, String password) {
        String query = "select * from dbi366275.users where username = ? and passwordHash = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()) {
                    String id = resultSet.getString("id");
                    return new UserResponse(id, username);
                } else {
                    return new UserResponse();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return null;
    }
}
