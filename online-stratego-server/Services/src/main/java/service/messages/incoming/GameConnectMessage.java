package service.messages.incoming;

import models.enums.Color;

public class GameConnectMessage extends ConnectMessage {
    private String id;
    private String username;
    private String lobbyId;
    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String getLobbyId() {
        return lobbyId;
    }

    @Override
    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
