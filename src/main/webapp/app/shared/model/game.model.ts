import { IField } from 'app/shared/model//field.model';

export interface IGame {
    id?: number;
    width?: number;
    height?: number;
    pitNumber?: number;
    arrows?: number;
    movements?: number[];
    board?: { [key: number]: { [key: number]: IField; }; };
    hunterAlive?: boolean;
    wumpusAlive?: boolean;
    gameover?: boolean;
    goldObtained?: boolean;
    win?: boolean;
}

export class Game implements IGame {
    constructor(
        public id?: number,
        public width?: number,
        public height?: number,
        public pitNumber?: number,
        public arrows?: number,
        public movements?: number[],
        public board?: IField[][],
        public hunterAlive?: boolean,
        public wumpusAlive?: boolean,
        public gameover?: boolean,
        public goldObtained?: boolean,
        public win?: boolean,
    ) {}
}
