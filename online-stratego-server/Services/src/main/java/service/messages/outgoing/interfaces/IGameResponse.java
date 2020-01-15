package service.messages.outgoing.interfaces;

import models.enums.Color;
import models.pawns.Pawn;

import java.util.List;

public interface IGameResponse extends IPawnListResponse {
    void setTurn(Color color);

    Color getTurn();

    void setDefeatedPawnList(List<Pawn> defeatedPawnList);

    List<Pawn> getDefeatedPawnList();
}
