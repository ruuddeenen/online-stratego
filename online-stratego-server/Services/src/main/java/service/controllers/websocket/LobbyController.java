package service.controllers.websocket;

import models.Player;
import models.enums.Color;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import service.Operation;
import service.messages.incoming.GameStartMessage;
import service.messages.responses.LobbyResponseMessage;
import service.messages.responses.ResponseMessage;
import service.messages.incoming.ConnectMessage;

import java.util.*;

@Controller
public class LobbyController {
    private static Map<String, List<Player>> lobbyPlayerMap = new HashMap<>();
    private static Set<Player> playerSet = new HashSet<>();


    @MessageMapping("/lobby/startgame")
    @SendTo("/topic/lobby")
    public ResponseMessage startGame(GameStartMessage message) {
        return new ResponseMessage(
                Operation.START_GAME,
                null,
                message.getLobbyId()
        );
    }


    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public ResponseMessage connect(ConnectMessage message) {
        if (message.getLobbyId().isBlank()) {        // New lobby
            Player player = getPlayerById(message.getId(), message.getUsername());
            player.setColor(null);
            return createNewLobbyAndAddPlayer(player);
        } else {                                    // Existing lobby
            return joinLobby(message);
        }
    }


    private ResponseMessage joinLobby(ConnectMessage message) {
        // Get player from message
        Player player = getPlayerById(message.getId(), message.getUsername());

        List<Player> playersInLobby = getPlayerListFromLobbyId(message.getLobbyId());

        if (playersInLobby.contains(player)) {                      // If player is already in lobby
            return playerAlreadyInLobby(player, message.getLobbyId());
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


    private ResponseMessage playerAlreadyInLobby(Player player, String lobbyId) {
        return new LobbyResponseMessage(Operation.JOINED_LOBBY, player.getId(), lobbyId, lobbyPlayerMap.get(lobbyId));
    }

    private List<Player> getPlayerListFromLobbyId(String lobbyId) {
        List<Player> players = lobbyPlayerMap.get(lobbyId);
        if (players == null) {
            return new ArrayList<>();
        }
        return players;
    }

    private ResponseMessage createNewLobbyAndAddPlayer(Player player) {
        String lobbyId = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        List<Player> players = new ArrayList<>();
        players.add(player);
        lobbyPlayerMap.put(lobbyId, players);
        return new LobbyResponseMessage(
                Operation.NEW_LOBBY,
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
