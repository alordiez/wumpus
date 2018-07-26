import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IGame } from '../shared/model/game.model';
import { IField } from '../shared/model/field.model';
import { IEnum } from '../shared/model/enum.model';
import { Principal } from 'app/core';
import { PlayGameService } from './play-game.service';

@Component({
    selector: 'jhi-play-game',
    templateUrl: './play-game.component.html',
    styleUrls: ['play-game.css']
})
export class PlayGameComponent implements OnInit, OnDestroy {
    games: IGame[];
    game: IGame;
    currentAccount: any;
    eventSubscriber: Subscription;
    gameStarted: boolean;

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
        this.gameStarted = false;
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

    getKeys(map) {
      if (map) {
        const tmpMap = new Map(Object.entries(map));
        return Array.from(tmpMap.keys());
      }
      return null;
    }

    printElements(field) {
      if (field) {
        let str = '';
        field.elements.forEach(function(item) {
          str = item ?
            str ? str + ',' + item : item
            : str;
        });
        return str;
      }
      return '';
    }

    printPerceptions(field) {
      if (field) {
        let str = '';
        field.perceptions.forEach(function(item) {
          str = item ?
            str ? str + ',' + item : item
            : str;
        });
        return str;
      }
      return '';
    }

    registerChangeInGames() {
        this.eventSubscriber = this.eventManager.subscribe('gamesListModification', response => this.loadAll());
    }

    getDescription(game: IGame) {
      return game.id + '/' + game.width + 'x' + game.height;
    }

    startGame() {
      this.playGameService.startGame(this.game.id).subscribe(
          (res: HttpResponse<IGame>) => {
              this.gameStarted = true;
              this.game = res.body;
          },
          (res: HttpErrorResponse) => this.onError(res.message)
      );
    }

    endGame() {
      this.gameStarted = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    moveUp() {

    }

    moveDown() {

    }

    moveLeft() {

    }

    moveRight() {

    }

    shootUp() {

    }

    shootDown() {

    }

    shootLeft() {

    }

    shootRight() {

    }
}
