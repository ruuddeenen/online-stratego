package service.messages.interfaces;

import models.Pawn.Pawn;

import java.util.List;

public interface IGameMessage extends ILobbyMessage {
    void setId(String id);
    String getId();
    void setPawnList(List<Pawn> pawnLayout);
    List<Pawn> getPawnList();
}
