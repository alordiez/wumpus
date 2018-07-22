import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IHunter } from 'app/shared/model/hunter.model';
import { HunterService } from './hunter.service';
import { IGame } from 'app/shared/model/game.model';
import { GameService } from 'app/entities/game';

@Component({
    selector: 'jhi-hunter-update',
    templateUrl: './hunter-update.component.html'
})
export class HunterUpdateComponent implements OnInit {
    private _hunter: IHunter;
    isSaving: boolean;

    games: IGame[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private hunterService: HunterService,
        private gameService: GameService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ hunter }) => {
            this.hunter = hunter;
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
        if (this.hunter.id !== undefined) {
            this.subscribeToSaveResponse(this.hunterService.update(this.hunter));
        } else {
            this.subscribeToSaveResponse(this.hunterService.create(this.hunter));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IHunter>>) {
        result.subscribe((res: HttpResponse<IHunter>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get hunter() {
        return this._hunter;
    }

    set hunter(hunter: IHunter) {
        this._hunter = hunter;
    }
}
