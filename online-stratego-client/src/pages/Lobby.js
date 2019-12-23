import React, { Component } from 'react';
import SockJS from "sockjs-client";
import * as Stomp from "@stomp/stompjs";

let stompClient = null;

class Lobby extends Component {

    constructor(props) {
        super(props);

        this.state = {
            lobbyId: null,
            playerList: []
        }
        this.onMessageRecieved = this.onMessageRecieved.bind(this);
        this.sendMessage = this.sendMessage.bind(this);
    }

    componentDidMount(){
        this.connect();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        console.log(this.state);
    }

    connect() {
        let _this = this;
        const socket = new SockJS('http://localhost:9000/ws');
        stompClient = Stomp.Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log(frame.toString());
            stompClient.subscribe('/topic/lobby', (message) => {
                _this.onMessageRecieved(message);
            });
            _this.sendMessage('/app/lobby', {
                id: Math.floor(Math.random()*100 +1),
                lobbyId: _this.state.lobbyId,
                username: 'ruudTest2'
            });
        });
    }

    onMessageRecieved(msg){
        const message  = JSON.parse(msg.body);
        console.log(message, 'recieved');

        if (message.operation === 'JOINED_LOBBY'){
            this.setState({
                lobbyId: message.lobbyId,
                playerList: JSON.parse(message.content)
            });
        }
    }

    sendMessage (endPoint, message){
        stompClient.send(endPoint, {}, JSON.stringify(message));
        console.log(message, 'SEND');
    }

    render() {
        return (
            < div className='Layout' >
            </div >

        )
    }
}
export default Lobby;