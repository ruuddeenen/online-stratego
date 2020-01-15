package service.messages.outgoing.interfaces;

import service.messages.Operation;

public interface IResponse {
    Operation getOperation();

    void setOperation(Operation operation);

    String getReceiver();

    void setReceiver(String receiver);

    String getLobbyId();

    void setLobbyId(String lobbyId);
}
