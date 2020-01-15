package service.messages.outgoing.interfaces;

import models.Player;

import java.util.List;

public interface IPlayerListResponse {
    void setPlayerList(List<Player> playerList);
    List<Player> getPlayerList();
}
