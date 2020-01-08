package models;

import models.Pawn.*;
import models.enums.BattleResult;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private boolean[][] field = STANDARD_FIELD;
    private ArrayList<Pawn> pawnList;

    Board() {
        pawnList = new ArrayList<>();
    }

    public List<Pawn> getPawnsForPlayer(Player player) {
        List<Pawn> newPawnList = new ArrayList<>();
        for (Pawn p : pawnList
        ) {
            if (p.getColor() == player.getColor() || p.isRevealed()) {         // Is players pawn or is revealed
                newPawnList.add(p);
            } else {
                newPawnList.add(new UnknownPawn(p));
            }
        }
        if (player.getColor() == models.enums.Color.BLUE) {
            flipPawnPosition(newPawnList);
        }
        return newPawnList;
    }

    void removePawns(models.enums.Color color) {
        List<Pawn> listToRemove = new ArrayList<>();
        pawnList.forEach(pawn -> {
            if (pawn.getColor() == color) {
                listToRemove.add(pawn);
            }
        });
        listToRemove.forEach(pawn -> {
            pawnList.remove(pawn);
        });
    }

    void addToPawnList(List<Pawn> toBeAdded, models.enums.Color color) {
        if (color == models.enums.Color.BLUE) {
            flipPawnPosition(toBeAdded);
        }
        pawnList.addAll(toBeAdded);
    }

    private void flipPawnPosition(List<Pawn> pawnList) {
        for (Pawn p : pawnList
        ) {
            Position pos = p.getPosition();
            pos.flipY();
        }
    }

    List<Pawn> getPawnList() {
        return pawnList;
    }

    private Pawn getPawnOnLocation(Position position) {
        for (Pawn p : pawnList
        ) {
            if (p.getPosition() == position) {
                return p;
            }
        }
        return null;
    }

    public boolean movePawn(Pawn pawn, Position newPosition) {
        if (pawn.canMoveTo(newPosition)) {
            pawn.move(newPosition);
            return true;
        } else {
            return false;
        }
    }

    public BattleResult attack(Pawn attacker, Pawn defender) {
        return attacker.attack(defender);
    }

    private static boolean[][] STANDARD_FIELD = {
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

    public static List<Pawn> getStandardPawns(models.enums.Color color) {
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
                if (pawn.canMoveTo(pos)){
                    if (getPawnOnPosition(pos) == null){
                        possiblePositions.add(pos);
                    }
                }
            }
        }
        return possiblePositions;
    }

    private Pawn getPawnOnPosition(Position pos){
        for (Pawn p : getPawnList()
             ) {
            if (p.getPosition().getX() == pos.getX() &&  p.getPosition().getY() == pos.getY()){
                return p;
            }
        }
        return null;
    }
}
