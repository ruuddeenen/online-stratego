import React, { Component } from 'react';
import SockJS from "sockjs-client";
import * as Stomp from "@stomp/stompjs";

const height = 800, width = 1000;
let stompClient = null;

class Game extends Component {
    constructor(props) {
        super(props);
        this.state = {
            mouseX: 0,
            mouseY: 0,
            ready: false,
            board: [],
            pawns: []
        };

        // Bindings
        this.sendMessage = this.sendMessage.bind(this);
        this.handleMessage = this.handleMessage.bind(this);
        this.toggle = this.toggle.bind(this);
    }

    componentDidMount() {
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

    connect = () => {
        let _this = this;
        const socket = new SockJS('http://localhost:9000/ws');
        stompClient = Stomp.Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log(frame.toString());
            stompClient.subscribe('/topic/game', (message) => {
                _this.handleMessage(message);
            });
            _this.sendMessage('/app/game', { id: 0, username: 'ruudTest' });
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
                    context.fillRect((width / size) * x, (height / size) * y, width / size - 1, height / size - 1);
                }
            }

    }

    drawPawns() {
        console.log('draw pawns');
    }

    handleMessage(message) {
        const obj = JSON.parse(message.body);
        this.setState({
            board: obj.field
        });
        console.log(obj, 'OUTPUT');
    }

    sendMessage = (endPoint, message) => {
        stompClient.send(endPoint, {}, JSON.stringify(message));
        console.log(message, 'SEND');
    }

    toggle = () => {
        this.sendMessage('/app/game', { id: 0, username: 'ruudTest' });
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
                            <canvas id='canvasBoard' width={width} height={height} onMouseDown={this.handleMouseClick.bind(this)} />
                        </div>
                        <div className='canvasWrapper'>
                            <canvas id='canvasPawns' width={width} height={height} onMouseDown={this.handleMouseClick.bind(this)} />
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
