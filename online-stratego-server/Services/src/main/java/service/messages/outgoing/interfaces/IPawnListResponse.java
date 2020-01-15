package service.messages.outgoing.interfaces;

import models.pawns.Pawn;

import java.util.List;

public interface IPawnListResponse{
    List<Pawn> getPawnList();
    void setPawnList(List<Pawn> pawnList);
}
