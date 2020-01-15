package service.messages.interfaces;

import models.Player;

import java.util.List;

public interface IGameStartMessage {
    List<Player> getPlayerList();
    void setPlayerList(List<Player> playerList);
}
