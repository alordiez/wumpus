import { IGamePits } from 'app/shared/model//game-pits.model';

export interface IGame {
    id?: number;
    width?: number;
    height?: number;
    pitNumber?: number;
    arrows?: number;
    goldPosition?: number;
    playerId?: number;
    gamePits?: IGamePits[];
    wumpusId?: number;
}

export class Game implements IGame {
    constructor(
        public id?: number,
        public width?: number,
        public height?: number,
        public pitNumber?: number,
        public arrows?: number,
        public goldPosition?: number,
        public playerId?: number,
        public gamePits?: IGamePits[],
        public wumpusId?: number
    ) {}
}
