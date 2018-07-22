export interface IWumpus {
    id?: number;
    position?: number;
    isAlive?: boolean;
}

export class Wumpus implements IWumpus {
    constructor(public id?: number, public position?: number, public isAlive?: boolean) {
        this.isAlive = false;
    }
}
