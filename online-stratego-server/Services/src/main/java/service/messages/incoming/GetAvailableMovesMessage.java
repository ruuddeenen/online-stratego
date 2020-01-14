package service.messages.incoming;

import models.pawns.Pawn;
import service.messages.interfaces.IMoveMessage;

public class GetAvailableMovesMessage implements IMoveMessage {
    private String id;
    private String lobbyId;
    private Pawn pawn;

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
    public Pawn getPawn() {
        return pawn;
    }

    @Override
    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }
}
