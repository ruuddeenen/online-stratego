package models;

import java.util.HashSet;
import java.util.Set;

public class Game {
    private Board board;
    private Set<Player> playerSet;

    public Game() {
        this.board = new Board();
        this.playerSet = new HashSet<>();
    }

    public void addPlayer(Player player) {
        playerSet.add(player);
    }

}
