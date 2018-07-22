import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWumpus } from 'app/shared/model/wumpus.model';
import { WumpusService } from './wumpus.service';
import { IGame } from 'app/shared/model/game.model';
import { GameService } from 'app/entities/game';

@Component({
    selector: 'jhi-wumpus-update',
    templateUrl: './wumpus-update.component.html'
})
export class WumpusUpdateComponent implements OnInit {
    private _wumpus: IWumpus;
    isSaving: boolean;

    games: IGame[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private wumpusService: WumpusService,
        private gameService: GameService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ wumpus }) => {
            this.wumpus = wumpus;
        });
        this.gameService.query().subscribe(
            (res: HttpResponse<IGame[]>) => {
                this.games = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.wumpus.id !== undefined) {
            this.subscribeToSaveResponse(this.wumpusService.update(this.wumpus));
        } else {
            this.subscribeToSaveResponse(this.wumpusService.create(this.wumpus));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWumpus>>) {
        result.subscribe((res: HttpResponse<IWumpus>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackGameById(index: number, item: IGame) {
        return item.id;
    }
    get wumpus() {
        return this._wumpus;
    }

    set wumpus(wumpus: IWumpus) {
        this._wumpus = wumpus;
    }
}
