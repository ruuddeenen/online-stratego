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

export class ReadyUpMessage {
    constructor(id, lobbyId, pawnList){
        this.id = id;
        this.lobbyId = lobbyId;
        this.pawnList = pawnList;
    }
}

export class GetAvailableMovesMessage {
    constructor(id, lobbyId, pawn){
        this.id = id;
        this.lobbyId = lobbyId;
        this.pawn = pawn;
    }
}

export class MoveMessage extends GetAvailableMovesMessage{
    constructor(id, lobbyId, pawn, position){
        super(id, lobbyId, pawn);
        this.position = position;
    }
}