package models.Pawn;

import models.enums.Color;

public class PawnFactory {
    private Color color;

    public PawnFactory(Color color){
        this.color = color;
    }

    public Pawn getPawn(String s){
        s = s.toUpperCase();
        switch (s){
            case "BOMB":
                return new Bomb(color);
            case "CAPTAIN":
                return new Captain(color);
            case "COLONEL":
                return new Colonel(color);
            case "FLAG":
                return new Flag(color);
            case "GENERAL":
                return new General(color);
            case "LIEUTENANT":
                return new Lieutenant(color);
            case "MAJOR":
                return new Major(color);
            case "MARSHAL":
                return new Marshal(color);
            case "MINER":
                return new Miner(color);
            case "SCOUT":
                return new Scout(color);
            case "SERGEANT":
                return new Sergeant(color);
            case "SPY":
                return new Spy(color);
            default:
                return new UnknownPawn(color);
        }
    }
}
