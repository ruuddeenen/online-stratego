package models.enums;

public enum Color {
    RED,
    BLUE;

    public static Color oppositeOf(Color color){
        if (color == RED) return BLUE;
        else if (color == BLUE) return RED;
        return null;
    }
}
