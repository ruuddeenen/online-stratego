package service.controllers.websocket;

import models.*;
import models.Pawn.*;
import models.enums.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import service.Operation;
import service.messages.incoming.GameConnectMessage;
import service.messages.incoming.GetAvailableMovesMessage;
import service.messages.incoming.MoveMessage;
import service.messages.incoming.GameMessage;
import service.messages.responses.*;

import java.util.*;

@Controller
public class GameController {
    private static Map<String, Game> gameMap = new HashMap<>();
    // private static Set<Player> playerSet = new HashSet<>();
    private static SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public GameController(SimpMessageSendingOperations messagingTemplate) {
        GameController.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/move")
    @SendTo("/topic/game")
    public Response move(MoveMessage message) {
        String lobbyId = message.getLobbyId();
        Game game = gameMap.get(lobbyId);
        Pawn pawn = message.getPawn();
        Player player = game.getPlayerById(message.getId());

        if (pawn.getColor() != player.getColor()){
            return new ErrorResponse("Trying to move opponents pawn!");
        }
        Board board = game.getBoard();

        boolean success = board.movePawn(pawn.getPosition(), message.getPosition());
        if (success) {
            for (Player p : game.getPlayerSet()
                 ) {
                GameResponse response = new GameResponse(
                        Operation.MOVE_PAWN,
                        p.getId(),
                        lobbyId,
                        board.getPawnsForColor(p.getColor()),
                        board.getDefeatedPawnsByColor(p.getColor()),
                        game.getTurn()
                );
                messagingTemplate.convertAndSend("/topic/game", response);
            }
        } else {
            return new ErrorResponse("Not a valid move!");
        }
        return null;
    }


    @MessageMapping("/game/moves")
    @SendTo("/topic/game")
    public Response getMoves(GetAvailableMovesMessage message) {
        String lobbyId = message.getLobbyId();
        Pawn pawn = message.getPawn();
        Game game = gameMap.get(lobbyId);
        if (game.getPlayerById(message.getId()).getColor() == pawn.getColor()) {
            return new AvailableMovesResponse(
                    Operation.POSSIBLE_MOVES,
                    message.getId(),
                    message.getLobbyId(),
                    game.getBoard().getPossibleMoves(pawn),
                    game.getBoard().getPossibleAttacks(pawn)
            );
        } else {
            return new ErrorResponse("Trying to move opponents pawn!");
        }

    }


    @MessageMapping("/game/ready")
    @SendTo("/topic/game")
    public void readyUp(GameMessage message) {
        String lobbyId = message.getLobbyId();
        Game game = gameMap.get(lobbyId);
        Player player = game.getPlayerById(message.getId());
        assert player != null;
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
                        game.getBoard().getPawnsForColor(p.getColor()),
                        game.getBoard().getDefeatedPawnsByColor(p.getColor()),
                        game.getTurn()
                );
                messagingTemplate.convertAndSend("/topic/game", responseMessage);
            }
        }
    }

    @MessageMapping("/game")
    @SendTo("/topic/game")
    public Response connect(GameConnectMessage message) {
        String lobbyId = message.getLobbyId();
        // Get game / Create new game
        Game game = gameMap.get(lobbyId);
        if (game == null) {
            gameMap.put(lobbyId, new Game());
            game = gameMap.get(lobbyId);
        }

        // Get player + set opponent
        List<Player> players = LobbyController.getPlayersByLobbyId(lobbyId);

        Player player = null;
        Player opponent = null;
        for (Player p : players
        ) {
            if (p.getId().equals(message.getId())) {
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
                game.getBoard().getField()
        );
    }
}
