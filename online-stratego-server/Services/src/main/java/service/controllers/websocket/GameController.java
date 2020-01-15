package service.controllers.websocket;

import models.*;
import models.pawns.*;
import models.enums.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import service.messages.Operation;
import service.messages.incoming.messages.AvailableMovesMessage;
import service.messages.incoming.messages.GameMessage;
import service.messages.incoming.messages.Message;
import service.messages.incoming.messages.MoveMessage;
import service.messages.outgoing.models.*;

import java.util.*;

@Controller
public class GameController {
    private static String destination = "/topic/game";
    private static SimpMessageSendingOperations messagingTemplate;
    private GameRepository gameRepository = GameRepository.getInstance();

    @Autowired
    public GameController(SimpMessageSendingOperations messagingTemplate) {
        GameController.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/move")
    public void move(MoveMessage message) {
        String lobbyId = message.getLobbyId();
        Game game = gameRepository.getGameById(lobbyId);
        Pawn pawn = message.getPawn();
        Player player = game.getPlayerById(message.getPlayer().getId());

        if (game.movePawn(player, pawn, message.getPosition())) {
            game.getPlayerSet().forEach(p -> {
                GameResponse response = new GameResponse(
                        Operation.MOVE_PAWN,
                        p.getId(),
                        lobbyId,
                        game.getPawnsByColor(p.getColor()),
                        game.getDefeatedPawnsByColor(p.getColor()),
                        game.getTurn()
                );
                messagingTemplate.convertAndSend(destination, response);
            });
            if (game.isOver()) {
                game.revealAll();
                game.getPlayerSet().forEach(p -> {
                    EndGameResponse response = new EndGameResponse(
                            Operation.GAME_OVER,
                            p.getId(),
                            lobbyId,
                            game.getWinner(),
                            game.getPawnsByColor(p.getColor())
                    );
                    messagingTemplate.convertAndSend(destination, response);
                });
            }
        } else {
            messagingTemplate.convertAndSend(destination, new ErrorResponse("Not a valid move!"));
        }

    }

    @MessageMapping("/game/moves")
    @SendTo("/topic/game")
    public Response getMoves(AvailableMovesMessage message) {
        String lobbyId = message.getLobbyId();
        Pawn pawn = message.getPawn();
        Game game = gameRepository.getGameById(lobbyId);
        Player player = game.getPlayerById(message.getPlayer().getId());
        if (game.isTurn(player)) {
            if (player.getColor() == pawn.getColor()) {
                return new AvailableMovesResponse(
                        Operation.POSSIBLE_MOVES,
                        player.getId(),
                        message.getLobbyId(),
                        game.getPossibleMoves(pawn),
                        game.getPossibleAttacks(pawn)
                );
            }
            return null;
        }
        Response response = new ErrorResponse("Trying to move while not your turn.");
        response.setOperation(Operation.NOT_YOUR_TURN);
        response.setLobbyId(message.getLobbyId());
        response.setReceiver(message.getPlayer().getId());
        return response;
    }


    @MessageMapping("/game/ready")
    @SendTo("/topic/game")
    public void readyUp(GameMessage message) {
        String lobbyId = message.getLobbyId();
        Game game = gameRepository.getGameById(lobbyId);
        Player player = game.getPlayerById(message.getPlayer().getId());
        Color color = player.getColor();

        List<Pawn> pawnList = message.getPawnList();
        game.addPawns(pawnList, color);

        if (game.isReady()) {
            for (Player p : game.getPlayerSet()
            ) {
                GameResponse responseMessage = new GameResponse(
                        Operation.START_GAME,
                        p.getId(),
                        lobbyId,
                        game.getPawnsByColor(p.getColor()),
                        game.getDefeatedPawnsByColor(p.getColor()),
                        game.getTurn()
                );
                messagingTemplate.convertAndSend(destination, responseMessage);
            }
        }
    }

    @MessageMapping("/game")
    @SendTo("/topic/game")
    public Response connect(Message message) {
        String lobbyId = message.getLobbyId();
        Game game = gameRepository.getOrCreateGame(lobbyId);

        List<Player> players = gameRepository.getPlayersFromLobby(lobbyId);

        Player player = null;
        Player opponent = null;
        for (Player p : players
        ) {
            if (p.getId().equals(message.getPlayer().getId())) {
                player = p;
            } else {
                opponent = p;
            }
        }
        assert player != null;
        game.addPlayer(player);

        return new GameStartResponse(
                Operation.START_PREP,
                player.getId(),
                opponent,
                message.getLobbyId(),
                Board.getStandardPawns(player.getColor()),
                game.getField()
        );
    }
}
