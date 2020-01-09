package service.messages.incoming;

import models.Player;
import service.messages.interfaces.IPlayerMessage;

import java.util.List;

public class GameStartMessage implements IPlayerMessage {
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
