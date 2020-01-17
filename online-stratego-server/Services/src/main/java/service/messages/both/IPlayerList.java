package service.messages.both;

import models.Player;

import java.util.List;

public interface IPlayerList {
    void setPlayerList(List<Player> playerList);
    List<Player> getPlayerList();
}
