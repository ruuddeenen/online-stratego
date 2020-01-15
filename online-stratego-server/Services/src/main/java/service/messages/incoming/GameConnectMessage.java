package service.messages.incoming;

import models.enums.Color;

public class GameConnectMessage extends ConnectMessage {
    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
