import { IField } from 'app/shared/model//field.model';

export interface IGame {
    id?: number;
    width?: number;
    height?: number;
    pitNumber?: number;
    arrows?: number;
    movements?: number[];
    board?: { [key: number]: { [key: number]: IField; }; };
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
    ) {}
}
