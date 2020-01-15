package service.messages.incoming.interfaces;

import models.pawns.Pawn;

public interface IAvailableMovesMessage {
    Pawn getPawn();
    void setPawn(Pawn pawn);
}
