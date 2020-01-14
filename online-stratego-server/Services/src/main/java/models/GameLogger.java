package models;

import models.pawns.Pawn;
import models.enums.BattleResult;

import java.util.ArrayList;
import java.util.List;

public class GameLogger {
    private List<String> logs;

    public GameLogger(){
        logs = new ArrayList<>();
    }

    public void move(Player player, Pawn pawn, Position pOld, Position pNew){
        logs.add(String.format("%s moved %s from %s to %s", player.getUsername(), pawn.toString(), pOld.toString(), pNew.toString()));
    }

    public void battle(Player player, Pawn attacker, Pawn defender, BattleResult result){
        logs.add(String.format("%s attacked %s with %s and the result =  %s", player.getUsername(), defender.toString(), attacker.toString(), result.toString()));
    }
}
