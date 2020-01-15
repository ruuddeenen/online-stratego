package service.messages.responses;

import models.Player;
import models.pawns.Pawn;
import service.Operation;

import java.util.List;

public class EndGameResponse extends Response {
    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    private Player winner;

    public List<Pawn> getPawnList() {
        return pawnList;
    }

    public void setPawnList(List<Pawn> pawnList) {
        this.pawnList = pawnList;
    }

    private List<Pawn> pawnList;

    public EndGameResponse(Operation operation, String receiver, String lobbyId, Player winner, List<Pawn> pawnList){
        super(operation, receiver, lobbyId);
        this.winner = winner;
        this.pawnList = pawnList;
    }
}
