package service.messages.interfaces;

import models.pawns.Pawn;

public interface IAvailableMovesMessage {
    Pawn getPawn();
    void setPawn(Pawn pawn);
}
