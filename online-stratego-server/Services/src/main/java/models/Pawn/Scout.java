package models.Pawn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.Position;
import models.enums.Color;

public class Scout extends Pawn {
    @JsonCreator
    public Scout(@JsonProperty("color")Color color) {
        super(2, color);
    }

    @Override
    public boolean canMoveTo(Position newPosition){
        return (position.getX() == newPosition.getX() || position.getY() == newPosition.getY());
    }
}
