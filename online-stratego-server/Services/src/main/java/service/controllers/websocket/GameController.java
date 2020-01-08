package service.controllers.websocket;

import models.*;
import models.Pawn.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import service.Operation;
import service.messages.incoming.GameConnectMessage;
import service.messages.incoming.GetAvailableMovesMessage;
import service.messages.incoming.MoveMessage;
import service.messages.incoming.ReadyUpMessage;
import service.messages.responses.*;

import java.util.*;

@Controller
public class GameController {
    private static Map<String, Game> gameMap = new HashMap<>();
    private static Set<Player> playerSet = new HashSet<>();
    private static SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public GameController(SimpMessageSendingOperations messagingTemplate) {
        GameController.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/move")
    @SendTo("/topic/game")
    public Response move(MoveMessage message){
        String lobbyId = message.getLobbyId();
        Game game = gameMap.get(lobbyId);
        Pawn pawn = message.getPawn();
        Position oldPosition = pawn.getPosition();
        boolean success = game.getBoard().movePawn(message.getPawn(), message.getPosition());
        Position newPosition = pawn.getPosition();
        if (success){
            return new MoveResponse(
                    Operation.MOVE_PAWN,
                    message.getId(),
                    message.getLobbyId(),
                    oldPosition,
                    newPosition
            );
        } else {
            return new ErrorResponse("Not a valid move!");
        }
    }


    @MessageMapping("/game/moves")
    @SendTo("/topic/game")
    public Response getMoves(GetAvailableMovesMessage message) {
        String lobbyId = message.getLobbyId();
        Pawn pawn = message.getPawn();
        Game game = gameMap.get(lobbyId);

        return new AvailableMovesResponse(
                Operation.POSSIBLE_MOVES,
                message.getId(),
                message.getLobbyId(),
                game.getBoard().getPossibleMoves(pawn)
        );
    }


    @MessageMapping("/game/ready")
    @SendTo("/topic/game")
    public void readyUp(ReadyUpMessage message) {
        String lobbyId = message.getLobbyId();
        Game game = gameMap.get(lobbyId);
        Player player = getPlayerById(message.getId());
        assert player != null;
        models.enums.Color color = player.getColor();

        List<Pawn> pawnList = message.getPawnList();
        game.addPawns(pawnList, color);

        if (game.isReady()) {
            for (Player p : game.getPlayerSet()
            ) {
                GameResponse responseMessage = new GameResponse(
                        Operation.START_GAME,
                        p.getId(),
                        lobbyId,
                        game.getBoard().getPawnsForPlayer(p),
                        game.getTurn()
                );
                messagingTemplate.convertAndSend("/topic/game", responseMessage);
            }
        }
    }

    @MessageMapping("/game")
    @SendTo("/topic/game")
    public Response connect(GameConnectMessage message) {
        playerSet.add(new Player(message.getId(), message.getUsername(), message.getColor()));
        List<Player> players = LobbyController.getPlayersByLobbyId(message.getLobbyId());
        Player player = null, opponent = null;
        for (Player p : players
        ) {
            if (p.getId().equals(message.getId())) {
                player = p;
            } else {
                opponent = p;
            }
        }

        Game game = gameMap.get(message.getLobbyId());
        if (game == null) {
            gameMap.put(message.getLobbyId(), new Game());
            game = gameMap.get(message.getLobbyId());
        }
        game.addPlayer(player);

        assert player != null;
        return new GameStartResponse(
                Operation.START_PREP,
                player.getId(),
                opponent,
                message.getLobbyId(),
                Board.getStandardPawns(player.getColor()),
                STANDARD_FIELD

        );
    }

    private Player getPlayerById(String id) {
        for (Player p : playerSet
        ) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
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
