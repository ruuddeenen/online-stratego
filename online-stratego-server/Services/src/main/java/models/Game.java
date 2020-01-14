package models;

import models.pawns.Pawn;
import models.enums.Color;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    private Board board;
    private Set<Player> playerSet;
    private Color turn;
    private GameLogger gameHistory;

    public Game() {
        this.board = new Board();
        this.playerSet = new HashSet<>();
        this.turn = Color.RED;
        this.gameHistory = new GameLogger();
    }

    public void addPlayer(Player player) {
        playerSet.add(player);
    }

    public void addPawns(List<Pawn> pawnList, Color color) {
        if (pawnList.size() != 40) {
            return;
        }
        board.removePawns(color);
        board.addToPawnList(pawnList);
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

    private void toggleTurn() {
        turn = Color.oppositeOf(turn);
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

    public boolean movePawn(Player player, Pawn pawn, Position newPosition) {
        if (turn != player.getColor() || !board.movePawn(pawn.getPosition(), newPosition) || pawn.getColor() != player.getColor()) {
            return false;
        }

        gameHistory.move(
                player,
                pawn,
                pawn.getPosition(),
                newPosition
        );
        toggleTurn();
        return true;
    }

    public boolean isTurn(Player player) {
        return player.getColor() == turn;
    }
}
