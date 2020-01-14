package service.controllers.websocket;

import models.GameRepository;
import models.Player;
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
    private GameRepository gameRepository = GameRepository.getInstance();


    @MessageMapping("/lobby/startgame")
    @SendTo("/topic/lobby")
    public Response startGame(GameStartMessage message) {
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
        gameRepository.addPlayer(message.getId(), message.getUsername());
        if (message.getLobbyId().isBlank()) {        // New lobby
            Player player = gameRepository.getPlayerById(message.getId());
            player.setColor(null);

            String lobbyId = gameRepository.createLobby();
            gameRepository.addPlayerToLobby(lobbyId, player);
            return createLobbyResponse(Operation.NEW_LOBBY, player, lobbyId);
        } else {                                    // Existing lobby
            return joinLobby(message);
        }
    }


    private Response joinLobby(ConnectMessage message) {
        String playerId = message.getId();
        String username = message.getUsername();
        String lobbyId = message.getLobbyId();

        Player player = gameRepository.getPlayerById(playerId);
        if (player == null) {
            player = gameRepository.addPlayer(playerId, username);
        }

        List<Player> playersInLobby = gameRepository.getPlayersFromLobby(lobbyId);

        if (playersInLobby.contains(player)) {                      // If player is already in lobby
            return createLobbyResponse(Operation.JOINED_LOBBY, player, message.getLobbyId());
        } else if (!gameRepository.isLobbyFull(lobbyId)) {                     // If player can be added to lobby
            gameRepository.addPlayerToLobby(lobbyId, player);
            return createLobbyResponse(Operation.JOINED_LOBBY, player, lobbyId);
        } else                                                      // If lobby is full
            return new ErrorResponse("Lobby is full! Could not join");
    }

    private LobbyResponse createLobbyResponse(Operation operation, Player player, String lobbyId) {
        return new LobbyResponse(
                operation,
                player.getId(),
                lobbyId,
                gameRepository.getPlayersFromLobby(lobbyId)
        );
    }
}
