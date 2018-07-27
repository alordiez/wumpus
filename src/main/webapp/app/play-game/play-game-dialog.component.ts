import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGame } from 'app/shared/model/game.model';
import { PlayGameService } from './play-game.service';

@Component({
    selector: 'jhi-play-game-dialog',
    templateUrl: './play-game-dialog.component.html'
})
export class PlayGameDialogComponent {
    game: IGame;

    constructor(private gameService: PlayGameService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    restart(id: number) {
      this.eventManager.broadcast({
        name: 'restartGame',
        content: 'Restart game'
      });
      this.activeModal.dismiss(true);
    }
}

@Component({
    selector: 'jhi-play-game-popup',
    template: ''
})
export class PlayGamePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ game }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PlayGameDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.game = game;
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
