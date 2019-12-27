package service.messages;

import models.Pawn.Pawn;
import service.Operation;

import java.util.List;

public class StartGameResponseMessage extends ResponseMessage {
    private List<Pawn> pawnList;
    private boolean[][] fields;

    public StartGameResponseMessage(Operation operation, String sender, String lobbyId, List<Pawn> pawnList, boolean[][] fields) {
        super(operation, sender, lobbyId);
        this.pawnList = pawnList;
        this.fields = fields;

    }

    public List<Pawn> getPawnList() {
        return pawnList;
    }

    public void setPawnList(List<Pawn> pawnList) {
        this.pawnList = pawnList;
    }

    public boolean[][] getFields() {
        return fields;
    }

    public void setFields(boolean[][] fields) {
        this.fields = fields;
    }

}
