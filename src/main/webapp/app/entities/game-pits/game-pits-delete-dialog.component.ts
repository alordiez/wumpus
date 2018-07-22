import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGamePits } from 'app/shared/model/game-pits.model';
import { GamePitsService } from './game-pits.service';

@Component({
    selector: 'jhi-game-pits-delete-dialog',
    templateUrl: './game-pits-delete-dialog.component.html'
})
export class GamePitsDeleteDialogComponent {
    gamePits: IGamePits;

    constructor(private gamePitsService: GamePitsService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.gamePitsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'gamePitsListModification',
                content: 'Deleted an gamePits'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-game-pits-delete-popup',
    template: ''
})
export class GamePitsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gamePits }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GamePitsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.gamePits = gamePits;
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
