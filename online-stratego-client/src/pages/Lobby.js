import React, { Component } from 'react';
import { Button } from 'react-bootstrap';
import { GameStartMessage, Message } from '../models/MessageModels';
import SockJS from "sockjs-client";
import * as Stomp from "@stomp/stompjs";

const wsUrl = 'http://localhost:9090/ws';
let stompClient = null;

class Lobby extends Component {

    constructor(props) {
        super(props);

        this.state = {
            user: null,
            lobbyId: null,
            playerList: [],
            buttonDisabled: true
        }

        // Bindings
        this.onMessageRecieved = this.onMessageRecieved.bind(this);
        this.sendMessage = this.sendMessage.bind(this);
    }

    componentDidMount() {
        this.getSessionStorage();
        this.connect();
    }

    componentDidUpdate(){
        console.log(this.state)
    }

    getSessionStorage() {
        this.setState({
            user: JSON.parse(sessionStorage.getItem('user')),
            lobbyId: sessionStorage.getItem('lobbyId')
        });
    }

    connect = () => {
        const that = this;
        stompClient = Stomp.Stomp.over(new SockJS(wsUrl));
        stompClient.connect({}, function (frame) {
            console.log(frame.toString());
            stompClient.subscribe('/topic/lobby', (msg) => {
                that.onMessageRecieved(msg);
            });
            that.sendMessage('/app/lobby', new Message(
                {
                    id: that.state.user.id,
                    username: that.state.user.username
                },
                that.state.lobbyId
            ));
        })
    }

    sendMessage = (endPoint, message) => {
        stompClient.send(endPoint, {}, JSON.stringify(message));
        console.log(message, 'SEND');
    }

    onMessageRecieved(msg) {
        const message = JSON.parse(msg.body);
        console.log(message, 'recieved');

        if (message.receiver === this.state.user.id) {
            if (message.operation === 'NEW_LOBBY') {
                this.setState({
                    lobbyId: message.lobbyId,
                    playerList: message.playerList
                });
                sessionStorage.setItem('lobbyId', this.state.lobbyId);
            }
        }

        if (message.lobbyId === this.state.lobbyId) {
            if (message.operation === 'JOINED_LOBBY') {
                this.setState({
                    playerList: message.playerList
                });

                this.state.playerList.forEach(player => {
                    if (player.id === this.state.user.id) {
                        sessionStorage.setItem('color', player.color);
                    }
                });
                if (this.state.playerList.size === 2) {
                    this.setState({
                        buttonDisabled: false
                    })
                }
            } else if (message.operation === 'OPEN_GAME') {
                window.location = '/game';
            }
        }
    }

    startGame = () => {
        if (this.state.playerList.length !== 2) {
            window.alert('2 Players are needed for a game!');
            return;
        }
        console.log('start game', this);
        this.sendMessage('/app/lobby/startgame', new GameStartMessage(
            {
                id: this.state.user.id,
                username: this.state.user.username
            },
            this.state.lobbyId,
            this.state.playerList
        ));
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
}
export default Lobby;