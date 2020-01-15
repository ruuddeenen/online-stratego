package service.messages.incoming;

import models.Position;
import models.pawns.Pawn;
import service.messages.interfaces.IMoveMessage;

public class MoveMessage extends AvailableMovesMessage implements IMoveMessage {
    private Position position;

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
