import { IGamePits } from 'app/shared/model//game-pits.model';

export interface IGame {
    id?: number;
    width?: number;
    height?: number;
    pitNumber?: number;
    arrows?: number;
    goldPosition?: number;
    gamePits?: IGamePits[];
    wumpusId?: number;
    hunterId?: number;
}

export class Game implements IGame {
    constructor(
        public id?: number,
        public width?: number,
        public height?: number,
        public pitNumber?: number,
        public arrows?: number,
        public goldPosition?: number,
        public gamePits?: IGamePits[],
        public wumpusId?: number,
        public hunterId?: number
    ) {}
}
