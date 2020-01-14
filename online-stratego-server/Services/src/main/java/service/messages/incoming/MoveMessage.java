package service.messages.incoming;

import models.Position;

public class MoveMessage extends GetAvailableMovesMessage {
    private Position position;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
