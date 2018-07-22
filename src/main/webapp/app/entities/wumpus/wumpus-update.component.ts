import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IWumpus } from 'app/shared/model/wumpus.model';
import { WumpusService } from './wumpus.service';

@Component({
    selector: 'jhi-wumpus-update',
    templateUrl: './wumpus-update.component.html'
})
export class WumpusUpdateComponent implements OnInit {
    private _wumpus: IWumpus;
    isSaving: boolean;

    constructor(private wumpusService: WumpusService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ wumpus }) => {
            this.wumpus = wumpus;
        });
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
    get wumpus() {
        return this._wumpus;
    }

    set wumpus(wumpus: IWumpus) {
        this._wumpus = wumpus;
    }
}
