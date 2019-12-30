package service.messages;

import models.Player;

import java.util.List;

public interface IGameMessage {
    String getLobbyId();
    void setLobbyId(String lobbyId);
    List<Player> getPlayerList();
    void setPlayerList(List<Player> playerList);
}
