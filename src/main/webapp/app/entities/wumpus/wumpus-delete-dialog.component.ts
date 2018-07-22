import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWumpus } from 'app/shared/model/wumpus.model';
import { WumpusService } from './wumpus.service';

@Component({
    selector: 'jhi-wumpus-delete-dialog',
    templateUrl: './wumpus-delete-dialog.component.html'
})
export class WumpusDeleteDialogComponent {
    wumpus: IWumpus;

    constructor(private wumpusService: WumpusService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wumpusService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'wumpusListModification',
                content: 'Deleted an wumpus'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-wumpus-delete-popup',
    template: ''
})
export class WumpusDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ wumpus }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(WumpusDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.wumpus = wumpus;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
