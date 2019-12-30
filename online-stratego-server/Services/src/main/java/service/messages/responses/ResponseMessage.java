package service.messages.responses;

import service.Operation;
import service.messages.interfaces.IResponse;

public class ResponseMessage implements IResponse {
    private Operation operation;
    private String receiver;
    private String lobbyId;

    public ResponseMessage(Operation operation, String sender, String lobbyId) {
        this.operation = operation;
        this.receiver = sender;
        this.lobbyId = lobbyId;
    }

    public ResponseMessage() {
        this.operation = null;
        this.receiver = null;
        this.lobbyId = null;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

}
