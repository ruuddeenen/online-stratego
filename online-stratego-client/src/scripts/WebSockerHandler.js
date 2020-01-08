import SockJS from "sockjs-client";
import * as Stomp from "@stomp/stompjs";
import { GameConnectMessage } from '../models/MessageModels';

const stompClient = null

export class WebSocketHandler {
    constructor() {
        this.wsUrl = 'http://localhost:9000/ws';
    }

    connect(user, lobbyId) {
        console.log(user, lobbyId);
        stompClient = Stomp.Stomp.over(new SockJS(this.wsUrl));
        stompClient.connect({}, function () {
            stompClient.subscribe('/topic/game', (message) => {
                this.onMessageRecieved(message);
            });

            this.sendMessage('/app/game', new GameConnectMessage(
                user.id,
                user.username,
                lobbyId,
                user.color
            ));
        });
    }

    onMessageRecieved(msg) {
        const message = JSON.parse(msg.body);
        console.log(message, 'recieved');

        if (message.lobbyId === this.state.lobbyId) {
            if (message.receiver === this.state.user.id) {
                if (message.operation === 'START_GAME') {
                    console.log(
                        message.pawnList);
                    this.setState({
                        board: message.fields,
                        pawns: message.pawnList,
                        opponent: message.opponent
                    });
                }
            }
        }
    }

    sendMessage(endPoint, message) {
        this.stompClient.send(endPoint, {}, JSON.stringify(message));
        console.log(message, 'SEND');
    }

}