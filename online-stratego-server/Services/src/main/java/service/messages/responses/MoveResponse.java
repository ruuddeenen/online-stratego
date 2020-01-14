package service.messages.responses;

import models.Position;
import service.Operation;

public class MoveResponse extends Response {
    public Position getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(Position oldPosition) {
        this.oldPosition = oldPosition;
    }

    public Position getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Position newPosition) {
        this.newPosition = newPosition;
    }

    private Position oldPosition;
    private Position newPosition;

    public MoveResponse(Operation operation, String receiver, String lobbyId, Position oldPosition, Position newPosition){
        super(operation, receiver, lobbyId);
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }
}
