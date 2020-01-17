package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.Color;

public class Player {
    private String id;
    private String username;
    private Color color;

    @JsonCreator
    public Player(@JsonProperty("id") String id, @JsonProperty("username") String username){
        this.id = id;
        this.username = username;
    }

    public Player(String id, String username, Color color){
        this(id, username);
        this.color = color;
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
