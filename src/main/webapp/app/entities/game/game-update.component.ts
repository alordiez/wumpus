import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IGame } from 'app/shared/model/game.model';
import { GameService } from './game.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player';
import { IWumpus } from 'app/shared/model/wumpus.model';
import { WumpusService } from 'app/entities/wumpus';

@Component({
    selector: 'jhi-game-update',
    templateUrl: './game-update.component.html'
})
export class GameUpdateComponent implements OnInit {
    private _game: IGame;
    isSaving: boolean;

    players: IPlayer[];

    wumpuses: IWumpus[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private gameService: GameService,
        private playerService: PlayerService,
        private wumpusService: WumpusService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ game }) => {
            this.game = game;
        });
        this.playerService.query({ filter: 'game-is-null' }).subscribe(
            (res: HttpResponse<IPlayer[]>) => {
                if (!this.game.playerId) {
                    this.players = res.body;
                } else {
                    this.playerService.find(this.game.playerId).subscribe(
                        (subRes: HttpResponse<IPlayer>) => {
                            this.players = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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

    trackPlayerById(index: number, item: IPlayer) {
        return item.id;
    }

    trackWumpusById(index: number, item: IWumpus) {
        return item.id;
    }
    get game() {
        return this._game;
    }

    set game(game: IGame) {
        this._game = game;
    }
}
