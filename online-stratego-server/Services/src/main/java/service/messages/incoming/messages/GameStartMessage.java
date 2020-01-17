package service.messages.incoming.messages;

import models.Player;
import service.messages.both.IPlayerList;

import java.util.List;

public class GameStartMessage extends Message implements IPlayerList {

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
