package service.messages.incoming;

import models.pawns.Pawn;
import service.messages.interfaces.IGameMessage;

import java.util.List;

public class GameMessage extends Message implements IGameMessage {
    private List<Pawn> pawnList;
    @Override
    public List<Pawn> getPawnList() {
        return pawnList;
    }

    @Override
    public void setPawnList(List<Pawn> pawnList) {
        this.pawnList = pawnList;
    }
}
