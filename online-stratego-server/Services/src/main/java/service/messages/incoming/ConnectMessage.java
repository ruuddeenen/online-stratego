package service.messages.incoming;

import service.messages.interfaces.IConnectMessage;

public class ConnectMessage extends BaseMessage implements IConnectMessage {
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
