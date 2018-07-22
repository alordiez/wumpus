import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IHunter } from 'app/shared/model/hunter.model';
import { HunterService } from './hunter.service';

@Component({
    selector: 'jhi-hunter-update',
    templateUrl: './hunter-update.component.html'
})
export class HunterUpdateComponent implements OnInit {
    private _hunter: IHunter;
    isSaving: boolean;

    constructor(private hunterService: HunterService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ hunter }) => {
            this.hunter = hunter;
        });
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
    get hunter() {
        return this._hunter;
    }

    set hunter(hunter: IHunter) {
        this._hunter = hunter;
    }
}
