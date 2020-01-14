package service.messages.responses;

public class ErrorResponse extends Response {
    private String message;

    public ErrorResponse(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
