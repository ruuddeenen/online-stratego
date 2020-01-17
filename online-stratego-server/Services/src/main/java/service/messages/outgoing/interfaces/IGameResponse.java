package service.messages.outgoing.interfaces;

import models.enums.Color;
import models.pawns.Pawn;
import service.messages.both.IPawnList;

import java.util.List;

public interface IGameResponse extends IPawnList {
    void setTurn(Color color);

    Color getTurn();

    void setDefeatedPawnList(List<Pawn> defeatedPawnList);

    List<Pawn> getDefeatedPawnList();
}
