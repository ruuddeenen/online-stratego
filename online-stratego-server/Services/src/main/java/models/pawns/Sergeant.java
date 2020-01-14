package models.pawns;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.Color;

public class Sergeant extends Pawn {
    @JsonCreator
    public Sergeant(@JsonProperty("color")Color color) {
        super(4, color);
    }
}
