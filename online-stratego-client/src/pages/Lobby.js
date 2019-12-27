import React, { Component } from 'react';
import SockJS from "sockjs-client";
import * as Stomp from "@stomp/stompjs";
import { Button } from 'react-bootstrap';
import { ConnectMessage } from '../models/MessageModels';

let stompClient = null;

class Lobby extends Component {

    constructor(props) {
        super(props);

        this.state = {
            user: null,
            lobbyId: null,
            playerList: [],
            info1: {
                username: 'Player 1',
                won: '0',
                lost: '0'
            },
            info2: {
                username: 'Waiting for player..',
                won: '',
                lost: ''
            }
        }
        this.onMessageRecieved = this.onMessageRecieved.bind(this);
        this.sendMessage = this.sendMessage.bind(this);
    }

    componentDidMount() {
        this.getSessionStorage();
        this.connect();
    }

    getSessionStorage() {
        this.setState({
            user: JSON.parse(sessionStorage.getItem('user')),
            lobbyId: sessionStorage.getItem('lobbyId')
        });
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        console.log(this.state);

        if (this.state.user !== prevState.user) {
            const info = {
                username: this.state.user.username,
                won: 6,
                lost: 9
            };

            this.setState({
                info1: info
            });
        }

        if (this.state.playerList.length === 2 && this.state.playerList !== prevState.playerList) {
            const playerList = this.state.playerList;
            this.setState({
                info1: this.createInfoBlockFromPlayer(playerList[0]),
                info2: this.createInfoBlockFromPlayer(playerList[1])
            });
        }
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
            _this.sendMessage('/app/lobby', new ConnectMessage(
                _this.state.user.id,
                _this.state.user.username,
                _this.state.lobbyId
            ))
        });

    }

    onMessageRecieved(msg) {
        const message = JSON.parse(msg.body);
        console.log(message, 'recieved');

        console.log(this.state)
        if (message.operation === 'JOINED_LOBBY') {
            this.setState({
                lobbyId: message.lobbyId,
                playerList: message.playerList
            });

            sessionStorage.setItem('lobbyId', this.state.lobbyId);
            this.state.playerList.forEach(player => {
                if (player.id === this.state.user.id) {
                    sessionStorage.setItem('color', player.color);
                }
            });
        }
    }

    sendMessage(endPoint, message) {
        stompClient.send(endPoint, {}, JSON.stringify(message));
        console.log(message, 'SEND');
    }

    getPlayer(i) {
        const player = this.state.playerList[i];
        console.log(player, 'player ' + i);
    }

    startGame() {
        window.location = '/game';
    }


    createInfoBlockFromPlayer(i) {
        if (this.state.playerList[i]) {
            const player = this.state.playerList[i];
            let color;
            if (player.color === 'RED') {
                color = 'rgba(200, 0, 0, 0.85';
            } else if (player.color === 'BLUE') {
                color = 'rgba(0, 0, 200, 0.85';
            } else {
                color = 'rgba(235, 200, 70, 0.85';
            }
            return (
                <div className='Form player-info' style={{ backgroundColor: color }}>
                    <p className='player-name'>{player.username}</p>
                    <p className='player-info'>{player.color}</p>
                </div>
            )
        } else {
            return (
                <div className='Form player-info' style={{ backgroundColor: 'rgba(235, 200, 70, 0.85)' }}>
                    <p className='player-name'>Waiting for player..</p>
                </div>
            )
        }
    }

    render() {
        return (
            <div className='Layout'>
                <div className='row'>
                    <header className='Header'>Lobby:&nbsp;</header>
                    <header className='lobby-id'>{this.state.lobbyId && this.state.lobbyId}</header>
                </div>
                <div className='container' style={{ width: '60%' }}>
                    <div className='row'>
                        <div className='col-sm'>
                            {this.createInfoBlockFromPlayer(0)}
                        </div>
                        <div className='col-sm'>
                            {this.createInfoBlockFromPlayer(1)}
                        </div>
                    </div>
                    <div className='row' style={{ paddingTop: '1rem' }}>
                        <Button className='btn btn-warning' onClick={this.startGame}>
                            Start Game
                        </Button>
                    </div>
                </div>
            </div>
        )
    }
}
export default Lobby;