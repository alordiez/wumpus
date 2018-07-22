export interface IGamePits {
    id?: number;
    position?: number;
}

export class GamePits implements IGamePits {
    constructor(public id?: number, public position?: number) {}
}
