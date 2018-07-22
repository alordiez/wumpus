import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IGame } from 'app/shared/model/game.model';
import { GameService } from './game.service';
import { IWumpus } from 'app/shared/model/wumpus.model';
import { WumpusService } from 'app/entities/wumpus';
import { IHunter } from 'app/shared/model/hunter.model';
import { HunterService } from 'app/entities/hunter';

@Component({
    selector: 'jhi-game-update',
    templateUrl: './game-update.component.html'
})
export class GameUpdateComponent implements OnInit {
    private _game: IGame;
    isSaving: boolean;

    wumpuses: IWumpus[];

    hunters: IHunter[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private gameService: GameService,
        private wumpusService: WumpusService,
        private hunterService: HunterService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ game }) => {
            this.game = game;
        });
        this.wumpusService.query({ filter: 'game-is-null' }).subscribe(
            (res: HttpResponse<IWumpus[]>) => {
                if (!this.game.wumpusId) {
                    this.wumpuses = res.body;
                } else {
                    this.wumpusService.find(this.game.wumpusId).subscribe(
                        (subRes: HttpResponse<IWumpus>) => {
                            this.wumpuses = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.hunterService.query({ filter: 'game-is-null' }).subscribe(
            (res: HttpResponse<IHunter[]>) => {
                if (!this.game.hunterId) {
                    this.hunters = res.body;
                } else {
                    this.hunterService.find(this.game.hunterId).subscribe(
                        (subRes: HttpResponse<IHunter>) => {
                            this.hunters = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.game.id !== undefined) {
            this.subscribeToSaveResponse(this.gameService.update(this.game));
        } else {
            this.subscribeToSaveResponse(this.gameService.create(this.game));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGame>>) {
        result.subscribe((res: HttpResponse<IGame>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackWumpusById(index: number, item: IWumpus) {
        return item.id;
    }

    trackHunterById(index: number, item: IHunter) {
        return item.id;
    }
    get game() {
        return this._game;
    }

    set game(game: IGame) {
        this._game = game;
    }
}
