package service.controllers.websocket;

import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.messages.Operation;
import service.messages.incoming.messages.Message;
import service.messages.outgoing.models.ErrorResponse;
import service.messages.outgoing.models.GameStartResponse;
import service.messages.outgoing.models.PlayerListResponse;
import service.messages.outgoing.models.Response;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    private GameController controller;
    private Player testPlayer1 = new Player("testId1", "testUsername1");
    private Player testPlayer2 = new Player("testId2", "testUsername2");
    private String createdLobbyId;

    @BeforeEach
    void setUp() {
        controller = new GameController();
        createdLobbyId = createLobby(testPlayer1, testPlayer2);
    }

    private String createLobby(Player p1, Player p2) {
        LobbyController controller = new LobbyController();

        Message message = new Message();
        message.setPlayer(p1);
        PlayerListResponse response = (PlayerListResponse) controller.connect(message);

        Message message1 = new Message();
        message1.setPlayer(p2);
        message1.setLobbyId(response.getLobbyId());
        PlayerListResponse response1 = (PlayerListResponse) controller.connect(message1);

        return response1.getLobbyId();
    }

    @Test
    void test_connect() {
        Message message = new Message();
        message.setLobbyId(createdLobbyId);
        message.setPlayer(testPlayer1);

        Response response = controller.connect(message);

        assertTrue(response instanceof GameStartResponse);
        GameStartResponse gsResponse = (GameStartResponse) response;

        assertEquals(Operation.START_PREP, gsResponse.getOperation());
        assertEquals(createdLobbyId, gsResponse.getLobbyId());
        assertEquals(40, gsResponse.getPawnList().size());
    }

    @Test
    void test_connectPlayerNull() {
        Message message = new Message();
        message.setLobbyId(createdLobbyId);

        Response response = controller.connect(message);

        assertNull(response);
    }

    @Test
    void test_connectLobbyIdNull() {
        Message message = new Message();
        message.setPlayer(testPlayer1);

        Response response = controller.connect(message);

        assertTrue(response instanceof ErrorResponse);
    }

    @Test
    void test_connectLobbyIdBlank() {
        Message message = new Message();
        message.setPlayer(testPlayer1);
        message.setLobbyId("");

        Response response = controller.connect(message);

        assertTrue(response instanceof ErrorResponse);
    }

}