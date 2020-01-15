package service.messages.outgoing.models;

import models.Position;
import service.messages.Operation;
import service.messages.outgoing.interfaces.IAvailableMovesResponse;

import java.util.List;

public class AvailableMovesResponse extends Response implements IAvailableMovesResponse {
    private List<Position> moves;
    private List<Position> attacks;

    public List<Position> getMoves() {
        return moves;
    }

    public void setMoves(List<Position> moves) {
        this.moves = moves;
    }

    public List<Position> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<Position> attacks) {
        this.attacks = attacks;
    }

    public AvailableMovesResponse(Operation operation, String receiver, String lobbyId, List<Position> moves, List<Position> attacks) {
        super(operation, receiver, lobbyId);
        this.moves = moves;
        this.attacks = attacks;
    }
}
