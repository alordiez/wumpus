import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IGamePits } from 'app/shared/model/game-pits.model';
import { Principal } from 'app/core';
import { GamePitsService } from './game-pits.service';

@Component({
    selector: 'jhi-game-pits',
    templateUrl: './game-pits.component.html'
})
export class GamePitsComponent implements OnInit, OnDestroy {
    gamePits: IGamePits[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private gamePitsService: GamePitsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.gamePitsService.query().subscribe(
            (res: HttpResponse<IGamePits[]>) => {
                this.gamePits = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInGamePits();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IGamePits) {
        return item.id;
    }

    registerChangeInGamePits() {
        this.eventSubscriber = this.eventManager.subscribe('gamePitsListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
