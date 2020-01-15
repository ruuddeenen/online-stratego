package service.controllers.websocket;

import models.GameRepository;
import models.Player;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import service.Operation;
import service.messages.incoming.GameStartMessage;
import service.messages.incoming.Message;
import service.messages.responses.ErrorResponse;
import service.messages.responses.LobbyResponse;
import service.messages.responses.Response;

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
    public Response connect(Message message) {
        gameRepository.addPlayer(message.getPlayer());
        if (message.getLobbyId().isBlank()) {        // New lobby
            Player player = gameRepository.getPlayerById(message.getPlayer().getId());
            player.setColor(null);

            String lobbyId = gameRepository.createLobby();
            gameRepository.addPlayerToLobby(lobbyId, player);
            return createLobbyResponse(Operation.NEW_LOBBY, player, lobbyId);
        } else {                                    // Existing lobby
            return joinLobby(message);
        }
    }


    private Response joinLobby(Message message) {
        String playerId = message.getPlayer().getId();
        String lobbyId = message.getLobbyId();

        Player player = gameRepository.getPlayerById(playerId);
        if (player == null) {
            player = gameRepository.addPlayer(message.getPlayer());
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
        List<Player> playerList = gameRepository.getPlayersFromLobby(lobbyId);
        return new LobbyResponse(
                operation,
                player.getId(),
                lobbyId,
                playerList
        );
    }
}
