import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IGamePits } from 'app/shared/model/game-pits.model';
import { GamePitsService } from './game-pits.service';

@Component({
    selector: 'jhi-game-pits-update',
    templateUrl: './game-pits-update.component.html'
})
export class GamePitsUpdateComponent implements OnInit {
    private _gamePits: IGamePits;
    isSaving: boolean;

    constructor(private gamePitsService: GamePitsService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ gamePits }) => {
            this.gamePits = gamePits;
        });
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
    get gamePits() {
        return this._gamePits;
    }

    set gamePits(gamePits: IGamePits) {
        this._gamePits = gamePits;
    }
}
