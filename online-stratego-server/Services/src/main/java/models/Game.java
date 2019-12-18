package models;

import org.apache.commons.lang3.RandomStringUtils;

public class Game {
    private Board board;
    private Player[] players;
    private String code;

    public Game(Player p1, Player p2) {
        this.code = RandomStringUtils.random(5, true, true);
        this.board = new Board();
        this.players = new Player[]{p1, p2};
    }

}
