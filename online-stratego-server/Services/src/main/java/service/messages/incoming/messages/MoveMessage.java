package service.messages.incoming.messages;

import models.Position;
import service.messages.incoming.interfaces.IMoveMessage;

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
