package service.controllers.websocket;

import com.google.gson.Gson;
import models.Player;
import models.enums.Color;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import service.Operation;
import service.messages.LobbyResponseMessage;
import service.messages.ResponseMessage;
import service.messages.ConnectMessage;

import java.util.*;

@Controller
public class LobbyController {
    private Gson gson = new Gson();
    private static Map<String, List<Player>> lobbyPlayerMap = new HashMap<>();
    private static Set<Player> playerSet = new HashSet<>();

    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public ResponseMessage connect(ConnectMessage message) {
        Player player = getPlayerById(message.getId(), message.getUsername());

        List<Player> playersInLobby = getPlayerListFromLobbyId(message.getLobbyId());

        if (playersInLobby.contains(player)) {                      // If player is already in lobby
            return playerAlreadyInLobby(message.getLobbyId());
        } else if (message.getLobbyId().length() != 5) {
            return createNewLobbyAndAddPlayer(player);
        } else if (playersInLobby.size() < 2) {                     // If player can be added to lobby
            return addPlayerToLobby(playersInLobby, player, message.getLobbyId());
        } else                                                      // If lobby is full
            return new ResponseMessage();
    }


    private Player getPlayerById(String id, String username) {
        for (Player p : playerSet
        ) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        Player p = new Player(id, username);
        playerSet.add(p);
        return p;
    }


    private ResponseMessage playerAlreadyInLobby(String lobbyId) {
        return new LobbyResponseMessage(Operation.JOINED_LOBBY, null, lobbyId, lobbyPlayerMap.get(lobbyId));
    }

    private List<Player> getPlayerListFromLobbyId(String lobbyId) {
        List<Player> players = lobbyPlayerMap.get(lobbyId);
        if (players == null) {
            return new ArrayList<>();
        }
        return players;
    }

    private ResponseMessage createNewLobbyAndAddPlayer(Player player) {
        String lobbyId = RandomStringUtils.randomAlphanumeric(5);
        List<Player> players = new ArrayList<>();
        players.add(player);
        lobbyPlayerMap.put(lobbyId, players);
        return new LobbyResponseMessage(
                Operation.JOINED_LOBBY,
                player.getId(),
                lobbyId,
                players
        );
    }

    private ResponseMessage addPlayerToLobby(List<Player> players, Player player, String lobbyId) {
        players.add(player);
        lobbyPlayerMap.put(lobbyId, players);

        if (players.size() == 2) {
            Collections.shuffle(players);
            players.get(0).setColor(Color.RED);
            players.get(1).setColor(Color.BLUE);
        }

        return new LobbyResponseMessage(
                Operation.JOINED_LOBBY,
                player.getId(),
                lobbyId,
                players
        );
    }
}
