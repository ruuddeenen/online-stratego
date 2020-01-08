package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void test_flipY_LowHigh() {
        Position pos = new Position(0,0);
        pos.flipY();
        assertEquals(9, pos.getY());
    }

    @Test
    void test_flipY_HighLow(){
        Position pos = new Position(0,9);
        pos.flipY();
        assertEquals(0, pos.getY());
    }
}