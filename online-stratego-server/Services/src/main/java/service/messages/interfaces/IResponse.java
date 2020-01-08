package service.messages.interfaces;

import service.Operation;

public interface IResponse extends ILobbyMessage {
    void setOperation(Operation operation);

    Operation getOperation();

    void setReceiver(String receiver);

    String getReceiver();

    void setLobbyId(String lobbyId);

    String getLobbyId();

}
