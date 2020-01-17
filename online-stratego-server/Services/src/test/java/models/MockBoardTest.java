package models;

import models.enums.Color;
import models.pawns.Flag;
import models.pawns.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockBoardTest {
    private MockBoard board;
    @BeforeEach
    void setUp() {
        board = new MockBoard();
    }

    @Test
    void addPawn() {
        Pawn flag = new Flag(Color.RED);
        flag.setPosition(new Position(0,0));
        board.addPawn(flag);

        assertEquals(1, board.getPawnList().size());
        assertTrue(board.getPawnList().contains(flag));
    }
}