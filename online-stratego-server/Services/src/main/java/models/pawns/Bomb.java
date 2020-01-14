package models.pawns;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.Position;
import models.enums.Color;

public class Bomb extends Pawn {
    @JsonCreator
    public Bomb(@JsonProperty("color") Color color) {
        super(11, color);
    }

    @Override
    public boolean canMoveTo(Position position){
        return false;
    }
}
