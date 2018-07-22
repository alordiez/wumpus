import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGamePits } from 'app/shared/model/game-pits.model';

@Component({
    selector: 'jhi-game-pits-detail',
    templateUrl: './game-pits-detail.component.html'
})
export class GamePitsDetailComponent implements OnInit {
    gamePits: IGamePits;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gamePits }) => {
            this.gamePits = gamePits;
        });
    }

    previousState() {
        window.history.back();
    }
}
