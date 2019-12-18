package models;

import models.Pawn.*;
import models.enums.BattleResult;
import models.enums.Color;

public class Board {
    private boolean[][] field;
    private Pawn[][] pawns;
    private static final int FIELD_SIZE = 10;

    public Board(){
        field = setField();
        pawns = new Pawn[FIELD_SIZE][FIELD_SIZE];
    }

    private Pawn getPawnOnLocation(Position position) {
        return pawns[position.getX()][position.getY()];
    }

    public boolean movePawn(Pawn pawn, Position newPosition) {
        if (pawn.canMoveTo(newPosition)) {
            pawn.move(newPosition);
            return true;
        } else {
            return false;
        }
    }

    public Pawn[][] getFieldForColor(Color color) {
        Color oppositeColor = Color.oppositeOf(color);
        Pawn[][] temp = new Pawn[FIELD_SIZE][FIELD_SIZE];

        for (int x = 0; x < pawns.length; x++) {
            for (int y = 0; y < pawns.length; y++) {
                if (pawns[x][y].getColor() == oppositeColor){
                    if (pawns[x][y].isRevealed()){
                        temp[x][y] = pawns[x][y];
                    } else {
                        temp[x][y] = new UnknownPawn(oppositeColor);
                    }
                } else {
                    temp[x][y] = pawns[x][y];
                }
            }
        }
        return temp;
    }

    public BattleResult attack(Pawn attacker, Pawn defender){
        return attacker.attack(defender);
    }

    private boolean[][] setField(){
        return new boolean[][]{
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
    }
}
