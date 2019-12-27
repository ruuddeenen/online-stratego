import React, { Component } from 'react';
import SockJS from "sockjs-client";
import * as Stomp from "@stomp/stompjs";
import { GameConnectMessage } from '../models/MessageModels';

const canvasDimensions = {
    height: 800,
    width: 1000
};

let stompClient = null;

class Game extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            lobbyId: null,
            color: null,
            mouseX: 0,
            mouseY: 0,
            ready: false,
            board: [],
            pawns: []
        };

        // Bindings
        this.sendMessage = this.sendMessage.bind(this);
        this.onMessageRecieved = this.onMessageRecieved.bind(this);
        this.toggle = this.toggle.bind(this);
    }

    componentDidMount() {
        this.getSessionStorage();
        this.getUser();
        this.getLobbyId();
        this.connect();
        this.drawBoard();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.state.board !== prevState.board) {
            this.drawBoard();
        }
        if (this.state.pawns !== prevState.pawns) {
            this.drawPawns();
        }
        console.log(this.state);
    }

    getSessionStorage() {
        this.setState({
            user: JSON.parse(sessionStorage.getItem('user')),
            lobbyId: sessionStorage.getItem('lobbyId'),
            color: sessionStorage.getItem('color')
        });
    }
    getUser() {
        this.setState({
            user: JSON.parse(sessionStorage.getItem('user'))
        });
    }

    getLobbyId() {
        this.setState({
            lobbyId: sessionStorage.getItem('lobbyId')
        });
    }

    connect = () => {
        const _this = this;
        const socket = new SockJS('http://localhost:9000/ws');
        stompClient = Stomp.Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log(frame.toString());
            stompClient.subscribe('/topic/game', (message) => {
                _this.onMessageRecieved(message);
            });
            _this.sendMessage('/app/game', new GameConnectMessage(
                _this.state.user.id,
                _this.state.user.username,
                _this.state.lobbyId,
                _this.state.color
            ))
        });
    }


    drawBoard() {
        console.log('drawing board');
        const canvas = document.getElementById('canvasBoard');
        const context = canvas.getContext('2d');

        const board = this.state.board;
        const size = board.length;
        for (var y = 0; y < size; y++) {
            const field = board[y];
            for (var x = 0; x < size; x++) {
                if (field[x] === true) {
                    context.fillStyle = 'green';
                } else if (field[x] === false) {
                    context.fillStyle = 'blue';
                }
                context.fillRect(
                    (canvasDimensions.width / size) * x,
                    (canvasDimensions.height / size) * y,
                    canvasDimensions.width / size - 1,
                    canvasDimensions.height / size - 1
                );
            }
        }

    }

    drawPawns() {
        console.log('draw pawns');
    }



    onMessageRecieved(msg) {
        const message = JSON.parse(msg.body);
        console.log(message, 'recieved');

        if (message.operation === 'START_GAME') {
            if (message.lobbyId === this.state.lobbyId && message.receiver === this.state.user.id){
                this.setState({
                    board: message.fields,
                    pawns: message.pawnList
                });
            }
        }
    }

    sendMessage = (endPoint, message) => {
        stompClient.send(endPoint, {}, JSON.stringify(message));
        console.log(message, 'SEND');
    }

    toggle = () => {
        this.sendMessage('/app/game', {
            id: 0,
            username: 'ruudTest'
        });
    }

    handleMouseClick(e) {
        let x = e.nativeEvent.layerX;
        let y = e.nativeEvent.layerY;
        x = Math.floor(x / 100);
        y = Math.floor(y / 80);

        this.setState({
            mouseX: x,
            mouseY: y
        });
    }

    render() {
        return (
            <div className='Layout'>
                <header style={{ paddingBottom: '3rem', fontSize: '40px' }}>
                    Online Stratego
                </header>

                <div className='row' id='left'>

                </div>
                <div className='row' id='center'>
                    <div className='grid'>
                        <div className='canvasWrapper'>
                            <canvas id='canvasBoard' width={canvasDimensions.width} height={canvasDimensions.height} onMouseDown={this.handleMouseClick.bind(this)} />
                        </div>
                        <div className='canvasWrapper'>
                            <canvas id='canvasPawns' width={canvasDimensions.width} height={canvasDimensions.height} onMouseDown={this.handleMouseClick.bind(this)} />
                        </div>
                    </div>
                </div>
                <div className='row' id='right'>
                </div>


                <button onClick={this.toggle}>
                    toggle
                </button>
            </div >
        )
    }
}

export default Game;
