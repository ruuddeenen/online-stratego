package service.messages.incoming.interfaces;

import models.pawns.Pawn;

import java.util.List;

public interface IGameMessage {
    List<Pawn> getPawnList();
    void setPawnList(List<Pawn> pawnList);
}