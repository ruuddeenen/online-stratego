package service.messages.responses;

import models.Position;
import service.Operation;

import java.util.List;

public class AvailableMovesResponse extends Response {
    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    List<Position> positions;

    public AvailableMovesResponse(Operation operation, String receiver, String lobbyId, List<Position> positions) {
        super(operation, receiver, lobbyId);
        this.positions = positions;
    }
}
