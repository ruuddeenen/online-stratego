package models.Pawn;

import models.Board;
import models.enums.BattleResult;
import models.enums.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.print.event.PrintJobAttributeListener;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class PawnTest {
    private static List<Pawn> pawnList;

    @BeforeAll
    static void beforeAll() {
        pawnList = Board.getStandardPawns(Color.RED);
    }

    private void standardScenario(Pawn attacker, Pawn defender, BattleResult result){
        if (defender instanceof Flag){
            assertEquals(BattleResult.GAME_WON, result);
        } else if (attacker.getRank() > defender.getRank()){
            assertEquals(BattleResult.WON, result);
        } else if (attacker.getRank() < defender.getRank()) {
            assertEquals(BattleResult.LOST, result);
        } else {
            assertEquals(BattleResult.DRAW, result);
        }
    }

    @Test
    void test_attackSpy() {
        Spy attacker = new Spy(Color.RED);
        pawnList.forEach(defender -> {
            BattleResult result = attacker.attack(defender);
            if (defender instanceof Marshal) {
                assertEquals(BattleResult.WON, result);
            } else {
                standardScenario(attacker, defender, result);
            }
        });
    }

    @Test
    void test_attackScout() {
        Scout attacker = new Scout(Color.RED);
        pawnList.forEach(defender -> {
            BattleResult result = attacker.attack(defender);
            standardScenario(attacker, defender, result);
        });
    }

    @Test
    void test_attackMiner() {
        Miner attacker = new Miner(Color.RED);
        pawnList.forEach(defender -> {
            BattleResult result = attacker.attack(defender);
            if (defender instanceof Bomb){
                assertEquals(BattleResult.WON, result);
            } else {
                standardScenario(attacker, defender, result);
            }
        });
    }

    @Test
    void test_attackSergeant() {
        Sergeant attacker = new Sergeant(Color.RED);
        pawnList.forEach(defender -> {
            BattleResult result = attacker.attack(defender);
            standardScenario(attacker, defender, result);
        });
    }

    @Test
    void test_attackLieutenant() {
        Lieutenant attacker = new Lieutenant(Color.RED);
        pawnList.forEach(defender -> {
            BattleResult result = attacker.attack(defender);
            standardScenario(attacker, defender, result);
        });
    }

    @Test
    void test_attackCaptain() {
        Captain attacker = new Captain(Color.RED);
        pawnList.forEach(defender -> {
            BattleResult result = attacker.attack(defender);
            standardScenario(attacker, defender, result);
        });
    }

    @Test
    void test_attackMajor() {
        Major attacker = new Major(Color.RED);
        pawnList.forEach(defender -> {
            BattleResult result = attacker.attack(defender);
            standardScenario(attacker, defender, result);
        });
    }

    @Test
    void test_attackColonel() {
        Colonel attacker = new Colonel(Color.RED);
        pawnList.forEach(defender -> {
            BattleResult result = attacker.attack(defender);
            standardScenario(attacker, defender, result);
        });
    }

    @Test
    void test_attackGeneral() {
        General attacker = new General(Color.RED);
        pawnList.forEach(defender -> {
            BattleResult result = attacker.attack(defender);
            standardScenario(attacker, defender, result);
        });
    }

    @Test
    void test_attackMarshall() {
        Lieutenant attacker = new Lieutenant(Color.RED);
        pawnList.forEach(defender -> {
            BattleResult result = attacker.attack(defender);
            standardScenario(attacker, defender, result);
        });
    }


    @Test
    void test_canMoveTo_Standard() {

    }

    @Test
    void test_canMoveTo_Scout() {

    }

    @Test
    void test_canMoveTo_Bomb() {

    }

    @Test
    void test_canMoveTo_Flag() {

    }
}