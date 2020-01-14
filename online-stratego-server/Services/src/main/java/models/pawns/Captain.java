package models.pawns;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.Color;

public class Captain extends Pawn{
    @JsonCreator
    public Captain(@JsonProperty("color") Color color) {
        super(6, color);
    }
}
