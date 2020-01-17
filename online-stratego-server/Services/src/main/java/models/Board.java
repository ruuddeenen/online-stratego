package models;

import models.pawns.*;
import models.enums.Color;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private boolean[][] field = standardField;
    private List<Pawn> pawnList;
    private List<Pawn> defeatedPawns;
    private Color winner = null;

    public Board() {
        this.pawnList = new ArrayList<>();
        this.defeatedPawns = new ArrayList<>();
    }

    void revealAll() {
        pawnList.forEach(Pawn::reveal);
    }

    public Color getWinningColor() {
        return winner;
    }

    public List<Pawn> getDefeatedPawnsByColor(Color color) {
        List<Pawn> defeatedByColor = new ArrayList<>();
        defeatedPawns.forEach(pawn -> {
            if (pawn.getColor() == Color.oppositeOf(color)) {
                defeatedByColor.add(pawn);
            }
        });
        return defeatedByColor;
    }

    public List<Pawn> getPawnsForColor(Color color) {
        List<Pawn> newPawnList = new ArrayList<>();
        for (Pawn p : pawnList
        ) {
            if (p.getColor() == color || p.isRevealed()) {         // Is players pawn or is revealed
                newPawnList.add(p);
            } else {
                newPawnList.add(new UnknownPawn(p));
            }
        }
        return newPawnList;
    }

    void removePawns(Color color) {
        List<Pawn> listToRemove = new ArrayList<>();
        pawnList.forEach(pawn -> {
            if (pawn.getColor() == color) {
                listToRemove.add(pawn);
            }
        });
        listToRemove.forEach(pawn -> pawnList.remove(pawn));
    }

    void addToPawnList(List<Pawn> toBeAdded) {
        pawnList.addAll(toBeAdded);
    }

    List<Pawn> getPawnList() {
        return pawnList;
    }

    boolean movePawn(Position oldPosition, Position newPosition) {
        Pawn pawn = getPawnOnPosition(oldPosition);
        if (pawn != null && isWalkable(newPosition) && pawnCanMoveTo(pawn, newPosition)) {
            Pawn defender = getPawnOnPosition(newPosition);
            if (defender != null) {
                battle(pawn, defender);
            }
            pawn.move(newPosition);
            return true;
        }
        return false;
    }

    private boolean isWalkable(Position position) {
        return field[position.getY()][position.getX()];
    }

    private void battle(Pawn pawn, Pawn defender) {
        switch (pawn.attack(defender)) {
            case GAME_WON:
                winner = pawn.getColor();
            case WON:
                pawn.reveal();
                defeatedPawns.add(defender);
                pawnList.remove(defender);
                break;
            case DRAW:
                defeatedPawns.add(pawn);
                defeatedPawns.add(defender);
                pawnList.remove(pawn);
                pawnList.remove(defender);
                break;
            case LOST:
                defender.reveal();
                defeatedPawns.add(pawn);
                pawnList.remove(pawn);
                break;
        }
    }

    private static boolean[][] standardField = {
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, false, false, true, true, false, false, true, true},
            {true, true, false, false, true, true, false, false, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
    };

    public static List<Pawn> getStandardPawns(Color color) {
        PawnFactory factory = new PawnFactory(color);
        List<Pawn> pawns = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            if (i < 6) {
                pawns.add(factory.getPawn("BOMB"));        // 6
            } else if (i < 7) {
                pawns.add(factory.getPawn("MARSHAL"));     // 1
            } else if (i < 8) {
                pawns.add(factory.getPawn("GENERAL"));     // 1
            } else if (i < 10) {
                pawns.add(factory.getPawn("COLONEL"));     // 2
            } else if (i < 13) {
                pawns.add(factory.getPawn("MAJOR"));       // 3
            } else if (i < 17) {
                pawns.add(factory.getPawn("CAPTAIN"));     // 4
            } else if (i < 21) {
                pawns.add(factory.getPawn("LIEUTENANT"));  // 4
            } else if (i < 25) {
                pawns.add(factory.getPawn("SERGEANT"));    // 4
            } else if (i < 30) {
                pawns.add(factory.getPawn("MINER"));       // 5
            } else if (i < 38) {
                pawns.add(factory.getPawn("SCOUT"));       // 8
            } else if (i < 39) {
                pawns.add(factory.getPawn("SPY"));         // 1
            } else {
                pawns.add(factory.getPawn("FLAG"));        // 1
            }
        }
        return pawns;
    }

    public List<Position> getPossibleMoves(Pawn pawn) {
        List<Position> possiblePositions = new ArrayList<>();
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field.length; y++) {
                Position pos = new Position(x, y);
                if (pawnCanMoveTo(pawn, pos) && getPawnOnPosition(pos) == null) {
                    possiblePositions.add(pos);
                }
            }
        }
        return possiblePositions;
    }

    public List<Position> getPossibleAttacks(Pawn pawn) {
        List<Position> possiblePositions = new ArrayList<>();
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field.length; y++) {
                Position pos = new Position(x, y);
                if (pawnCanMoveTo(pawn, pos)) {
                    Pawn possiblePawn = getPawnOnPosition(pos);
                    if (possiblePawn != null && possiblePawn.getColor() == Color.oppositeOf(pawn.getColor())) {
                        possiblePositions.add(pos);
                    }
                }
            }
        }
        return possiblePositions;
    }

    private Pawn getPawnOnPosition(Position position) {
        for (Pawn p : getPawnList()
        ) {
            if (p.getPosition().getX() == position.getX() && p.getPosition().getY() == position.getY()) {
                return p;
            }
        }
        return null;
    }

    public boolean[][] getField() {
        return field;
    }

    public boolean pawnCanMoveTo(Pawn pawn, Position position) {
        if (pawn instanceof Scout) {
            if (pawn.canMoveTo(position) && field[position.getY()][position.getX()]) {
                List<Position> positions = getMovesForScout((Scout) pawn);
                for (Position pos : positions
                ) {
                    if (pos.getX() == position.getX() && pos.getY() == position.getY()) {
                        return true;
                    }
                }
            }
            return false;
        }
        return pawn.canMoveTo(position) && field[position.getY()][position.getX()];
    }

    private List<Position> getMovesForScout(Scout scout) {
        Position position = scout.getPosition();
        List<Position> possiblePositions = new ArrayList<>();

        // To left
        for (int x = position.getX() - 1; x >= 0; x--) {
            Position tempPosition = new Position(x, position.getY());
            if (checkPositionAndReturnBreak(tempPosition, possiblePositions)){
                break;
            }
        }
        // To right
        for (int x = position.getX() + 1; x < 10; x++) {
            Position tempPosition = new Position(x, position.getY());
            if (checkPositionAndReturnBreak(tempPosition, possiblePositions)){
                break;
            }
        }
        // To up
        for (int y = position.getY() - 1; y >= 0; y--) {
            Position tempPosition = new Position(position.getX(), y);
            if (checkPositionAndReturnBreak(tempPosition, possiblePositions)){
                break;
            }
        }
        // To down
        for (int y = position.getY() + 1; y < 10; y++) {
            Position tempPosition = new Position(position.getX(), y);
            if (checkPositionAndReturnBreak(tempPosition, possiblePositions)){
                break;
            }
        }
        return possiblePositions;
    }

    private boolean checkPositionAndReturnBreak(Position position, List<Position> possiblePositions){
        if (getPawnOnPosition(position) == null && field[position.getY()][position.getX()]) {
            possiblePositions.add(position);
        } else if (getPawnOnPosition(position) != null) {
            possiblePositions.add(position);
            return true;
        } return !field[position.getY()][position.getX()];
    }
}
