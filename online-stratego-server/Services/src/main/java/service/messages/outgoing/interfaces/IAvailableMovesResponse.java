package service.messages.outgoing.interfaces;

import models.Position;

import java.util.List;

public interface IAvailableMovesResponse {
    List<Position> getMoves();

    void setMoves(List<Position> moves);

    List<Position> getAttacks();

    void setAttacks(List<Position> attacks);
}
