package service.messages.outgoing.models;

import models.pawns.Pawn;
import models.Player;
import service.messages.Operation;
import service.messages.outgoing.interfaces.IGameStartResponse;

import java.util.List;

public class GameStartResponse extends Response implements IGameStartResponse {
    private List<Pawn> pawnList;
    private boolean[][] fields;
    private Player opponent;

    public GameStartResponse(Operation operation, String receiver, Player opponent, String lobbyId, List<Pawn> pawnList, boolean[][] fields) {
        super(operation, receiver, lobbyId);
        this.pawnList = pawnList;
        this.fields = fields;
        this.opponent = opponent;

    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
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
