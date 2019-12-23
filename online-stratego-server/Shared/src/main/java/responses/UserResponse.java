package responses;

public class UserResponse {
    private final String id;
    private final String username;

    public UserResponse(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }
}
