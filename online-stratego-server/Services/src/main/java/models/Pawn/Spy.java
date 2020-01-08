package models.Pawn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.BattleResult;
import models.enums.Color;

public class Spy extends Pawn{
    @JsonCreator
    public Spy(@JsonProperty("color")Color color) {
        super(1, color);
    }


    @Override
    public BattleResult attack (Pawn defender){
        if (defender instanceof Marshal){
            return BattleResult.WON;
        }
        if (rank > defender.getRank()){
            return BattleResult.WON;
        } else if (rank < defender.getRank()){
            return BattleResult.LOST;
        } else return BattleResult.DRAW;
    }
}
