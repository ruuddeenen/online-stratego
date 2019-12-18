package models;

import models.Pawn.Pawn;
import models.enums.Color;

import java.util.List;

public class Player {
    private int id;
    private String username;
    private Color color;
    private List<Pawn> defeatedPawns;

    public Player(int id, String username){
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
