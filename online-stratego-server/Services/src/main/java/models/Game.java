package models;

import models.pawns.Flag;
import models.pawns.Pawn;
import models.enums.Color;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
        board.addToPawnList(pawnList);
    }

    public boolean isReady() {
        return board.getPawnList().size() == 80;
    }

    public Set<Player> getPlayerSet() {
        return playerSet;
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

    private Player getPlayerByColor(Color color){
        for (Player p : playerSet
        ) {
            if (p.getColor().equals(color)) {
                return p;
            }
        }
        return null;
    }

    public boolean movePawn(Player player, Pawn pawn, Position newPosition) {
        if (turn != player.getColor() || !board.movePawn(pawn.getPosition(), newPosition) || pawn.getColor() != player.getColor()) {
            return false;
        }
        toggleTurn();
        return true;
    }

    public boolean isTurn(Player player) {
        return player.getColor() == turn;
    }

    public boolean isOver(){
        return board.getWinningColor() != null;
    }

    public Player getWinner(){
        return getPlayerByColor(board.getWinningColor());
    }

    public boolean[][] getField(){
        return board.getField();
    }

    public List<Pawn> getPawnsByColor(Color color){
        return board.getPawnsForColor(color);
    }

    public List<Pawn> getDefeatedPawnsByColor(Color color){
        return board.getDefeatedPawnsByColor(color);
    }

    public List<Position> getPossibleMoves(Pawn pawn){
        return board.getPossibleMoves(pawn);
    }

    public List<Position> getPossibleAttacks(Pawn pawn){
        return board.getPossibleAttacks(pawn);
    }

    public void revealAll(){
        board.revealAll();
    }
}
