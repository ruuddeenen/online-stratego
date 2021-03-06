package service.messages.outgoing.models;

import service.messages.Operation;

import service.messages.outgoing.interfaces.IResponse;

public abstract class Response implements IResponse {
    private Operation operation;
    private String receiver;
    private String lobbyId;

    Response(Operation operation, String receiver, String lobbyId) {
        this.operation = operation;
        this.receiver = receiver;
        this.lobbyId = lobbyId;
    }

    Response() {
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
