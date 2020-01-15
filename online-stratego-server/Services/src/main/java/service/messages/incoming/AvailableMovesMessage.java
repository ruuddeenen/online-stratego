package service.messages.incoming;

import models.pawns.Pawn;
import service.messages.interfaces.IAvailableMovesMessage;

public class AvailableMovesMessage extends Message implements IAvailableMovesMessage{
    private Pawn pawn;

    @Override
    public Pawn getPawn() {
        return pawn;
    }

    @Override
    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }
}
