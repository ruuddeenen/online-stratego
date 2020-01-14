package models.pawns;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.Color;

public class UnknownPawn extends Pawn {
    @JsonCreator
    public UnknownPawn(@JsonProperty("color") Color color) {
        super(-1, color);
    }

    public UnknownPawn(Pawn pawn) {
        super(-1, pawn.getColor());
        this.position = pawn.getPosition();
    }
}
