package service.messages.outgoing.interfaces;

import models.Player;
import service.messages.both.IPawnList;

public interface IGameStartResponse extends IPawnList {
    void setFields(boolean[][] fields);

    boolean[][] getFields();

    Player getOpponent();

    void setOpponent(Player opponent);
}
