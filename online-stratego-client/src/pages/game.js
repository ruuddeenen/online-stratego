import React, { Component } from 'react';

const height = 800, width = 1000;


class Game extends Component {
    constructor(props) {
        super(props);
        this.state = {
            board: [
                ['OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN'],
                ['OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN'],
                ['OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN'],
                ['OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN'],
                ['OPEN', 'OPEN', 'LAKE', 'LAKE', 'OPEN', 'OPEN', 'LAKE', 'LAKE', 'OPEN', 'OPEN'],
                ['OPEN', 'OPEN', 'LAKE', 'LAKE', 'OPEN', 'OPEN', 'LAKE', 'LAKE', 'OPEN', 'OPEN'],
                ['OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN'],
                ['OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN'],
                ['OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN'],
                ['OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN', 'OPEN'],
            ]
        }
    }

    componentDidMount() {
        this.drawBoard();
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
                if (field[x] === 'OPEN') {
                    context.fillStyle = 'green';
                    context.fillRect((width / size) * x, (height / size) * y, width / size - 1, height / size - 1);
                } else if (field[x] === 'LAKE'){
                    context.fillStyle = 'blue';
                    context.fillRect((width / size) * x, (height / size) * y, width / size - 1, height / size - 1);
                }
            }
        }
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
                            <canvas id='canvasBoard' width={width} height={height}/>
                        </div>
                        <div className='canvasWrapper'>
                            <canvas id='canvasPawns' width={width} height={height} />
                        </div>
                    </div>
                </div>
                <div className='row' id='right'>

                </div>
            </div >
        )
    }
}

export default Game;