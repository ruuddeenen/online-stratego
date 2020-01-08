package models.Pawn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.Color;

public class General extends Pawn {
    @JsonCreator
    public General(@JsonProperty("color")Color color) {
        super(9, color);
    }
}
