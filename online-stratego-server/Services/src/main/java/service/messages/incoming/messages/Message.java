package service.messages.incoming.messages;

import models.Player;
import service.messages.incoming.interfaces.Messegeable;

public class Message implements Messegeable {
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    private String lobbyId;
}
