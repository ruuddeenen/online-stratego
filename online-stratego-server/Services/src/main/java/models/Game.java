package models;

import models.Pawn.Pawn;
import models.enums.Color;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    private Board board;
    private Set<Player> playerSet;
    private Color turn;

    public Game() {
        this.board = new Board();
        this.playerSet = new HashSet<>();
        this.turn = Color.RED;
    }

    public void addPlayer(Player player) {
        playerSet.add(player);
    }

    public void addPawns(List<Pawn> pawnList, Color color) {
        if (pawnList.size() != 40) {
            return;
        }
        board.removePawns(color);
        board.addToPawnList(pawnList, color);
    }

    public boolean isReady() {
        return board.getPawnList().size() == 80;
    }

    public Set<Player> getPlayerSet() {
        return playerSet;
    }

    public Board getBoard() {
        return board;
    }

    public Color getTurn() {
        return turn;
    }

    public void setTurn(Color turn) {
        this.turn = turn;
    }


    public Player getPlayerByColor(Color color) {
        for (Player p : playerSet
        ) {
            if (p.getColor().equals(color)) {
                return p;
            }
        }
        return null;
    }

    public Player getPlayerById(String id) {
        for (Player p : playerSet
        ) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

}
