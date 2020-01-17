package responses;

public class UserResponse {
    private final String id;
    private final String username;
    private String message;

    public UserResponse(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserResponse(){
        this("null", "null");
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {return message;}
    public void setMessage(String message){
        this.message = message;
    }
}
