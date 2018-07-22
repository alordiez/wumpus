export interface IHunter {
    id?: number;
    position?: number;
    isAlive?: boolean;
    gameId?: number;
}

export class Hunter implements IHunter {
    constructor(public id?: number, public position?: number, public isAlive?: boolean, public gameId?: number) {
        this.isAlive = false;
    }
}
