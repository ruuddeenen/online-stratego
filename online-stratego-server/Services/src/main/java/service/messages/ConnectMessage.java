package service.messages;

public class ConnectMessage {
    int id;
    String username;
    String lobbyId;

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }


    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

}
