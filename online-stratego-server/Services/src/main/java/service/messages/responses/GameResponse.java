package service.messages.responses;

import models.Pawn.Pawn;
import models.enums.Color;
import service.Operation;

import java.util.List;

public class GameResponse extends Response {
    private List<Pawn> pawnList;
    private Color turn;
    private List<Pawn> defeatedPawnList;

    public GameResponse(Operation operation, String receiver, String lobbyId, List<Pawn> pawnList, List<Pawn> defeatedPawnList, Color turn) {
        super(operation, receiver, lobbyId);
        this.pawnList = pawnList;
        this.turn = turn;
        this.defeatedPawnList = defeatedPawnList;
    }

    public List<Pawn> getDefeatedPawnList() {
        return defeatedPawnList;
    }

    public void setDefeatedPawnList(List<Pawn> defeatedPawnList) {
        this.defeatedPawnList = defeatedPawnList;
    }

    public List<Pawn> getPawnList() {
        return pawnList;
    }

    public void setPawnList(List<Pawn> pawnList) {
        this.pawnList = pawnList;
    }

    public Color getTurn() {
        return turn;
    }

    public void setTurn(Color turn) {
        this.turn = turn;
    }

}
