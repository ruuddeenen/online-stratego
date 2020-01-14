package service.messages.interfaces;

import models.Player;

import java.util.List;

public interface IPlayerMessage extends ILobbyMessage {
    List<Player> getPlayerList();
    void setPlayerList(List<Player> playerList);
}
