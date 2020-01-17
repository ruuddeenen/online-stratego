package service.messages.incoming.messages;

import models.pawns.Pawn;
import service.messages.both.IPawnList;

import java.util.List;

public class GameMessage extends Message implements IPawnList {
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
