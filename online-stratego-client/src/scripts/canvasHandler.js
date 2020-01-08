import Grass from '../images/grass.png';
import Water from '../images/water.png'
import Cross from '../images/cross.webp';
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
import UnkownPawn from '../images/stratego-S.webp';


export class CanvasHandler {
    constructor(canvasBoard, canvasPawns, canvasOverlay) {
        this.ctxBoard = canvasBoard.getContext('2d');
        this.ctxPawns = canvasPawns.getContext('2d');
        this.ctxOverlay = canvasOverlay.getContext('2d');
        this.width = canvasBoard.width;
        this.height = canvasBoard.height;

        this.images = {
            pawnsOnRank: [
                createImage(Flag),
                createImage(Spy),
                createImage(Scout),
                createImage(Miner),
                createImage(Sergeant),
                createImage(Lieutenant),
                createImage(Captain),
                createImage(Major),
                createImage(Colonel),
                createImage(General),
                createImage(Marshal),
                createImage(Bomb)
            ],
            unkownPawn: createImage(UnkownPawn),
            grass: createImage(Grass),
            water: createImage(Water),
            cross: createImage(Cross),
        };
        console.log(this.images, 'images')

        function createImage(url) {
            let image = new Image();
            image.src = url;
            return image;
        }
    }

    setBoard(board) {
        this.board = board;
        this.size = board.length;
    }

    drawStrategoBoard(playerColor, opponentColor, overlay) {
        this.ctxBoard.fillRect(0, 0, this.width, this.height);

        for (var y = 0; y < this.size; y++) {
            const field = this.board[y];
            for (var x = 0; x < this.size; x++) {
                let image = this.images.grass;
                if (field[x] === false) {
                    image = this.images.water;
                }

                this.ctxBoard.drawImage(
                    image,
                    (this.width / this.size) * x + 1,
                    (this.height / this.size) * y + 1,
                    this.width / this.size - 1,
                    this.height / this.size - 1
                );

                if (overlay) {
                    if (y < 4) {
                        this.drawOverlay(x, y, opponentColor);
                    }
                    else if (y > 5) {
                        this.drawOverlay(x, y, playerColor);
                    }
                }
            }
        }
        this.drawBorder();
    }

    drawPawns(pawnArray) {
        this.clear(this.ctxPawns);

        for (let i = 0; i < pawnArray.length; i++) {
            const position = pawnArray[i].position;
            if (position.x !== -1 || position.y !== -1) {
                let image = this.images.pawnsOnRank[pawnArray[i].rank];

                if (pawnArray[i].rank === -1) {
                    image = this.images.unkownPawn;
                }

                this.ctxPawns.fillStyle = pawnArray[i].color;
                this.ctxPawns.globalAlpha = 0.4;
                this.ctxPawns.fillRect(
                    (this.width / this.size) * position.x + 1,
                    (this.height / this.size) * position.y + 1,
                    this.width / this.size - 1,
                    this.height / this.size - 1
                )
                this.ctxPawns.globalAlpha = 1;

                this.ctxPawns.drawImage(
                    image,
                    (this.width / this.size) * position.x + 5,
                    (this.height / this.size) * position.y + 5,
                    this.width / this.size - 10,
                    this.height / this.size - 10
                )
            }
        }
    }


    drawBorder() {
        this.ctxBoard.beginPath();
        this.ctxBoard.moveTo(0, 0);
        this.ctxBoard.lineTo(0, this.height);
        this.ctxBoard.lineTo(this.width, this.height);
        this.ctxBoard.lineTo(this.width, 0);
        this.ctxBoard.lineTo(0, 0);
        this.ctxBoard.stroke();
    }

    drawOverlay(x, y, color) {
        this.ctxBoard.fillStyle = color;
        this.ctxBoard.globalAlpha = 0.4;
        this.ctxBoard.fillRect(
            (this.width / this.size) * x,
            (this.height / this.size) * y,
            this.width / this.size,
            this.height / this.size
        )
        this.ctxBoard.globalAlpha = 1.0;
        this.ctxBoard.fillStyle = 'black';
    }

    drawCrossAtPosition(x, y) {
        this.clearOverlay();
        this.ctxOverlay.drawImage(
            this.images.cross,
            (this.width / this.size) * x + 5,
            (this.height / this.size) * y + 5,
            this.width / this.size - 10,
            this.height / this.size - 10
        );
    }

    drawDot(x, y) {
        const startX = (((this.width / this.size) * x) + ((this.width / this.size) * (x + 1))) / 2;
        const startY = (((this.height / this.size) * y) + ((this.height / this.size) * (y + 1))) / 2;
        this.ctxOverlay.beginPath();
        this.ctxOverlay.arc(
            startX,
            startY,
            this.width / this.size / 10,
            0,
            2 * Math.PI,
            false
        );
        this.ctxOverlay.fillStyle = 'yellow';
        this.ctxOverlay.fill();
        this.ctxOverlay.lineWidth = 2;
        this.ctxOverlay.strokeStyle = 'black';
        this.ctxOverlay.stroke();
    }

    clearOverlay() {
        this.clear(this.ctxOverlay);
    }

    clear(context) {
        context.clearRect(0, 0, this.width, this.height);
    }

    drawPossibleMoves(positions) {
        this.clearOverlay();
        for (let l = 0; l < positions.length; l++) {
            this.drawDot(positions[l].x, positions[l].y);
        }
    }
}
