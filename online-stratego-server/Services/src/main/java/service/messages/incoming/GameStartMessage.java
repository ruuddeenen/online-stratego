package service.messages;

import models.Player;

import java.util.List;

public class GameStartMessage implements IGameMessage {
    private String lobbyId;
    private List<Player> playerList;

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

}
