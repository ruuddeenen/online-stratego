package service.controllers.websocket;

import com.google.gson.Gson;
import models.Game;
import models.Pawn.*;
import models.PawnFactory;
import models.Player;
import models.enums.Color;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import service.Operation;
import service.ResponseMessage;
import service.messages.ConnectMessage;
import service.responses.ConnectResponse;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {
    Gson gson = new Gson();
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
    public ResponseMessage connect(ConnectMessage message) {
        players.add(new Player(message.getId(), message.getUsername()));

        ConnectResponse response = new ConnectResponse();
        response.setPlayerList(players);
        response.setField(STANDARD_FIELD);
        // response.setPawns(getStandardPawns());

        return new ResponseMessage(
                Operation.CONNECT,
                "from",
                "lobbyId",
                gson.toJson(response)
        );
    }

    private List<Pawn> getStandardPawns(Color color) {
        PawnFactory factory = new PawnFactory(color);
        List<Pawn> pawns = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            if (i < 6) {
                factory.getPawn("BOMB");        // 6
            } else if (i < 7){
                factory.getPawn("MARSHAL");     // 1
            } else if (i < 8){
                factory.getPawn("GENERAL");     // 1
            } else if (i < 10){
                factory.getPawn("COLONEL");     // 2
            } else if (i < 13){
                factory.getPawn("MAJOR");       // 3
            } else if (i < 17){
                factory.getPawn("CAPTAIN");     // 4
            } else if (i < 21){
                factory.getPawn("LIEUTENANT");  // 4
            } else if (i < 25){
                factory.getPawn("SERGEANT");    // 4
            } else if (i < 30){
                factory.getPawn("MINER");       // 5
            } else if (i < 38){
                factory.getPawn("SCOUT");       // 8
            } else if (i < 39){
                factory.getPawn("SPY");         // 1
            } else {
                factory.getPawn("FLAG");        // 1
            }
        }
        return pawns;
    }
}
