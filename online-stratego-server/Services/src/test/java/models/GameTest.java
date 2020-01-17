package models;

import models.enums.Color;
import models.pawns.Flag;
import models.pawns.Pawn;
import models.pawns.Spy;
import models.pawns.UnknownPawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private Player player;

    @BeforeEach
    void setUp() {
        game = new Game();
        player = new Player("id", "username");
    }

    @Test
    void test_startingTurnRed() {
        assertEquals(Color.RED, game.getTurn());
    }

    @Test
    void test_addPawns() {
        game.addPawns(Board.getStandardPawns(Color.RED));

        List<Pawn> red = game.getPawnsByColor(Color.RED);
        assertEquals(40, red.size());
        red.forEach(pawn -> assertFalse(pawn instanceof UnknownPawn));

        List<Pawn> blue = game.getPawnsByColor(Color.BLUE);
        assertEquals(40, blue.size());
        blue.forEach(pawn -> assertTrue(pawn instanceof UnknownPawn));

        game.addPawns(Board.getStandardPawns(Color.BLUE));
    }

    @Test
    void test_addPawnsReplace() {
        game.addPawns(Board.getStandardPawns(Color.RED));
        List<Pawn> newPawns = Board.getStandardPawns(Color.RED);
        newPawns.remove(39); // Flag
        Spy spy = new Spy(Color.RED);
        newPawns.add(spy);

        game.addPawns(newPawns);

        assertEquals(40, game.getPawnsByColor(Color.RED).size());
        assertTrue(game.getPawnsByColor(Color.RED).contains(spy));
        int count = 0;
        for (Pawn p : game.getPawnsByColor(Color.RED)
        ) {
            if (p instanceof Flag) {
                fail();
            } else if (p instanceof Spy) {
                count++;
            }
        }
        assertEquals(2, count);
    }

    @Test
    void test_addPawnsSizeSmallerThen40() {
        List<Pawn> pawns = Board.getStandardPawns(Color.RED);
        pawns.remove(0);
        game.addPawns(pawns);

        assertEquals(0, game.getPawnsByColor(Color.RED).size());
    }

    @Test
    void test_addPawnsSizeBiggerThen40() {
        List<Pawn> pawns = Board.getStandardPawns(Color.RED);
        pawns.add(new Flag(Color.RED));
        game.addPawns(pawns);

        assertEquals(0, game.getPawnsByColor(Color.RED).size());
    }

    @Test
    void test_movePawnNotInTurn() {
        test_addPawns();
        Pawn pawnToMove = game.getPawnsByColor(Color.BLUE).get(46); // Marshal
        pawnToMove.setPosition(new Position(0, 0));

        boolean result = game.movePawn(player, pawnToMove, new Position(0, 1));
        assertFalse(result);
    }

    @Test
    void test_movePawnOpponent() {
        test_addPawns();
        Pawn pawnToMove = game.getPawnsByColor(Color.BLUE).get(46); // Marshal
        pawnToMove.setPosition(new Position(0, 0));

        player.setColor(Color.RED);
        boolean result = game.movePawn(player, pawnToMove, new Position(0, 1));
        assertFalse(result);
    }

    @Test
    void test_movePawnImpossibleMove(){
        test_addPawns();
        Pawn pawnToMove = game.getPawnsByColor(Color.BLUE).get(46); // Marshal
        pawnToMove.setPosition(new Position(0, 0));

        player.setColor(Color.RED);
        boolean result = game.movePawn(player, pawnToMove, new Position(0, 2));
        assertFalse(result);
    }

    @Test
    void test_movePawn(){
        test_addPawns();
        game.setTurn(Color.BLUE);
        Pawn pawnToMove = game.getPawnsByColor(Color.BLUE).get(46); // Marshal
        pawnToMove.setPosition(new Position(0, 0));

        player.setColor(Color.BLUE);
        boolean result = game.movePawn(player, pawnToMove, new Position(0, 1));

        assertTrue(result);
        assertEquals(Color.RED, game.getTurn());
        assertFalse(game.isTurn(player));
    }
}