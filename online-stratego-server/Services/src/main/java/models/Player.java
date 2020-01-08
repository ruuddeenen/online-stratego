package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.Pawn.Pawn;

import java.util.List;

public class Player {
    private String id;
    private String username;
    private models.enums.Color color;
    private List<Pawn> defeatedPawns;

    @JsonCreator
    public Player(@JsonProperty("id") String id, @JsonProperty("username") String username){
        this.id = id;
        this.username = username;
    }

    public Player(String id, String username, models.enums.Color color){
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

    public void setColor(models.enums.Color color){
        this.color = color;
    }

    public models.enums.Color getColor(){
        return color;
    }

}
