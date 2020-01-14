package models.pawns;

import models.Board;
import models.Position;
import models.enums.BattleResult;
import models.enums.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class PawnTest {
    private static List<Pawn> pawnList;
    private static Board board;

    @BeforeAll
    static void beforeAll() {
        board = new Board();
        pawnList = Board.getStandardPawns(Color.RED);
    }

    private void standardScenario(Pawn attacker, Pawn defender, BattleResult result) {
        if (defender instanceof Flag) {
            assertEquals(BattleResult.GAME_WON, result);
        } else if (attacker.getRank() > defender.getRank()) {
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
            if (defender instanceof Bomb) {
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
        Pawn pawn = new UnknownPawn(Color.RED);
        pawn.setPosition(new Position(5, 5));

        for (int x = 0; x < board.getField().length; x++) {
            for (int y = 0; y < board.getField().length; y++) {
                // Check if position is in range
                if (y == 5) {
                    if (x == 4 || x == 6) {
                        assertTrue(pawn.canMoveTo(new Position(x, y)));
                    } else {
                        assertFalse(pawn.canMoveTo(new Position(x, y)));
                    }
                } else if (x == 5) {
                    if (y == 4 || y == 6) {
                        assertTrue(pawn.canMoveTo(new Position(x, y)));
                    } else {
                        assertFalse(pawn.canMoveTo(new Position(x, y)));
                    }
                } else {
                    assertFalse(pawn.canMoveTo(new Position(x, y)));
                }
            }
        }
    }

    @Test
    void test_canMoveTo_Scout() {
        Pawn pawn = new Scout(Color.RED);
        pawn.setPosition(new Position(3, 5));

        for (int x = 0; x < board.getField().length; x++) {
            for (int y = 0; y < board.getField().length; y++) {
                if (y == 5 || x == 3) {
                    assertTrue(pawn.canMoveTo(new Position(x, y)));
                } else {
                    assertFalse(pawn.canMoveTo(new Position(x, y)));
                }
            }
        }
    }

    @Test
    void test_canMoveTo_Bomb() {
        Pawn bomb = new Bomb(Color.RED);
        bomb.setPosition(new Position(0, 0));
        for (int x = 0; x < board.getField().length; x++) {
            for (int y = 0; y < board.getField().length; y++) {
                assertFalse(bomb.canMoveTo(new Position(x, y)));
            }
        }
    }


    @Test
    void test_canMoveTo_Flag() {
        Pawn flag = new Flag(Color.RED);
        flag.setPosition(new Position(0, 0));
        for (int x = 0; x < board.getField().length; x++) {
            for (int y = 0; y < board.getField().length; y++) {
                assertFalse(flag.canMoveTo(new Position(x, y)));
            }
        }
    }
}