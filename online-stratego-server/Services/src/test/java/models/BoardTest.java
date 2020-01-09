package models;

import models.Pawn.Pawn;
import models.Pawn.UnknownPawn;
import models.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        List<Pawn> redPawns = getPawnsWithLocation(Color.RED);
        List<Pawn> bluePawns = getPawnsWithLocation(Color.BLUE);
        board.addToPawnList(redPawns, Color.RED);
        board.addToPawnList(bluePawns, Color.BLUE);
        System.out.println(board);
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
            if (pawn.getPosition().getY() < 4) {
                assertEquals(UnknownPawn.class, pawn.getClass());
                assertEquals(Color.RED, pawn.getColor());
            } else if (pawn.getPosition().getY() > 5) {
                assertEquals(Color.BLUE, pawn.getColor());
            }
        });
    }

    private List<Pawn> getPawnsWithLocation(Color color) {
        List<Pawn> pawnList = Board.getStandardPawns(color);
        int i = 0;
        for (int x = 0; x < board.getField().length; x++) {
            for (int y = 6; y < board.getField().length; y++) {
                // Place pawn
                pawnList.get(i++).setPosition(new Position(x, y));
            }
        }
        return pawnList;
    }

    @Test
    void movePawn() {

    }
}