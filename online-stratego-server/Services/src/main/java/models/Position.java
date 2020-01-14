package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {
    private int x;
    private int y;

    @JsonCreator
    public Position(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void flipX() {
        this.x = 10 - (x + 1);
    }

    public int flipY() {
        this.y = 10 - (y + 1);
        return y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
