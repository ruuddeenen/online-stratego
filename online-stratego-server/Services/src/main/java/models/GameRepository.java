package models;


import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

public class GameRepository {
    private static final GameRepository instance = new GameRepository();
    private Map<String, Game> gameMap;
    private Map<String, List<Player>> lobbyPlayerMap = new HashMap<>();
    private Set<Player> playerSet;

    private GameRepository() {
        gameMap = new HashMap<>();
        playerSet = new HashSet<>();
    }

    public static GameRepository getInstance() {
        return instance;
    }

    public Game getGameById(String id) {
        return gameMap.getOrDefault(id, null);
    }

    public Game createNewGame(String id) {
        gameMap.put(id, new Game());
        return gameMap.get(id);
    }

    public Game getOrCreateGame(String id) {
        Game game = getGameById(id);
        if (game == null) {
            return createNewGame(id);
        }
        return game;
    }

    public List<Player> getPlayersFromLobby(String id){
        return lobbyPlayerMap.get(id);
    }

    public String createLobby(){
        String lobbyId = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        lobbyPlayerMap.put(lobbyId, new ArrayList<>());
        return lobbyId;
    }

    public void addPlayerToLobby(String lobbyId, Player player){
        List<Player> playerList = getPlayersFromLobby(lobbyId);
        if (playerList == null){
            playerList = new ArrayList<>();
        }
        playerList.add(player);
        lobbyPlayerMap.put(lobbyId, playerList);

        if (isLobbyFull(lobbyId)){
            Collections.shuffle(playerList);
            playerList.get(0).setColor(models.enums.Color.RED);
            playerList.get(1).setColor(models.enums.Color.BLUE);
        }
    }

    public Player getPlayerById(String id) {
        for (Player p : playerSet
        ) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Player addPlayer(String id, String username){
        playerSet.add(new Player(id, username));
        return getPlayerById(id);
    }

    public boolean isLobbyFull(String id){
        return getPlayersFromLobby(id).size() >= 2;
    }


}