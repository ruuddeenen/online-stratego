package service.messages.interfaces;

public interface IConnectMessage extends ILobbyMessage {
    void setId(String id);

    String getId();

    void setUsername(String username);

    String getUsername();
}
