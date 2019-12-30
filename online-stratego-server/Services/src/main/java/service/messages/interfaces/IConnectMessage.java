package service.messages;

public interface IConnectMessage {
    void setId(String id);

    String getId();

    void setUsername(String username);

    String getUsername();

    void setLobbyId(String lobbyId);

    String getLobbyId();
}
