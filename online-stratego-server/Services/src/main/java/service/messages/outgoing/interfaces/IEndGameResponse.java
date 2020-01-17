package service.messages.outgoing.interfaces;

import models.Player;
import service.messages.both.IPawnList;

public interface IEndGameResponse extends IPawnList {
    Player getWinner();
    void setWinner(Player player);
}
