package models.pawns;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.Color;

public class Colonel extends Pawn {
    @JsonCreator
    public Colonel(@JsonProperty("color")Color color) {
        super(8, color);
    }
}
