package service.messages.incoming.interfaces;

import models.Position;

public interface IMoveMessage extends IAvailableMovesMessage {
    Position getPosition();
    void setPosition(Position position);
}
