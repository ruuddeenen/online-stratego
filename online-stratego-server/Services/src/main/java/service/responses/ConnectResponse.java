package service.responses;

import models.Pawn.Pawn;
import models.Player;

import java.util.ArrayList;

public class ConnectResponse {
    private ArrayList<Player> playerList;
    private boolean[][] field;
    private Pawn[][] pawns;

    public Pawn[][] getPawns() {
        return pawns;
    }

    public void setPawns(Pawn[][] pawns) {
        this.pawns = pawns;
    }

    public boolean[][] getField() {
        return field;
    }

    public void setField(boolean[][] field) {
        this.field = field;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }
}
