package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameRepositoryTest {
    private GameRepository repo;
    private Player player;

    @BeforeEach
    void setUp() {
        repo = GameRepository.getInstance();
        player = new Player("id", "username");
    }

    @Test
    void test_createNewGame(){
        assertNull(repo.getGameById("test1,m"));
        assertNotNull(repo.getOrCreateGame("test1"));
        assertNotNull(repo.getGameById("test1"));
    }

    @Test
    void test_createLobby(){
        assertEquals(5, repo.createLobby().length());
    }

    @Test
    void test_addPlayerToLobby(){
        String lobbyId = repo.createLobby();
        repo.addPlayerToLobby(lobbyId, player);
        assertEquals(1, repo.getPlayersFromLobby(lobbyId).size());
    }

    @Test
    void test_isLobbyFull(){
        Player p2 = new Player("id2", "username2");
        String lobbyId = repo.createLobby();

        repo.addPlayerToLobby(lobbyId, player);
        assertFalse(repo.isLobbyFull(lobbyId));

        repo.addPlayerToLobby(lobbyId, p2);
        assertTrue(repo.isLobbyFull(lobbyId));
    }

    @Test
    void test_addThreePlayersToLobby(){
        Player p2 = new Player("id2", "username2");
        Player p3 = new Player("id3", "username3");
        String lobbyId = repo.createLobby();
        repo.addPlayerToLobby(lobbyId, player);
        repo.addPlayerToLobby(lobbyId, p2);
        repo.addPlayerToLobby(lobbyId, p3);

        assertEquals(2, repo.getPlayersFromLobby(lobbyId).size());
        assertTrue(repo.getPlayersFromLobby(lobbyId).contains(player));
        assertTrue(repo.getPlayersFromLobby(lobbyId).contains(p2));
        assertFalse(repo.getPlayersFromLobby(lobbyId).contains(p3));
    }

    @Test
    void test_addPlayer(){
        repo.addPlayer(player);
        assertEquals(player, repo.getPlayerById(player.getId()));
    }
}