package service.messages.interfaces;

import models.Pawn.Pawn;

public interface IMoveMessage extends ILobbyMessage {
    void setId(String id);
    String getId();
    void setPawn(Pawn pawn);
    Pawn getPawn();
}
