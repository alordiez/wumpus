import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWumpus } from 'app/shared/model/wumpus.model';

@Component({
    selector: 'jhi-wumpus-detail',
    templateUrl: './wumpus-detail.component.html'
})
export class WumpusDetailComponent implements OnInit {
    wumpus: IWumpus;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ wumpus }) => {
            this.wumpus = wumpus;
        });
    }

    previousState() {
        window.history.back();
    }
}
