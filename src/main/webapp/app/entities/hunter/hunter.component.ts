import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IHunter } from 'app/shared/model/hunter.model';
import { Principal } from 'app/core';
import { HunterService } from './hunter.service';

@Component({
    selector: 'jhi-hunter',
    templateUrl: './hunter.component.html'
})
export class HunterComponent implements OnInit, OnDestroy {
    hunters: IHunter[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private hunterService: HunterService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.hunterService.query().subscribe(
            (res: HttpResponse<IHunter[]>) => {
                this.hunters = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInHunters();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IHunter) {
        return item.id;
    }

    registerChangeInHunters() {
        this.eventSubscriber = this.eventManager.subscribe('hunterListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
