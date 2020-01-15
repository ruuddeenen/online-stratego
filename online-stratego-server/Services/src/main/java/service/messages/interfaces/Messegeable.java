package service.messages.interfaces;

import models.Player;

public interface Messegeable {
    Player getPlayer();
    void setPlayer(Player player);
    String getLobbyId();
    void setLobbyId(String lobbyId);
}
