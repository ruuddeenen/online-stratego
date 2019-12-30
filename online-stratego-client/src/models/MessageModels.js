export class ConnectMessage {
    constructor(id, username, lobbyId){
        this.id = id;
        this.username = username;
        this.lobbyId = lobbyId;
    }
}

export class GameConnectMessage {
    constructor(id, username, lobbyId, color){
        this.id = id;
        this.username = username;
        this.lobbyId = lobbyId;
        this.color = color;
    };
}

export class GameStartMessage {
    constructor(lobbyId, playerList){
        this.lobbyId = lobbyId;
        this.playerList = playerList;
    };
}