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
import service.messages.incoming.GameConnectMessage;
import service.messages.responses.ResponseMessage;
import service.messages.responses.GameStartResponseMessage;

import java.util.*;

@Controller
public class GameController {
    private Gson gson = new Gson();
    private static Map<String, Game> gameMap = new HashMap<>();
    private static Set<Player> playerSet = new HashSet<>();

    @MessageMapping("/game")
    @SendTo("/topic/game")
    public ResponseMessage connect(GameConnectMessage message) throws Exception {
        Player player = getPlayerById(message.getId(), message.getUsername(), message.getColor());

        Game game = gameMap.get(message.getLobbyId());
        if (game == null) {
            gameMap.put(message.getLobbyId(), new Game());
            game = gameMap.get(message.getLobbyId());
        }
        game.addPlayer(player);

        return new GameStartResponseMessage(
                Operation.START_GAME,
                player.getId(),
                message.getLobbyId(),
                getStandardPawns(player.getColor()),
                STANDARD_FIELD

        );
    }

    private Player getPlayerById(String id, String username, Color color) {
        for (Player p : playerSet
        ) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        Player p = new Player(id, username, color);
        playerSet.add(p);
        return p;
    }

    private List<Pawn> getStandardPawns(Color color) {
        PawnFactory factory = new PawnFactory(color);
        List<Pawn> pawns = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            if (i < 6) {
                pawns.add(factory.getPawn("BOMB"));        // 6
            } else if (i < 7) {
                pawns.add(factory.getPawn("MARSHAL"));     // 1
            } else if (i < 8) {
                pawns.add(factory.getPawn("GENERAL"));     // 1
            } else if (i < 10) {
                pawns.add(factory.getPawn("COLONEL"));     // 2
            } else if (i < 13) {
                pawns.add(factory.getPawn("MAJOR"));       // 3
            } else if (i < 17) {
                pawns.add(factory.getPawn("CAPTAIN"));     // 4
            } else if (i < 21) {
                pawns.add(factory.getPawn("LIEUTENANT"));  // 4
            } else if (i < 25) {
                pawns.add(factory.getPawn("SERGEANT"));    // 4
            } else if (i < 30) {
                pawns.add(factory.getPawn("MINER"));       // 5
            } else if (i < 38) {
                pawns.add(factory.getPawn("SCOUT"));       // 8
            } else if (i < 39) {
                pawns.add(factory.getPawn("SPY"));         // 1
            } else {
                pawns.add(factory.getPawn("FLAG"));        // 1
            }
        }
        return pawns;
    }


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
}
