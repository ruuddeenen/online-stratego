package service.controllers.websocket;

import com.google.gson.Gson;
import models.Player;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import service.Operation;
import service.ResponseMessage;
import service.messages.ConnectMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LobbyController {
    Gson gson = new Gson();
    private static Map<String, List<Player>> lobbyPlayerMap = new HashMap<>();

    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public ResponseMessage connect(ConnectMessage message) {
        Player player = new Player(message.getId(), message.getUsername());
        List<Player> players = getPlayerListFromLobbyId(message.getLobbyId());

        if (message.getLobbyId() == null) {                 // If no such lobby exists, create new and add player
            return createNewLobbyAndAddPlayer(player);
        } else if (players.contains(player)) {              // If player is already in lobby
            return playerAlreadyInLobby(message.getLobbyId());
        } else if (players.size() < 2) {                    // If player can be added to lobby
            return addPlayerToLobby(players, player, message.getLobbyId());
        } else                                              // If lobby is full
            return new ResponseMessage(null, null, null, null);
    }

    private ResponseMessage playerAlreadyInLobby(String lobbyId) {
        return new ResponseMessage(Operation.JOINED_LOBBY, null, lobbyId, gson.toJson(lobbyPlayerMap.get(lobbyId)));
    }

    private List<Player> getPlayerListFromLobbyId(String lobbyId){
        return lobbyPlayerMap.get(lobbyId);
    }

    private ResponseMessage createNewLobbyAndAddPlayer(Player player) {
        String lobbyId = RandomStringUtils.randomAlphabetic(5);
        List<Player> players = new ArrayList<>();
        players.add(player);
        lobbyPlayerMap.put(lobbyId, players);
        return new ResponseMessage(
                Operation.JOINED_LOBBY,
                "from",
                lobbyId,
                gson.toJson(players)
        );
    }

    private ResponseMessage addPlayerToLobby(List<Player> players, Player player, String lobbyId) {
        players.add(player);
        lobbyPlayerMap.put(lobbyId, players);
        return new ResponseMessage(
                Operation.JOINED_LOBBY,
                null,
                lobbyId,
                gson.toJson(players));
    }
}
