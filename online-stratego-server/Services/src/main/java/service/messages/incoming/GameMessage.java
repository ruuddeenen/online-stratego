package service.messages.incoming;

import models.Pawn.Pawn;
import service.messages.interfaces.IGameMessage;

import java.util.List;

public class GameMessage implements IGameMessage {
    private String id;
    private String lobbyId;
    private List<Pawn> pawnList;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getLobbyId() {
        return lobbyId;
    }

    @Override
    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    @Override
    public List<Pawn> getPawnList() {
        return pawnList;
    }

    @Override
    public void setPawnList(List<Pawn> pawnList) {
        this.pawnList = pawnList;
    }
}
