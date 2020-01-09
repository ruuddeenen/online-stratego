import React, { Component } from 'react';
import SockJS from "sockjs-client";
import * as Stomp from "@stomp/stompjs";
import { GameConnectMessage, ReadyUpMessage, GetAvailableMovesMessage, MoveMessage } from '../models/MessageModels';
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
import { Button } from 'react-bootstrap';
import { CanvasHandler } from '../scripts/CanvasHandler';

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
let canvasHandler = null;

class Game extends Component {
    constructor(props) {
        super(props);
        this.state = {
            preperationMode: true,
            user: null,
            opponent: null,
            lobbyId: null,
            color: null,
            ready: false,
            board: [],
            pawns: [],
            selectedItem: null,
            selectedPawn: null,
            pawnsLeftToPlace: [],
            allPawnsPlaced: false,
            pawnInfo: []
        };

        // Bindings
        this.sendMessage = this.sendMessage.bind(this);
        this.onMessageRecieved = this.onMessageRecieved.bind(this);
    }

    componentDidMount() {
        canvasHandler = new CanvasHandler(
            document.getElementById('canvasBoard'),
            document.getElementById('canvasPawns'),
            document.getElementById('canvasOverlay')
        );

        this.getSessionStorage();
        this.connect();

        canvasHandler.drawPawns(this.state.pawns, this.state.color);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.state.board !== prevState.board) {
            canvasHandler.setBoard(this.state.board);
            canvasHandler.drawStrategoBoard(true);
        }
        if (this.state.pawns !== prevState.pawns) {
            this.setState({
                pawnsLeftToPlace: this.getPawnsLeftToPlace(this.state.pawns)
            });
            canvasHandler.drawPawns(this.state.pawns, this.state.color);
        }
        console.log(this.state, 'state update');
    }

    getPawnCountLeftToPlace(pawnArray, name) {
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
            ));
        });
    }

    onMessageRecieved(msg) {
        const message = JSON.parse(msg.body);
        console.log(message, 'recieved');

        if (message.lobbyId === this.state.lobbyId) {
            if (message.receiver === this.state.user.id) {
                console.log(message, 'read');
                // If message is for this client
                switch (message.operation) {
                    case 'START_PREP':
                        const pawnInfo = [];
                        for (let l = 0; l < 12; l++) {
                            const pawn = this.getPawnByRank(l, message.pawnList);
                            const info = {
                                name: pawn.name,
                                rank: pawn.rank,
                                max: this.getPawnCountLeftToPlace(message.pawnList, pawn.name)
                            };
                            pawnInfo.push(info);
                        }

                        this.setState({
                            board: message.fields,
                            pawns: message.pawnList,
                            opponent: message.opponent,
                            pawnInfo: pawnInfo
                        });
                        break;
                    case 'START_GAME':
                        this.setState({
                            preperationMode: false,
                            pawns: message.pawnList
                        });
                        sessionStorage.setItem('pawns', this.state.pawns);
                        window.alert('Game started!');
                        canvasHandler.drawStrategoBoard(false);
                        break;
                    case 'POSSIBLE_MOVES':
                        canvasHandler.clearOverlay();
                        canvasHandler.drawOverlay(this.state.selectedPawn.position.x, this.state.selectedPawn.position.y, 'rgba(255,255,255,0.3)');
                        canvasHandler.drawPossibleMoves(message.moves, 'yellow');
                        canvasHandler.drawPossibleMoves(message.attacks, 'red');
                        break;
                    case 'MOVE_PAWN':
                        this.setState({
                            pawns: message.pawnList,
                            defeatedPawnList: message.defeatedPawnList
                        });
                        canvasHandler.drawPawns(this.state.pawns, this.state.color);
                        canvasHandler.clearOverlay();
                        sessionStorage.setItem('pawns', this.state.pawns);
                        break;
                    default:
                        break;

                }
            }
        }
    }

    sendMessage = (endPoint, message) => {
        stompClient.send(endPoint, {}, JSON.stringify(message));
        console.log(message, 'SEND');
    }

    handleReadyUp = () => {
        this.sendMessage('/app/game/ready', new ReadyUpMessage(
            this.state.user.id,
            this.state.lobbyId,
            this.state.pawns
        ));
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
        if (this.state.preperationMode) {
            if (this.getPawnOnPosition(x, y) !== null && this.state.selectedItem === '') {
                canvasHandler.drawCrossAtPosition(x, y);
            } else {
                canvasHandler.clearOverlay();
            }
        }
    }

    handleMouseClick(e) {
        const x = Math.floor(e.nativeEvent.layerX / e.target.width * 10);
        const y = Math.floor(e.nativeEvent.layerY / e.target.height * 10);

        if (this.state.preperationMode) {
            if (this.state.color === 'RED' && y > 5) { this.placeOrRemovePawn(x, y); }
            else if (this.state.color === 'BLUE' && y < 4) { this.placeOrRemovePawn(x, y); }
        } else {
            console.log(this.getPawnOnPosition(x, y));

            const pawn = this.getPawnOnPosition(x, y);
            if (pawn === null || pawn.color === this.state.opponent.color) {
                this.movePawn(x, y);
            } else if (pawn.color === this.state.color) {
                this.setState({
                    selectedPawn: pawn
                });
                this.getPawnMoves(pawn);
            } else if (this.state.selectedPawn === null) {
                return;
            }
            /*
            if (this.getPawnOnPosition(x, y) === null) {
                console.log(this.state.selectedItem)
                if (this.state.selectedPawn !== null && this.state.selectedPawn !== '') {
                    this.sendMessage('/app/game/move', new MoveMessage(
                        this.state.user.id,
                        this.state.lobbyId,
                        this.state.selectedPawn,
                        { x: x, y: y }
                    ));
                }

            } else {
                const pawn = this.getPawnOnPosition(x, y);
                if (pawn.color === this.state.color) {
                    this.setState({
                        selectedPawn: pawn
                    })
                    this.getPawnMoves(pawn);
                } else {
                    if (this.state.selectedPawn !== null && this.state.selectedPawn !== '') {
                        this.sendMessage('/app/game/move', new MoveMessage(
                            this.state.user.id,
                            this.state.lobbyId,
                            this.state.selectedPawn,
                            { x: x, y: y }
                        ));
                    }
                }
            }
            */
        }
    }

    movePawn(x, y) {
        this.sendMessage('/app/game/move', new MoveMessage(
            this.state.user.id,
            this.state.lobbyId,
            this.state.selectedPawn,
            { x: x, y: y }
        ));
    }



    getPawnMoves(pawn) {
        this.sendMessage('/app/game/moves', new GetAvailableMovesMessage(
            this.state.user.id,
            this.state.lobbyId,
            pawn
        ));
    }

    placeOrRemovePawn(x, y) {
        let selectedPawn = this.getPawnOnPosition(x, y);
        if (selectedPawn === null) {
            this.placePawn(this.state.pawns, this.state.selectedItem, x, y);
        } else {
            this.removePawn(selectedPawn);
        }
        canvasHandler.drawPawns(this.state.pawns, this.state.color);

        this.setState({
            pawnsLeftToPlace: this.getPawnsLeftToPlace(this.state.pawns)
        });
    }

    handleMouseOut() {
        this.selectPawn('');
        canvasHandler.clearOverlay();
    }

    handleClearAll = () => {
        const pawnList = this.state.pawns;
        for (let l = 0; l < pawnList.length; l++) {
            this.removePawn(pawnList[l]);
        }
        this.setState({
            pawns: pawnList
        });
        canvasHandler.drawPawns(this.state.pawns, this.state.color);
        this.setState({
            pawnsLeftToPlace: this.getPawnsLeftToPlace(this.state.pawns)
        });
        canvasHandler.clearOverlay();
    }

    handleRandomSetUp = () => {
        const pawnList = this.state.pawns;
        let i = 0;
        if (this.state.color === 'RED') {
            for (let x = 0; x < this.state.board.length; x++) {
                for (let y = 6; y < this.state.board.length; y++) {
                    this.placePawn(pawnList, pawnList[i++].name, x, y);
                }
            }
        } else if (this.state.color === 'BLUE') {
            for (let x = 0; x < this.state.board.length; x++) {
                for (let y = 3; y >= 0; y--) {
                    this.placePawn(pawnList, pawnList[i++].name, x, y);
                }
            }

        }
        canvasHandler.drawPawns(this.state.pawns, this.state.color);
        this.setState({
            pawnsLeftToPlace: this.getPawnsLeftToPlace(this.state.pawns)
        });
    }

    removePawn(pawn) {
        pawn.position.x = -1;
        pawn.position.y = -1;
        this.selectPawn('')
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

    getBG(name) {
        switch (name) {
            case 'Flag': return url(Flag);
            case 'Spy': return url(Spy);
            case 'Scout': return url(Scout);
            case 'Miner': return url(Miner);
            case 'Sergeant': return url(Sergeant);
            case 'Lieutenant': return url(Lieutenant);
            case 'Captain': return url(Captain);
            case 'Major': return url(Major);
            case 'Colonel': return url(Colonel);
            case 'General': return url(General);
            case 'Marshal': return url(Marshal);
            case 'Bomb': return url(Bomb);
            default: return null;
        }

        function url(imageUrl) {
            return 'url(' + imageUrl + ')';
        }
    }

    selectPawn(selectedItem) {
        this.setState({
            selectedItem: selectedItem
        });
    }

    getPawnByRank(rank, array) {
        for (let l = 0; l < array.length; l++) {
            if (array[l].rank === rank) {
                return array[l];
            }
        }
    }

    getPawnsLeftToPlace(pawnArray) {
        const arr = [
            this.getPawnCountLeftToPlace(pawnArray, 'Flag'),
            this.getPawnCountLeftToPlace(pawnArray, 'Spy'),
            this.getPawnCountLeftToPlace(pawnArray, 'Scout'),
            this.getPawnCountLeftToPlace(pawnArray, 'Miner'),
            this.getPawnCountLeftToPlace(pawnArray, 'Sergeant'),
            this.getPawnCountLeftToPlace(pawnArray, 'Lieutenant'),
            this.getPawnCountLeftToPlace(pawnArray, 'Captain'),
            this.getPawnCountLeftToPlace(pawnArray, 'Major'),
            this.getPawnCountLeftToPlace(pawnArray, 'Colonel'),
            this.getPawnCountLeftToPlace(pawnArray, 'General'),
            this.getPawnCountLeftToPlace(pawnArray, 'Marshal'),
            this.getPawnCountLeftToPlace(pawnArray, 'Bomb')
        ];

        for (let l = 0; l < arr.length; l++) {
            if (arr[l] === 0) {
                continue;
            } else {
                return arr;
            }
        }
        this.setState({
            allPawnsPlaced: true
        });
        return arr;
    }

    // RENDER FUNCTIONS

    render() {
        return (
            <div className='Layout'>
                {this.getHeader()}
                <div className='row'>
                    <div id='left' className='col-md'>
                        {this.createButtons(this.state.pawnInfo)}
                        <Button
                            hidden={!this.state.preperationMode}
                            className='btn-warning'
                            onClick={this.handleReadyUp}
                            disabled={!this.state.allPawnsPlaced}>
                            Ready up!
                        </Button>
                    </div>
                    <div className='center col-md'>
                        <div className='grid'>
                            <div className='canvasWrapper'>
                                <canvas
                                    id='canvasBoard'
                                    width={canvasDimensions.width.medium}
                                    height={canvasDimensions.height.medium}
                                    onMouseDown={this.handleMouseClick.bind(this)} />
                            </div>
                            <div className='canvasWrapper'>
                                <canvas
                                    id='canvasPawns'
                                    width={canvasDimensions.width.medium}
                                    height={canvasDimensions.height.medium}
                                    onMouseDown={this.handleMouseClick.bind(this)} />
                            </div>
                            <div className='canvasWrapper'>
                                <canvas
                                    id='canvasOverlay'
                                    width={canvasDimensions.width.medium}
                                    height={canvasDimensions.height.medium}
                                    onMouseMove={this.handleMouseMove.bind(this)}
                                    onMouseDown={this.handleMouseClick.bind(this)}
                                    onMouseOut={this.handleMouseOut.bind(this)} />
                            </div>
                            <div className='row'>
                                <div className='col sm-6'>
                                    <Button
                                        hidden={!this.state.preperationMode}
                                        className='btn-info'
                                        onClick={this.handleRandomSetUp}>
                                        Randomize set-up
                                    </Button>
                                </div>
                                <div className='col sm-6'>
                                    <Button
                                        hidden={!this.state.preperationMode}
                                        className='btn-danger'
                                        onClick={this.handleClearAll}>
                                        Clear all
                                     </Button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id='right' className='col-sm'>
                        <canvas id='canvasDefeatedPawns'
                            width={canvasDimensions.width.small}
                            height={canvasDimensions.height.medium}>
                        </canvas>
                    </div>
                </div>
            </div >
        )
    }

    getHeader() {
        if (this.state.opponent) {
            return (
                <div className='row'>
                    <header className={this.state.color + ' Header'}>{this.state.user.username}</header>
                    <header className='Header'>&nbsp;-&nbsp;</header>
                    <header className={this.state.opponent.color + ' Header'}>{this.state.opponent.username}</header>
                </div>
            )
        } else {
            return (
                <header className='Header'>
                    Waiting for player..
                </header>
            )
        }
    }

    getDefeatedPawnsByRank(rank){
        const defeatedPawns = this.state.defeatedPawnList;
        let count = 0;
        defeatedPawns.forEach(p => {
            if (p.rank === rank){
                count++;
            }
        });
        return count;
    }

    createButtons = (pawnArray) => {
        if (pawnArray.length === 0) {
            return;
        }

        let rows = [];
        let rank = 0;
        for (let row = 0; row < 12 / 2; row++) {
            let buttons = [];
            for (let i = 0; i < 2; i++) {
                const name = pawnArray[rank].name;
                buttons.push(
                    <div className='col-sm' key={i}>
                        <Button
                            className={this.state.selectedItem === name ? 'btn-warning pawn focus' : 'btn-warning pawn'}
                            style={{ backgroundImage: this.getBG(name) }}
                            title={name}
                            onClick={e => this.selectPawn(e.target.title)}>
                            { this.state.preperationMode && this.state.pawnsLeftToPlace[rank] }
                            { !this.state.preperationMode && this.state.defeatedPawnList &&  this.getDefeatedPawnsByRank(rank)}
                            { !this.state.preperationMode && '/'}
                            { !this.state.preperationMode && this.state.pawnInfo[rank].max}
                        </Button>
                    </div>
                );
                rank++;
            }
            rows.push(<div className='row' key={row}>{buttons}</div>)
        }
        return rows;
    }
}

export default Game;