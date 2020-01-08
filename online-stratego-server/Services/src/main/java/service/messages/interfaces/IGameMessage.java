package service.messages.interfaces;

import models.Player;

import java.util.List;

public interface IGameMessage extends ILobbyMessage {
    List<Player> getPlayerList();
    void setPlayerList(List<Player> playerList);
}
