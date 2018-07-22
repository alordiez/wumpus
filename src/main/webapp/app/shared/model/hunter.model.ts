export interface IHunter {
    id?: number;
    position?: number;
    isAlive?: boolean;
}

export class Hunter implements IHunter {
    constructor(public id?: number, public position?: number, public isAlive?: boolean) {
        this.isAlive = false;
    }
}
