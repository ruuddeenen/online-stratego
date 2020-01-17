package service.messages.outgoing.models;

public class ErrorResponse extends Response {
    private String message;

    public ErrorResponse(String message, String receiver){
        this.message = message;
        setReceiver(receiver);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
