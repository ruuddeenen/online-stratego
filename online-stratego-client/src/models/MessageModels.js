export class Message {
    constructor(player, lobbyId) {
        this.player = player;
        this.lobbyId = lobbyId;
    };
}

export class GameStartMessage extends Message {
    constructor(player, lobbyId, playerList) {
        super(player, lobbyId);
        this.playerList = playerList;
    };
}

export class GameMessage extends Message {
    constructor(player, lobbyId, pawnList) {
        super(player, lobbyId);
        this.pawnList = pawnList;
    }
}

export class AvailableMovesMessage extends Message {
    constructor(player, lobbyId, pawn) {
        super(player, lobbyId);
        this.pawn = pawn;
    }
}

export class MoveMessage extends AvailableMovesMessage {
    constructor(player, lobbyId, pawn, position) {
        super(player, lobbyId, pawn);
        this.position = position;
    }
}