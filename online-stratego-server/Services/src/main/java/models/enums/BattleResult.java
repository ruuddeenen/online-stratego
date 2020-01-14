package models.enums;

public enum BattleResult {

    WON("Won"),
    DRAW("Draw"),
    LOST("Lost"),
    GAME_WON("Game won!");

    BattleResult(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String toString() {
        return name;
    }
}
