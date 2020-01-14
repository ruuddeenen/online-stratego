package interfaces;

import responses.UserResponse;

public interface IUserRepository {
    UserResponse register(String username, String password);
    UserResponse login(String username, String password);
}
