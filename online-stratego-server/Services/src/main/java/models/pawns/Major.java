package models.pawns;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.Color;

public class Major extends Pawn {
    @JsonCreator
    public Major(@JsonProperty("color")Color color) {
        super(7, color);
    }
}
