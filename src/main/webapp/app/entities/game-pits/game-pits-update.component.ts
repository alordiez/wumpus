import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IGamePits } from 'app/shared/model/game-pits.model';
import { GamePitsService } from './game-pits.service';
import { IGame } from 'app/shared/model/game.model';
import { GameService } from 'app/entities/game';

@Component({
    selector: 'jhi-game-pits-update',
    templateUrl: './game-pits-update.component.html'
})
export class GamePitsUpdateComponent implements OnInit {
    private _gamePits: IGamePits;
    isSaving: boolean;

    games: IGame[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private gamePitsService: GamePitsService,
        private gameService: GameService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ gamePits }) => {
            this.gamePits = gamePits;
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
        if (this.gamePits.id !== undefined) {
            this.subscribeToSaveResponse(this.gamePitsService.update(this.gamePits));
        } else {
            this.subscribeToSaveResponse(this.gamePitsService.create(this.gamePits));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGamePits>>) {
        result.subscribe((res: HttpResponse<IGamePits>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get gamePits() {
        return this._gamePits;
    }

    set gamePits(gamePits: IGamePits) {
        this._gamePits = gamePits;
    }
}
