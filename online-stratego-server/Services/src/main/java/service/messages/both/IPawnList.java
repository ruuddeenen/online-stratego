package service.messages.both;

import models.pawns.Pawn;

import java.util.List;

public interface IPawnList {
    List<Pawn> getPawnList();
    void setPawnList(List<Pawn> pawnList);
}
