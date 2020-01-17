package service.messages.incoming.interfaces;

import models.pawns.Pawn;

public interface IPawnMessage {
    Pawn getPawn();
    void setPawn(Pawn pawn);
}
