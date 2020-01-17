package service.messages.outgoing.models;

import models.Player;
import service.messages.Operation;
import service.messages.both.IPlayerList;

import java.util.List;

public class PlayerListResponse extends Response implements IPlayerList {
    private List<Player> playerList;

    public PlayerListResponse(Operation operation, String sender, String lobbyId, List<Player> playerList) {
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
