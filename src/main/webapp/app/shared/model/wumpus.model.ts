export interface IWumpus {
    id?: number;
    position?: number;
    isAlive?: boolean;
    gameId?: number;
}

export class Wumpus implements IWumpus {
    constructor(public id?: number, public position?: number, public isAlive?: boolean, public gameId?: number) {
        this.isAlive = false;
    }
}
