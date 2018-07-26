export interface IEnum {
    es?: string;
    en?: string;
    icon?: string;
}

export class Enum implements IEnum {
    constructor(
        public es?: string,
        public en?: string,
        public icon?: string
    ) {}
}
