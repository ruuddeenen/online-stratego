package service.messages.incoming.messages;

import models.pawns.Pawn;
import service.messages.incoming.interfaces.IPawnMessage;

public class AvailableMovesMessage extends Message implements IPawnMessage {
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
