package service.messages.responses;

import models.Player;
import service.Operation;
import service.messages.responses.ResponseMessage;

import java.util.List;

public class LobbyResponseMessage extends ResponseMessage {
    private List<Player> playerList;

    public LobbyResponseMessage(Operation operation, String sender, String lobbyId, List<Player> playerList) {
        super(operation, sender, lobbyId);
        this.playerList = playerList;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

}
