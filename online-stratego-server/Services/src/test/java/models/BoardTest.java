package models;

import models.enums.Color;
import models.pawns.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private MockBoard mock;

    @BeforeEach
    void setUp() {
        board = new Board();
        mock = new MockBoard();
        List<Pawn> redPawns = getPawnsWithLocation(Color.RED);
        List<Pawn> bluePawns = getPawnsWithLocation(Color.BLUE);
        board.addToPawnList(redPawns);
        board.addToPawnList(bluePawns);
    }

    @Test
    void test_initialSetUp() {
        List<Pawn> pawnList = board.getPawnList();
        assertEquals(80, pawnList.size());
        Set<Position> positions = new HashSet<>();
        pawnList.forEach(p -> {
            positions.add(p.getPosition());
        });
        assertEquals(80, positions.size());
    }

    @Test
    void test_removeRedPawns() {
        board.removePawns(Color.RED);
        assertEquals(40, board.getPawnList().size());
        board.getPawnList().forEach(pawn -> assertEquals(Color.BLUE, pawn.getColor()));
    }

    @Test
    void test_removeBluePawns() {
        board.removePawns(Color.BLUE);
        assertEquals(40, board.getPawnList().size());
        board.getPawnList().forEach(pawn -> assertEquals(Color.RED, pawn.getColor()));
    }

    @Test
    void test_getPawnsForColorStartingPositionRed() {
        List<Pawn> pawns = board.getPawnsForColor(Color.RED);
        pawns.forEach(pawn -> {
            if (pawn.getPosition().getY() < 4) {
                assertEquals(UnknownPawn.class, pawn.getClass());
                assertEquals(Color.BLUE, pawn.getColor());
            } else if (pawn.getPosition().getY() > 5) {
                assertEquals(Color.RED, pawn.getColor());
            }
        });
    }

    @Test
    void test_getPawnsForColorStartingPositionBlue() {
        List<Pawn> pawns = board.getPawnsForColor(Color.BLUE);
        pawns.forEach(pawn -> {
            if (pawn.getPosition().getY() > 5) {
                assertEquals(UnknownPawn.class, pawn.getClass());
                assertEquals(Color.RED, pawn.getColor());
            } else if (pawn.getPosition().getY() < 4) {
                assertEquals(Color.BLUE, pawn.getColor());
            }
        });
    }

    @Test
    void test_battleCaseGameWon() {
        Flag defender = new Flag(Color.RED);
        Marshal attacker = new Marshal(Color.BLUE);

        battleCase(mock, attacker, defender);

        // assert
        assertEquals(Color.BLUE, mock.getWinningColor());
        assertTrue(attacker.isRevealed());
        assertTrue(mock.getDefeatedPawnsByColor(Color.BLUE).contains(defender));
        assertFalse(mock.getPawnsForColor(Color.RED).contains(defender));
    }

    @Test
    void test_battleCaseWon() {
        Scout defender = new Scout(Color.RED);
        Marshal attacker = new Marshal(Color.BLUE);

        battleCase(mock, attacker, defender);

        // assert
        assertNull(mock.getWinningColor());
        assertTrue(mock.getDefeatedPawnsByColor(Color.BLUE).contains(defender));
        assertFalse(mock.getPawnsForColor(Color.RED).contains(defender));
        assertTrue(attacker.isRevealed());
    }

    @Test
    void test_battleCaseDraw() {
        Marshal defender = new Marshal(Color.RED);
        Marshal attacker = new Marshal(Color.BLUE);

        battleCase(mock, attacker, defender);

        // assert
        assertNull(mock.getWinningColor());
        assertTrue(mock.getDefeatedPawnsByColor(Color.BLUE).contains(defender));
        assertTrue(mock.getDefeatedPawnsByColor(Color.RED).contains(attacker));
        assertFalse(mock.getPawnsForColor(Color.RED).contains(defender));
        assertFalse(mock.getPawnsForColor(Color.BLUE).contains(attacker));
    }

    @Test
    void test_battleCaseLost() {
        Marshal defender = new Marshal(Color.RED);
        Miner attacker = new Miner(Color.BLUE);

        battleCase(mock, attacker, defender);

        // assert
        assertNull(mock.getWinningColor());
        assertTrue(mock.getDefeatedPawnsByColor(Color.RED).contains(attacker));
        assertFalse(mock.getPawnsForColor(Color.BLUE).contains(attacker));
        assertTrue(defender.isRevealed());
    }

    @Test
    void test_getPossibleMoves() {
        List<Position> possibleMoves = placePawnAndGetMoves(new Marshal(Color.RED), 2, 2);

        assertEquals(4, possibleMoves.size());
    }

    @Test
    void test_getPossibleMovesNearWater() {
        List<Position> possibleMoves = placePawnAndGetMoves(new Marshal(Color.RED), 3, 3);

        assertEquals(3, possibleMoves.size());
    }

    @Test
    void test_getPossibleMovesNearEdge() {
        List<Position> possibleMoves = placePawnAndGetMoves(new Marshal(Color.RED), 0, 0);

        assertEquals(2, possibleMoves.size());
    }

    @Test
    void test_getPossibleAttacksAndMoves() {
        Pawn attacker = new Spy(Color.RED);
        attacker.setPosition(new Position(1, 1));
        mock.addPawn(attacker);

        int attacks = 0;
        int moves = mock.getPossibleMoves(attacker).size();
        assertEquals(attacks++, mock.getPossibleAttacks(attacker).size());
        assertEquals(moves--, mock.getPossibleMoves(attacker).size());

        placePawn(new UnknownPawn(Color.BLUE), 0, 1);
        assertEquals(attacks++, mock.getPossibleAttacks(attacker).size());
        assertEquals(moves--, mock.getPossibleMoves(attacker).size());

        placePawn(new UnknownPawn(Color.BLUE), 1, 0);
        assertEquals(attacks++, mock.getPossibleAttacks(attacker).size());
        assertEquals(moves--, mock.getPossibleMoves(attacker).size());

        placePawn(new UnknownPawn(Color.BLUE), 2, 1);
        assertEquals(attacks++, mock.getPossibleAttacks(attacker).size());
        assertEquals(moves--, mock.getPossibleMoves(attacker).size());

        placePawn(new UnknownPawn(Color.BLUE), 1, 2);
        assertEquals(attacks, mock.getPossibleAttacks(attacker).size());
        assertEquals(moves, mock.getPossibleMoves(attacker).size());
    }

    @Test
    void test_scoutCantMoveOverWater() {
        int x = 2, y = 2;
        Scout scout = new Scout(Color.RED);
        scout.setPosition(new Position(x,y));
        mock.addPawn(scout);

        assertTrue(mock.pawnCanMoveTo(scout, new Position(x, ++y))); // Open
        assertFalse(mock.pawnCanMoveTo(scout, new Position(x, ++y))); // Water
        assertFalse(mock.pawnCanMoveTo(scout, new Position(x, ++y))); // Water
        assertFalse(mock.pawnCanMoveTo(scout, new Position(x, ++y))); // Open
    }

    @Test
    void test_scoutCantMoveOverPawn(){
        int x = 0, y = 0;
        Scout scout = new Scout(Color.RED);
        scout.setPosition(new Position(x,y));
        mock.addPawn(scout);
        Spy spy = new Spy(Color.RED);
        spy.setPosition(new Position(x, 2));
        mock.addPawn(spy);

        assertTrue(mock.pawnCanMoveTo(scout, new Position(x, ++y))); // Open
        assertTrue(mock.pawnCanMoveTo(scout, new Position(x, ++y))); // Spy
        assertFalse(mock.pawnCanMoveTo(scout, new Position(x, ++y))); // Open
    }

    private void placePawn(Pawn pawn, int x, int y) {
        pawn.setPosition(new Position(x, y));
        mock.addPawn(pawn);
    }

    private List<Position> placePawnAndGetMoves(Pawn pawn, int x, int y) {
        pawn.setPosition(new Position(x, y));
        mock.addPawn(pawn);
        return mock.getPossibleMoves(pawn);
    }

    private void battleCase(MockBoard mock, Pawn attacker, Pawn defender) {
        Position attackPos = new Position(1, 0);
        Position defendPos = new Position(0, 0);
        attacker.setPosition(attackPos);
        defender.setPosition(defendPos);
        mock.addPawn(attacker);
        mock.addPawn(defender);

        mock.movePawn(attackPos, defendPos);
    }


    private List<Pawn> getPawnsWithLocation(Color color) {
        List<Pawn> pawnList = Board.getStandardPawns(color);
        int i = 0;
        for (int x = 0; x < board.getField().length; x++) {
            if (color == Color.RED) {
                for (int y = 6; y < board.getField().length; y++) {
                    // Place pawn
                    pawnList.get(i++).setPosition(new Position(x, y));
                }
            } else if (color == Color.BLUE) {
                for (int y = 0; y < 4; y++) {
                    // Place pawn
                    pawnList.get(i++).setPosition(new Position(x, y));
                }
            }
        }
        return pawnList;
    }
}