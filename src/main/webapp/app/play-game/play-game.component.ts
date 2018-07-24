import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IGame } from '../shared/model/game.model';
import { Principal } from 'app/core';
import { PlayGameService } from './play-game.service';

@Component({
    selector: 'jhi-play-game',
    templateUrl: './play-game.component.html'
})
export class PlayGameComponent implements OnInit, OnDestroy {
    games: IGame[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private playGameService: PlayGameService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.playGameService.query().subscribe(
            (res: HttpResponse<IGame[]>) => {
                this.games = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInGames();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IGame) {
        return item.id;
    }

    registerChangeInGames() {
        this.eventSubscriber = this.eventManager.subscribe('gamesListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
