package service.messages.outgoing.interfaces;

import models.Player;

public interface IEndGameResponse extends IPawnListResponse {
    Player getWinner();
    void setWinner(Player player);
}
