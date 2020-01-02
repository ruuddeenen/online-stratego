import React, { Component } from 'react';
import SockJS from "sockjs-client";
import * as Stomp from "@stomp/stompjs";
import { GameConnectMessage } from '../models/MessageModels';
import Grass from '../images/grass.png';
import Water from '../images/water.png';
import Bomb from '../images/pawns/stratego-bomb.webp';
import Captain from '../images/pawns/stratego-captain.webp';
import Colonel from '../images/pawns/stratego-colonel.webp';
import Flag from '../images/pawns/stratego-flag.webp';
import General from '../images/pawns/stratego-general.webp';
import Lieutenant from '../images/pawns/stratego-lieutenant.webp';
import Major from '../images/pawns/stratego-major.webp';
import Marshal from '../images/pawns/stratego-marshal.webp';
import Miner from '../images/pawns/stratego-miner.webp';
import Scout from '../images/pawns/stratego-scout.webp';
import Sergeant from '../images/pawns/stratego-sergeant.webp';
import Spy from '../images/pawns/stratego-spy.webp';
import Cross from '../images/cross.webp';
import { Button } from 'react-bootstrap';

const canvasDimensions = {
    height: {
        small: 320,
        medium: 640,
        big: 800
    },
    width: {
        small: 400,
        medium: 800,
        big: 1000
    }
};

let stompClient = null;

class Game extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            opponent: null,
            lobbyId: null,
            color: null,
            ready: false,
            board: [],
            pawns: [],
            selectedItem: null,
            images: [],
            pawnsLeftToPlace: []
        };

        // Bindings
        this.sendMessage = this.sendMessage.bind(this);
        this.onMessageRecieved = this.onMessageRecieved.bind(this);
    }

    componentDidMount() {
        const images = {
            pawns: {
                sergeant: createImage(Sergeant),
                bomb: createImage(Bomb),
                captain: createImage(Captain),
                colonel: createImage(Colonel),
                flag: createImage(Flag),
                general: createImage(General),
                lieutenant: createImage(Lieutenant),
                major: createImage(Major),
                marshal: createImage(Marshal),
                miner: createImage(Miner),
                scout: createImage(Scout),
                spy: createImage(Spy)
            },
            grass: createImage(Grass),
            water: createImage(Water),
            cross: createImage(Cross),
        };

        const pawnsLeftToPlace = {
            sergeant: 0,
            bomb: 0,
            captain: 0,
            colonel: 0,
            flag: 0,
            general: 0,
            lieutenant: 0,
            major: 0,
            marshal: 0,
            miner: 0,
            scout: 0,
            spy: 0
        }

        this.setState({
            images: images,
            pawnsLeftToPlace: pawnsLeftToPlace
        });

        function createImage(url) {
            let image = new Image();
            image.src = url;
            return image;
        }

        this.getSessionStorage();
        this.getUser();
        this.getLobbyId();
        this.connect();
        this.drawBoard();
        this.drawPawns();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.state.images) {
            if (this.state.board !== prevState.board) {
                this.drawBoard();
            }
            if (this.state.pawns !== prevState.pawns) {
                this.drawPawns();
            }
        }
        console.log(this.state, 'state update');
    }

    updatePawnsLeftToPlace() {
        const pawns = this.state.pawns;
        let pawnsLeftToPlace = {
            sergeant: this.getPawnsToPlace(pawns, this.getNameFromImport(Sergeant)),
            bomb: this.getPawnsToPlace(pawns, this.getNameFromImport(Bomb)),
            captain: this.getPawnsToPlace(pawns, this.getNameFromImport(Captain)),
            colonel: this.getPawnsToPlace(pawns, this.getNameFromImport(Colonel)),
            flag: this.getPawnsToPlace(pawns, this.getNameFromImport(Flag)),
            general: this.getPawnsToPlace(pawns, this.getNameFromImport(General)),
            lieutenant: this.getPawnsToPlace(pawns, this.getNameFromImport(Lieutenant)),
            major: this.getPawnsToPlace(pawns, this.getNameFromImport(Major)),
            marshal: this.getPawnsToPlace(pawns, this.getNameFromImport(Marshal)),
            miner: this.getPawnsToPlace(pawns, this.getNameFromImport(Miner)),
            scout: this.getPawnsToPlace(pawns, this.getNameFromImport(Scout)),
            spy: this.getPawnsToPlace(pawns, this.getNameFromImport(Spy))
        };

        this.setState({
            pawnsLeftToPlace: pawnsLeftToPlace
        });
    }

    getPawnsToPlace(pawnArray, name) {
        let count = 0;
        for (let i = 0; i < pawnArray.length; i++) {
            if (pawnArray[i].name === name) {
                if (pawnArray[i].position.x === -1 || pawnArray[i].position.y === -1) {
                    count++;
                }
            }
        }
        return count;
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
        stompClient.connect({}, function () {
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
        const canvas = document.getElementById('canvasBoard');
        const context = canvas.getContext('2d');
        const board = this.state.board;
        const size = board.length;
        const width = canvas.width, height = canvas.height;

        context.fillStyle = 'black';
        context.fillRect(0, 0, width, height);
        for (var y = 0; y < size; y++) {
            const field = board[y];
            for (var x = 0; x < size; x++) {
                let image;
                if (field[x] === true) {
                    image = this.state.images.grass;
                } else if (field[x] === false) {
                    image = this.state.images.water;
                }

                context.drawImage(
                    image,
                    (width / size) * x + 1,
                    (height / size) * y + 1,
                    width / size - 1,
                    height / size - 1
                );

                // Draw colored overlay
                if (y < 4) {
                    context.fillStyle = this.state.opponent.color;
                    context.globalAlpha = 0.4;
                    context.fillRect(
                        (width / size) * x,
                        (height / size) * y,
                        width / size,
                        height / size
                    )
                }
                else if (y > 5) {
                    context.fillStyle = this.state.color;
                    context.globalAlpha = 0.4;
                    context.fillRect(
                        (width / size) * x,
                        (height / size) * y,
                        width / size,
                        height / size
                    )
                }
                context.globalAlpha = 1.0;
            }
        }

        // Draw border
        context.beginPath();
        context.moveTo(0, 0);
        context.lineTo(0, height);
        context.lineTo(width, height);
        context.lineTo(width, 0);
        context.lineTo(0, 0);
        context.stroke();
    }

    drawPawns() {
        const canvas = document.getElementById('canvasPawns');
        const context = canvas.getContext('2d');
        const board = this.state.board;
        const size = board.length;
        const width = canvas.width, height = canvas.height;
        const pawns = this.state.pawns;

        context.clearRect(0, 0, width, height);

        for (let i = 0; i < pawns.length; i++) {
            const position = pawns[i].position;
            if (position.x !== -1 || position.y !== -1) {
                console.log('drawing',
                    this.getPawnFromArray(pawns[i].name, this.state.images.pawns),
                    (width / size) * position.x,
                    (height / size) * position.y,
                    width / size,
                    height / size);

                context.drawImage(
                    this.getPawnFromArray(pawns[i].name, this.state.images.pawns),
                    (width / size) * position.x + 5,
                    (height / size) * position.y + 5,
                    width / size - 10,
                    height / size - 10
                )
            }
        }
        this.updatePawnsLeftToPlace();
    }



    onMessageRecieved(msg) {
        const message = JSON.parse(msg.body);
        console.log(message, 'recieved');

        if (message.lobbyId === this.state.lobbyId) {
            if (message.receiver === this.state.user.id) {
                if (message.operation === 'START_GAME') {
                    this.setState({
                        board: message.fields,
                        pawns: message.pawnList,
                        opponent: message.opponent
                    });
                }
            }
        }
    }

    sendMessage = (endPoint, message) => {
        stompClient.send(endPoint, {}, JSON.stringify(message));
        console.log(message, 'SEND');
    }

    drawCrossAtPosition(x, y) {
        const canvas = document.getElementById('canvasCross');
        const context = canvas.getContext('2d');
        const width = canvas.width, height = canvas.height;
        const size = this.state.board.length;
        context.clearRect(0, 0, width, height);

        if (this.getPawnOnPosition(x, y) !== null) {
            context.drawImage(
                this.state.images.cross,
                (width / size) * x + 5,
                (height / size) * y + 5,
                width / size - 10,
                height / size - 10
            )
        }
    }

    getPawnOnPosition(x, y) {
        const pawns = this.state.pawns;
        for (let i = 0; i < pawns.length; i++) {
            if (pawns[i].position.x === x && pawns[i].position.y === y) {
                return pawns[i];
            }
        }
        return null;
    }

    handleMouseMove(e) {
        const x = Math.floor(e.nativeEvent.layerX / e.target.width * 10);
        const y = Math.floor(e.nativeEvent.layerY / e.target.height * 10);
        this.drawCrossAtPosition(x, y);
    }

    handleMouseClick(e) {
        const x = Math.floor(e.nativeEvent.layerX / e.target.width * 10);
        const y = Math.floor(e.nativeEvent.layerY / e.target.height * 10);

        // ONLY IN PREPARATION MODE
        if (y > 5) {
            let selectedPawn = this.getPawnOnPosition(x, y);
            if (selectedPawn === null) {
                this.placePawn(this.state.pawns, this.state.selectedItem, x, y);
            } else {
                this.removePawn(selectedPawn);
            }
            this.drawPawns();
        }

    }

    removePawn(pawn) {
        pawn.position.x = -1;
        pawn.position.y = -1;
        this.setState({
            selectedItem: ''
        });
    }

    placePawn(pawnList, selectedName, x, y) {
        for (let i = 0; i < pawnList.length; i++) {
            if (pawnList[i].name === selectedName) {
                if (pawnList[i].position.x === -1 || pawnList[i].position.y === -1) {
                    pawnList[i].position.x = x;
                    pawnList[i].position.y = y;
                    console.log(selectedName, 'placed at', 'x:', x, 'y:', y)
                    break;
                }
            }
        }
        this.setState({
            pawns: pawnList
        });
    }

    getBG(imageUrl) {
        return 'url(' + imageUrl + ')';
    }

    selectPawn(selectedItem) {
        console.log(selectedItem, 'selected');
        this.setState({
            selectedItem: selectedItem
        });
    }

    render() {
        return (
            <div className='Layout'>
                {this.getHeader()}
                <div className='row'>
                    <div id='left' className='col-md'>
                        {this.createPawnButtons([Flag, Spy, Scout, Miner, Sergeant, Lieutenant, Captain, Major, Colonel, General, Marshal, Bomb])}
                    </div>
                    <div className='center' className='col-md'>
                        <div className='grid'>
                            <div className='canvasWrapper'>
                                <canvas id='canvasBoard' width={canvasDimensions.width.medium} height={canvasDimensions.height.medium} onMouseDown={this.handleMouseClick.bind(this)} />
                            </div>
                            <div className='canvasWrapper'>
                                <canvas id='canvasPawns' width={canvasDimensions.width.medium} height={canvasDimensions.height.medium} onMouseDown={this.handleMouseClick.bind(this)} />
                            </div>
                            <div className='canvasWrapper'>
                                <canvas id='canvasCross' width={canvasDimensions.width.medium} height={canvasDimensions.height.medium} onMouseMove={this.handleMouseMove.bind(this)} onMouseDown={this.handleMouseClick.bind(this)} />
                            </div>
                        </div>
                    </div>
                    <div id='right' className='col-sm'>
                        <canvas id='canvasDefeatedPawns' width={canvasDimensions.width.small} height={canvasDimensions.height.medium}></canvas>
                    </div>
                </div>
            </div >
        )
    }

    getHeader() {
        if (this.state.opponent) {
            if (this.state.color === 'RED') {
                return (
                    <div className='row'>
                        <header className='Header red'>{this.state.user.username}</header>
                        <header className='Header'>&nbsp;-&nbsp;</header>
                        <header className='Header blue'>{this.state.opponent.username}</header>
                    </div>
                )
            } else {
                return (
                    <div className='row'>
                        <header className='Header blue'>{this.state.user.username}</header>
                        <header className='Header'>&nbsp;-&nbsp;</header>
                        <header className='Header red'>{this.state.opponent.username}</header>
                    </div>
                )
            }
        } else {
            return (
                <header className='Header'>
                    Waiting for player..
                </header>
            )
        }
    }

    createPawnButtons(pawns) {
        let rows = [];
        let i = 0;
        for (let row = 0; row < pawns.length / 2; row++) {
            rows.push(
                <div className='row' key={i}>
                    <div className='col-sm'>
                        <Button
                            className={this.state.selectedItem === this.getNameFromImport(pawns[i]) ? 'btn-warning pawn active' : 'btn-warning pawn'}
                            style={{ backgroundImage: this.getBG(pawns[i]) }}
                            title={this.getNameFromImport(pawns[i])}
                            onClick={e => this.selectPawn(e.target.title)}>
                            {this.getPawnFromArray(this.getNameFromImport(pawns[i++]), this.state.pawnsLeftToPlace)}
                        </Button>
                    </div>
                    <div className='col-sm'>
                        <Button
                            className={this.state.selectedItem === this.getNameFromImport(pawns[i]) ? 'btn-warning pawn active' : 'btn-warning pawn'}
                            style={{ backgroundImage: this.getBG(pawns[i]) }}
                            title={this.getNameFromImport(pawns[i])}
                            onClick={e => this.selectPawn(e.target.title)}>
                            {this.getPawnFromArray(this.getNameFromImport(pawns[i++]), this.state.pawnsLeftToPlace)}
                        </Button>
                    </div>
                </div>);
        }
        return rows;
    }

    getNameFromImport(item) {
        const start = item.indexOf('stratego-') + 9;
        const end = item.indexOf('.');
        item = item.slice(start, end);
        item = item.replace(item[0], item[0].toUpperCase());
        return item;
    }



    getPawnFromArray(name, pawnArray) {
        switch (name) {
            case 'Flag': return pawnArray.flag;
            case 'Spy': return pawnArray.spy;
            case 'Scout': return pawnArray.scout;
            case 'Miner': return pawnArray.miner;
            case 'Sergeant': return pawnArray.sergeant;
            case 'Marshal': return pawnArray.marshal;
            case 'Major': return pawnArray.major;
            case 'Lieutenant': return pawnArray.lieutenant;
            case 'General': return pawnArray.general;
            case 'Colonel': return pawnArray.colonel;
            case 'Captain': return pawnArray.captain;
            case 'Bomb': return pawnArray.bomb;
            default: return name + ' did not match any pawn.'
        }
    }
}

export default Game;
