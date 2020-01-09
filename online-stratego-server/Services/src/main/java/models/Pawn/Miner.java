package models.Pawn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.BattleResult;
import models.enums.Color;

public class Miner extends Pawn{
    @JsonCreator
    public Miner(@JsonProperty("color")Color color) {
        super(3, color);
    }

    @Override
    public BattleResult attack(Pawn defender){
        if (defender instanceof Bomb){
            return BattleResult.WON;
        }

        if (defender instanceof Flag){
            return BattleResult.GAME_WON;
        }

        if (rank > defender.getRank()){
            return BattleResult.WON;
        } else if (rank < defender.getRank()){
            return BattleResult.LOST;
        } else return BattleResult.DRAW;
    }
}
