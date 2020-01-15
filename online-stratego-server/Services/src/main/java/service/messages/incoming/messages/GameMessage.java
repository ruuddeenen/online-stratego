package service.messages.incoming.messages;

import models.pawns.Pawn;
import service.messages.incoming.interfaces.IGameMessage;

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
