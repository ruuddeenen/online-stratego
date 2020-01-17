package service.controllers.websocket;

import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.messages.Operation;
import service.messages.both.IPlayerList;
import service.messages.incoming.interfaces.Messegeable;
import service.messages.incoming.messages.GameMessage;
import service.messages.incoming.messages.GameStartMessage;
import service.messages.incoming.messages.Message;
import service.messages.outgoing.models.ErrorResponse;
import service.messages.outgoing.models.PlayerListResponse;
import service.messages.outgoing.models.Response;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LobbyControllerTest {

    private LobbyController controller;
    private Player testPlayer1 = new Player("testId1", "testUsername1");
    private Player testPlayer2 = new Player("testId2", "testUsername2");
    private String testLobbyId = "TEST1";

    @BeforeEach
    void setUp() {
        controller = new LobbyController();
    }

    private List<Player> setPlayerList(IPlayerList message) {
        List<Player> playerList = new ArrayList<>();
        playerList.add(testPlayer1);
        playerList.add(testPlayer2);
        message.setPlayerList(playerList);
        return playerList;
    }

    @Test
    void test_startGame() {
        GameStartMessage gameStartMessage = new GameStartMessage();
        gameStartMessage.setPlayer(testPlayer1);
        gameStartMessage.setLobbyId(testLobbyId);
        List<Player> playerList = setPlayerList(gameStartMessage);

        Response response = controller.startGame(gameStartMessage);

        assertTrue(response instanceof PlayerListResponse);

        PlayerListResponse playerListResponse = (PlayerListResponse) response;
        assertEquals(Operation.OPEN_GAME, playerListResponse.getOperation());
        assertEquals(testLobbyId, playerListResponse.getLobbyId());
        assertEquals(playerList, playerListResponse.getPlayerList());
    }

    @Test
    void test_startGameNullLobbyId() {
        GameStartMessage gameStartMessage = new GameStartMessage();
        gameStartMessage.setPlayer(testPlayer1);
        setPlayerList(gameStartMessage);

        Response response = controller.startGame(gameStartMessage);

        assertTrue(response instanceof ErrorResponse);
    }

    @Test
    void test_startGameBlankLobbyId() {
        GameStartMessage gameStartMessage = new GameStartMessage();
        gameStartMessage.setPlayer(testPlayer1);
        setPlayerList(gameStartMessage);
        gameStartMessage.setLobbyId("");

        Response response = controller.startGame(gameStartMessage);

        assertTrue(response instanceof ErrorResponse);
    }

    @Test
    void test_startGameNotEnoughPlayers() {
        GameStartMessage message = new GameStartMessage();
        message.setPlayer(testPlayer1);
        message.setLobbyId(testLobbyId);

        Response response = controller.startGame(message);

        assertTrue(response instanceof ErrorResponse);
    }

    @Test
    void test_startGameTooMuchPlayers() {
        GameStartMessage gameStartMessage = new GameStartMessage();
        gameStartMessage.setPlayer(testPlayer1);
        List<Player> playerList = setPlayerList(gameStartMessage);
        playerList.add(testPlayer2);
        gameStartMessage.setPlayerList(playerList);

        Response response = controller.startGame(gameStartMessage);

        assertTrue(response instanceof ErrorResponse);
    }

    @Test
    void test_connectPlayerNull() {
        Message message = new Message();
        Response response = controller.connect(message);

        assertNull(response);
    }

    private String createNewLobby(Player player){
        Message message = new Message();
        message.setPlayer(player);
        PlayerListResponse response = (PlayerListResponse) controller.connect(message);
        return response.getLobbyId();
    }

    @Test
    void test_connectLobbyIdNull() {
        Message message = new Message();
        message.setPlayer(testPlayer1);
        Response response = controller.connect(message);

        assertTrue(response instanceof PlayerListResponse);
        PlayerListResponse playerListResponse = (PlayerListResponse) response;

        assertEquals(Operation.NEW_LOBBY, playerListResponse.getOperation());
        assertEquals(1, playerListResponse.getPlayerList().size());
        assertEquals(testPlayer1.getId(), playerListResponse.getPlayerList().get(0).getId());
        assertEquals(5, playerListResponse.getLobbyId().length());
    }

    @Test
    void test_connectLobbyIdBlank() {
        Message message = new Message();
        message.setPlayer(testPlayer1);
        message.setLobbyId("");
        Response response = controller.connect(message);

        assertTrue(response instanceof PlayerListResponse);
        PlayerListResponse playerListResponse = (PlayerListResponse) response;

        assertEquals(Operation.NEW_LOBBY, playerListResponse.getOperation());
        assertEquals(1, playerListResponse.getPlayerList().size());
        assertEquals(testPlayer1.getId(), playerListResponse.getPlayerList().get(0).getId());
        assertEquals(5, playerListResponse.getLobbyId().length());
    }

    @Test
    void test_connectWithLobbyId() {
        String createdLobbyId = createNewLobby(testPlayer1);
        Message message = new Message();
        message.setPlayer(testPlayer2);
        message.setLobbyId(createdLobbyId);
        Response response = controller.connect(message);

        assertTrue(response instanceof PlayerListResponse);
        PlayerListResponse playerListResponse = (PlayerListResponse) response;

        assertEquals(Operation.JOINED_LOBBY, playerListResponse.getOperation());
        assertEquals(2, playerListResponse.getPlayerList().size());
        assertEquals(createdLobbyId, playerListResponse.getLobbyId());
    }

    @Test
    void test_connectWithLobbyIdAlreadyInLobby() {
        String createdLobbyId = createNewLobby(testPlayer1);
        Message message = new Message();
        message.setPlayer(testPlayer1);
        message.setLobbyId(createdLobbyId);
        Response response = controller.connect(message);

        assertTrue(response instanceof PlayerListResponse);
        PlayerListResponse playerListResponse = (PlayerListResponse) response;

        assertEquals(Operation.JOINED_LOBBY, playerListResponse.getOperation());
        assertEquals(1, playerListResponse.getPlayerList().size());
        assertEquals(testPlayer1.getId(), playerListResponse.getPlayerList().get(0).getId());
        assertEquals(createdLobbyId, playerListResponse.getLobbyId());
    }


    @Test
    void test_connectWithLobbyIdLobbyFull() {
        String createdLobbyId = createNewLobby(testPlayer1);
        Message message = new Message();
        message.setPlayer(testPlayer2);
        message.setLobbyId(createdLobbyId);
        controller.connect(message);

        message = new Message();
        message.setPlayer(new Player("id", "username"));
        message.setLobbyId(createdLobbyId);
        Response response = controller.connect(message);


        assertTrue(response instanceof ErrorResponse);
    }

}