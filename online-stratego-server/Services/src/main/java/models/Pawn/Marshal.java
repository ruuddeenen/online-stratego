package models.Pawn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.Color;

public class Marshal extends Pawn {
    @JsonCreator
    public Marshal(@JsonProperty("color") Color color) {
        super(10, color);
    }
}
