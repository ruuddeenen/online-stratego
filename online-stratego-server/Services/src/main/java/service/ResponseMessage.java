package service;

public class ResponseMessage {
    private Operation operation;
    private String from;
    private String lobbyId;
    private String content;

    public ResponseMessage(Operation operation, String from, String lobbyId, String content){
        this.operation = operation;
        this.from = from;
        this.lobbyId = lobbyId;
        this.content = content;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
