package models.Pawn;

import models.Position;
import models.enums.BattleResult;
import models.enums.Color;

public abstract class Pawn {
    private String name;
    int rank;
    Position position;
    private Color color;
    private boolean revealed;

    public Pawn(int rank, Color color) {
        this.rank = rank;
        this.color = color;
        this.name = getClass().getSimpleName();
        this.revealed = false;
        this.position = new Position(-1, -1);
    }

    public void move(Position position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void reveal() {
        this.revealed = true;
    }

    public boolean isRevealed() {
        return revealed;
    }

    int getRank() {
        return rank;
    }

    public BattleResult attack(Pawn defender) {
        if (rank > defender.getRank()) {
            return BattleResult.WON;
        } else if (rank < defender.getRank()) {
            return BattleResult.LOST;
        } else return BattleResult.DRAW;
    }

    public boolean canMoveTo(Position newPosition) {
        if (position.getY() == newPosition.getY()) {     // Als Y gelijk is
            return (position.getX() == newPosition.getX() - 1 || position.getX() == newPosition.getX() + 1);
        } else if (position.getX() == newPosition.getX()){
            return (position.getY() == newPosition.getY() - 1 || position.getY() == newPosition.getY() + 1);
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
}
