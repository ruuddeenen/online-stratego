package service.messages.outgoing.interfaces;

import models.Player;
import models.pawns.Pawn;

import java.util.List;

public interface IGameStartResponse extends IPawnListResponse {
    void setFields(boolean[][] fields);

    boolean[][] getFields();

    Player getOpponent();

    void setOpponent(Player opponent);
}
