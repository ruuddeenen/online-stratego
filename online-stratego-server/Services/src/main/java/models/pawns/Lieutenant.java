package models.pawns;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.Color;

public class Lieutenant extends Pawn {
    @JsonCreator
    public Lieutenant(@JsonProperty("color")Color color) {
        super(5, color);
    }
}
