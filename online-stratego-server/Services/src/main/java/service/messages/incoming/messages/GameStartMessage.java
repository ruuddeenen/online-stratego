package service.messages.incoming.messages;

import models.Player;
import service.messages.incoming.interfaces.IGameStartMessage;

import java.util.List;

public class GameStartMessage extends Message implements IGameStartMessage {

    @Override
    public List<Player> getPlayerList() {
        return playerList;
    }

    @Override
    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    private List<Player> playerList;
}
