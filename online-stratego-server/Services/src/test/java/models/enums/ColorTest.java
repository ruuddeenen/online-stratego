package models.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void oppositeOfRedBlue() {
        assertEquals(Color.BLUE, Color.oppositeOf(Color.RED));
        assertEquals(Color.RED, Color.oppositeOf(Color.BLUE));
        assertNull(Color.oppositeOf(null));
    }
}