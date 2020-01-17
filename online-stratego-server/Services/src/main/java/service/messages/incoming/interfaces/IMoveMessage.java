package service.messages.incoming.interfaces;

import models.Position;

public interface IMoveMessage extends IPawnMessage {
    Position getPosition();
    void setPosition(Position position);
}
