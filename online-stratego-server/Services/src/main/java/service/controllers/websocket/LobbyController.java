package service.controllers.websocket;

import models.Player;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import service.Operation;
import service.messages.incoming.GameStartMessage;
import service.messages.responses.ErrorResponse;
import service.messages.responses.LobbyResponse;
import service.messages.responses.Response;
import service.messages.incoming.ConnectMessage;

import java.util.*;

@Controller
public class LobbyController {
    private static Map<String, List<Player>> lobbyPlayerMap = new HashMap<>();
    private static Set<Player> playerSet = new HashSet<>();

    static List<Player> getPlayersByLobbyId(String lobbyId){
        return lobbyPlayerMap.get(lobbyId);
    }


    @MessageMapping("/lobby/startgame")
    @SendTo("/topic/lobby")
    public Response startGame(GameStartMessage message) {
        // lobbyPlayerMap.remove(message.getLobbyId());
        return new LobbyResponse(
                Operation.OPEN_GAME,
                null,
                message.getLobbyId(),
                message.getPlayerList()
        );
    }

    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public Response connect(ConnectMessage message) {
        if (message.getLobbyId().isBlank()) {        // New lobby
            Player player = getPlayerById(message.getId(), message.getUsername());
            player.setColor(null);
            return createNewLobbyAndAddPlayer(player);
        } else {                                    // Existing lobby
            return joinLobby(message);
        }
    }


    private Response joinLobby(ConnectMessage message) {
        Player player = getPlayerById(message.getId(), message.getUsername());

        List<Player> playersInLobby = getPlayerListFromLobbyId(message.getLobbyId());

        if (playersInLobby.contains(player)) {                      // If player is already in lobby
            return playerAlreadyInLobby(player, message.getLobbyId());
        } else if (playersInLobby.size() < 2) {                     // If player can be added to lobby
            return addPlayerToLobby(playersInLobby, player, message.getLobbyId());
        } else                                                      // If lobby is full
            return new ErrorResponse("Lobby is full! Could not join");
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


    private Response playerAlreadyInLobby(Player player, String lobbyId) {
        return new LobbyResponse(Operation.JOINED_LOBBY, player.getId(), lobbyId, lobbyPlayerMap.get(lobbyId));
    }

    private List<Player> getPlayerListFromLobbyId(String lobbyId) {
        List<Player> players = lobbyPlayerMap.get(lobbyId);
        if (players == null) {
            return new ArrayList<>();
        }
        return players;
    }

    private Response createNewLobbyAndAddPlayer(Player player) {
        String lobbyId = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        List<Player> players = new ArrayList<>();
        players.add(player);
        lobbyPlayerMap.put(lobbyId, players);
        return new LobbyResponse(
                Operation.NEW_LOBBY,
                player.getId(),
                lobbyId,
                players
        );
    }

    private Response addPlayerToLobby(List<Player> players, Player player, String lobbyId) {
        players.add(player);
        lobbyPlayerMap.put(lobbyId, players);

        if (players.size() == 2) {
            Collections.shuffle(players);
            players.get(0).setColor(models.enums.Color.RED);
            players.get(1).setColor(models.enums.Color.BLUE);
        }

        return new LobbyResponse(
                Operation.JOINED_LOBBY,
                player.getId(),
                lobbyId,
                players
        );
    }
}
