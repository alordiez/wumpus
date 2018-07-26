import { IEnum } from 'app/shared/model//enum.model';

export interface IField {
    position?: number;
    perceptions?: IEnum[];
    elements?: IEnum[];
    visited?: boolean;
}

export class Field implements IField {
    constructor(
        public position?: number,
        public perceptions?: IEnum[],
        public elements?: IEnum[],
        public visited?: boolean
    ) {}
}
