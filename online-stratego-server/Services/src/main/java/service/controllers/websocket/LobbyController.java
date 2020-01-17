package service.controllers.websocket;

import models.GameRepository;
import models.Player;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import service.messages.Operation;
import service.messages.incoming.messages.GameStartMessage;
import service.messages.incoming.messages.Message;
import service.messages.outgoing.models.ErrorResponse;
import service.messages.outgoing.models.PlayerListResponse;
import service.messages.outgoing.models.Response;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class LobbyController {
    private GameRepository gameRepository = GameRepository.getInstance();
    private Logger logger = Logger.getLogger(LobbyController.class.getName());


    @MessageMapping("/lobby/startgame")
    @SendTo("/topic/lobby")
    public Response startGame(GameStartMessage message) {
        if (message.getLobbyId() == null || message.getLobbyId().isBlank() || message.getPlayerList() == null || message.getPlayerList().size() != 2) {
            return new ErrorResponse("Not all parameters have been filled correctly", message.getPlayer().getId());
        }

        return new PlayerListResponse(
                Operation.OPEN_GAME,
                null,
                message.getLobbyId(),
                message.getPlayerList()
        );
    }

    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public Response connect(Message message) {
        if (message.getPlayer() == null) {
            logger.log(Level.WARNING, "message.getPlayer() == null", new NullPointerException());
            return null;
        }
        gameRepository.addPlayer(message.getPlayer());
        if (message.getLobbyId() == null || message.getLobbyId().isBlank()) {        // New lobby
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

        if (playersInLobby.contains(player)) {                                      // If player is already in lobby
            return createLobbyResponse(Operation.JOINED_LOBBY, player, message.getLobbyId());
        } else if (!gameRepository.isLobbyFull(lobbyId)) {                          // If player can be added to lobby
            gameRepository.addPlayerToLobby(lobbyId, player);
            return createLobbyResponse(Operation.JOINED_LOBBY, player, lobbyId);
        } else                                                                      // If lobby is full
            return new ErrorResponse("Lobby is full! Could not join", message.getPlayer().getId());
    }

    private PlayerListResponse createLobbyResponse(Operation operation, Player player, String lobbyId) {
        List<Player> playerList = gameRepository.getPlayersFromLobby(lobbyId);
        return new PlayerListResponse(
                operation,
                player.getId(),
                lobbyId,
                playerList
        );
    }
}
