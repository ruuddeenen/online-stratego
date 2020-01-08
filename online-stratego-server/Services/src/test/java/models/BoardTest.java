package models;

import models.Pawn.Flag;
import models.Pawn.Pawn;
import models.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.addToPawnList(Board.getStandardPawns(Color.RED), Color.RED);
        board.addToPawnList(Board.getStandardPawns(Color.BLUE), Color.BLUE);
    }

    @Test
    void test_removePawnsRED() {
        board.removePawns(Color.RED);
        assertEquals(40, board.getPawnList().size());
        board.getPawnList().forEach(pawn -> assertEquals(Color.BLUE, pawn.getColor()));
    }

    @Test
    void test_removePawnsBLUE(){
        board.removePawns(Color.BLUE);
        assertEquals(40, board.getPawnList().size());
        board.getPawnList().forEach(pawn -> assertEquals(Color.RED, pawn.getColor()));
    }
}