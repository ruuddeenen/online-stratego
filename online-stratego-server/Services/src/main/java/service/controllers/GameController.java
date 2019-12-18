package service.controllers;

import models.Game;
import models.Player;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import service.messages.ConnectMessage;
import service.responses.ConnectResponse;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {
    ArrayList<Player> players;
    Game game;
    private boolean[][] STANDARD_FIELD = new boolean[][]{
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, false, false, true, true, false, false, true, true},
            {true, true, false, false, true, true, false, false, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true},
    };

    public GameController() {
        players = new ArrayList<>();
    }

    @MessageMapping("/game")
    @SendTo("/topic/game")
    public ConnectResponse connect(ConnectMessage message) {
        players.add(new Player(message.getId(), message.getUsername()));
        ConnectResponse response = new ConnectResponse();
        response.setPlayerList(players);
        response.setField(STANDARD_FIELD);
        response.setPawns(null);
        return response;
    }
}
