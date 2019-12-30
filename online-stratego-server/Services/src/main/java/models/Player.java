package models;

import models.Pawn.Pawn;
import models.enums.Color;

import java.util.List;

public class Player {
    private String id;
    private String username;
    private Color color;
    private List<Pawn> defeatedPawns;

    public Player(){

    }

    public Player(String id, String username){
        this.id = id;
        this.username = username;
    }

    public Player(String id, String username, Color color){
        this(id, username);
        setColor(color);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }
}
