package models.Pawn;

import models.Position;
import models.enums.Color;

public class Scout extends Pawn {
    public Scout(Color color) {
        super(2, color);
    }

    @Override
    public boolean canMoveTo(Position newPosition){
        return (position.getX() == newPosition.getX() || position.getY() == newPosition.getY());
    }
}
