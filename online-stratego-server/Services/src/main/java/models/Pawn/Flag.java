package models.Pawn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.Position;
import models.enums.Color;

public class Flag extends Pawn{
    @JsonCreator
    public Flag(@JsonProperty("color")Color color) {
        super(0, color);
    }

    @Override
    public boolean canMoveTo(Position position){
        return false;
    }

}
