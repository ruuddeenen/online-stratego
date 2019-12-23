package repositories;

import connection.ConnectionManager;
import interfaces.IUserRepository;
import responses.UserResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserRepository implements IUserRepository {
    @Override
    public UserResponse register(String username, String password) {
        String uuid = UUID.randomUUID().toString();
        //language=MySQL
        String query = "insert into dbi366275.users (id, username, passwordHash) values (?,?,?)";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);

            preparedStatement.execute();
            return login(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserResponse login(String username, String password) {
        String query = "select * from dbi366275.users where username = ? and passwordHash = ?";

        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                String id = resultSet.getString("id");
                return new UserResponse(id, username);
            } else {
                return register(username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
