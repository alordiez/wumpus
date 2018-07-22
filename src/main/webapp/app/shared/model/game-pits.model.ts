export interface IGamePits {
    id?: number;
    position?: number;
    gameId?: number;
}

export class GamePits implements IGamePits {
    constructor(public id?: number, public position?: number, public gameId?: number) {}
}
