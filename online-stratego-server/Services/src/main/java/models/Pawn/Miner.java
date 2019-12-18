package models.Pawn;

import models.enums.BattleResult;
import models.enums.Color;

public class Miner extends Pawn{
    public Miner(Color color) {
        super(3, color);
    }

    @Override
    public BattleResult attack(Pawn defender){
        if (defender instanceof Bomb){
            return BattleResult.WON;
        }
        if (rank > defender.getRank()){
            return BattleResult.WON;
        } else if (rank < defender.getRank()){
            return BattleResult.LOST;
        } else return BattleResult.DRAW;
    }
}
