import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IWumpus } from 'app/shared/model/wumpus.model';
import { Principal } from 'app/core';
import { WumpusService } from './wumpus.service';

@Component({
    selector: 'jhi-wumpus',
    templateUrl: './wumpus.component.html'
})
export class WumpusComponent implements OnInit, OnDestroy {
    wumpuses: IWumpus[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private wumpusService: WumpusService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.wumpusService.query().subscribe(
            (res: HttpResponse<IWumpus[]>) => {
                this.wumpuses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInWumpuses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IWumpus) {
        return item.id;
    }

    registerChangeInWumpuses() {
        this.eventSubscriber = this.eventManager.subscribe('wumpusListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
